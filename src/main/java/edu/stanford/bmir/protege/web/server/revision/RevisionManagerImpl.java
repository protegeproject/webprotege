package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

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
    public RevisionManagerImpl(RevisionStore revisionStore) {
        this.revisionStore = checkNotNull(revisionStore);
    }

    @Override
    public Revision addRevision(UserId userId, List<? extends OWLOntologyChangeRecord> changes, String desc) {
        try {
            writeLock.lock();
            long timestamp = System.currentTimeMillis();
            RevisionNumber revisionNumber = getCurrentRevision().getNextRevisionNumber();
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



    @Override
    public RevisionNumber getCurrentRevision() {
        return revisionStore.getCurrentRevisionNumber();
    }

    @Override
    public OWLOntologyManager getOntologyManagerForRevision(RevisionNumber revision) {
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
    @Override
    public Optional<Revision> getRevision(RevisionNumber revisionNumber) {
        return revisionStore.getRevision(revisionNumber);
    }

    @Override
    public Optional<RevisionSummary> getRevisionSummary(RevisionNumber revisionNumber) {
        Optional<Revision> revision = revisionStore.getRevision(revisionNumber);
        if(!revision.isPresent()) {
            return Optional.absent();
        }
        else {
            return Optional.of(getRevisionSummaryFromRevision(revision.get()));
        }

    }


    @Override
    public List<RevisionSummary> getRevisionSummaries() {
        List<RevisionSummary> result = new ArrayList<>();
        for(Revision revision : revisionStore.getRevisions()) {
            result.add(getRevisionSummaryFromRevision(revision));
        }
        return result;
    }

    private RevisionSummary getRevisionSummaryFromRevision(Revision revision) {
        return new RevisionSummary(revision.getRevisionNumber(), revision.getUserId(), revision.getTimestamp(), revision.getSize(), revision.getHighLevelDescription());
    }

}
