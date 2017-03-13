package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
@ProjectSingleton
public class RevisionManagerImpl implements RevisionManager {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final RevisionStore revisionStore;

    @Inject
    public RevisionManagerImpl(@Nonnull RevisionStore revisionStore) {
        this.revisionStore = checkNotNull(revisionStore);
    }

    @Nonnull
    @Override
    public Revision addRevision(@Nonnull UserId userId, @Nonnull List<? extends OWLOntologyChangeRecord> changes, @Nonnull String desc) {
        try {
            writeLock.lock();
            long timestamp = System.currentTimeMillis();
            RevisionNumber revisionNumber = revisionStore.getCurrentRevisionNumber().getNextRevisionNumber();
            final String highlevelDescription = desc != null ? desc : "";
            final Revision revision = new Revision(
                    userId,
                    revisionNumber,
                    ImmutableList.copyOf(changes),
                    timestamp,
                    highlevelDescription);
            revisionStore.addRevision(revision);
            return revision;
        } finally {
            writeLock.unlock();
        }
    }



    @Nonnull
    @Override
    public RevisionNumber getCurrentRevision() {
        return revisionStore.getCurrentRevisionNumber();
    }

    @Nonnull
    @Override
    public OWLOntologyManager getOntologyManagerForRevision(@Nonnull RevisionNumber revision) {
         try {
            OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
            final OWLOntologyID singletonOntologyId = new OWLOntologyID();
            for (Revision rev : revisionStore.getRevisions()) {
                if(rev.getRevisionNumber().compareTo(revision) <= 0)
                for (OWLOntologyChangeRecord record : rev) {
                    // Anonymous ontologies are not handled nicely at all.
                    OWLOntologyChangeRecord normalisedChangeRecord = normaliseChangeRecord(record, singletonOntologyId);
                    OWLOntologyID ontologyId = normalisedChangeRecord.getOntologyID();
                    if (!manager.contains(ontologyId)) {
                        manager.createOntology(ontologyId);
                    }

                    OWLOntologyChange change = normalisedChangeRecord.createOntologyChange(manager);
                    manager.applyChange(change);
                }
            }
            if (manager.getOntologies().isEmpty()) {
                // No revisions exported.  Just create an empty ontology
                manager.createOntology();
            }
            return manager;
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Problem creating ontology: " + e);
        }
    }

    private OWLOntologyChangeRecord normaliseChangeRecord(OWLOntologyChangeRecord changeRecord, OWLOntologyID singletonAnonymousId) {
        OWLOntologyID ontologyID = changeRecord.getOntologyID();
        if (ontologyID.isAnonymous()) {
            return new OWLOntologyChangeRecord(singletonAnonymousId, changeRecord.getData());
        } else {
            // As is
            return changeRecord;
        }
    }

    @Nonnull
    @Override
    public List<Revision> getRevisions() {
        return revisionStore.getRevisions();
    }

    /**
     * Gets the specified revision
     * @param revisionNumber The revision number of the revision to return
     * @return The revision that has the specified revision number, or absent if the revision with the specfied
     * revision number does not exist.
     */
    @Nonnull
    @Override
    public Optional<Revision> getRevision(@Nonnull RevisionNumber revisionNumber) {
        return revisionStore.getRevision(revisionNumber);
    }

    @Nonnull
    @Override
    public Optional<RevisionSummary> getRevisionSummary(@Nonnull RevisionNumber revisionNumber) {
        Optional<Revision> revision = revisionStore.getRevision(revisionNumber);
        if(!revision.isPresent()) {
            return Optional.empty();
        }
        else {
            return Optional.of(toRevisionSummary(revision.get()));
        }

    }


    @Nonnull
    @Override
    public List<RevisionSummary> getRevisionSummaries() {
        return revisionStore.getRevisions().stream()
                .map(RevisionManagerImpl::toRevisionSummary)
                .collect(toList());
    }

    private static RevisionSummary toRevisionSummary(Revision revision) {
        return new RevisionSummary(revision.getRevisionNumber(),
                                   revision.getUserId(),
                                   revision.getTimestamp(),
                                   revision.getSize(),
                                   revision.getHighLevelDescription());
    }

}
