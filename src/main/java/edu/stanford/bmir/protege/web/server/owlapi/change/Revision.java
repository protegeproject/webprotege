package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;


import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/07/2013
 */
public class Revision implements Iterable<OWLOntologyChangeRecord>, Comparable<Revision> {


    private static final int DESCRIPTION_MAX_CHANGE_COUNT = 20;

    private UserId userId;

    private RevisionNumber revision;

    private long timestamp;

    private OWLOntologyChangeRecordList changes;

    private String highLevelDescription;

    private RevisionType revisionType;

    private ImmutableSet<OWLEntity> cachedEntities = null;

    public Revision(UserId userId, RevisionNumber revision, List<OWLOntologyChangeRecord> changes, long timestamp, String highLevelDescription, RevisionType revisionType) {
        this.changes = checkNotNull(new OWLOntologyChangeRecordList(changes));
        this.userId = checkNotNull(userId);
        this.revision = checkNotNull(revision);
        this.timestamp = timestamp;
        this.highLevelDescription = checkNotNull(highLevelDescription);
        this.revisionType = checkNotNull(revisionType);
    }

    public Revision(RevisionNumber revision) {
        this.userId = UserId.getGuest();
        this.revision = revision;
        this.timestamp = 0;
        this.changes = new OWLOntologyChangeRecordList();
        this.highLevelDescription = "";
        this.revisionType = RevisionType.EDIT;
    }

    public int getSize() {
        return changes.size();
    }

    public static Revision createEmptyRevisionWithRevisionNumber(RevisionNumber revision) {
        return new Revision(revision);
    }

    public static Revision createEmptyRevisionWithTimestamp(long timestamp) {
        Revision revision = new Revision(RevisionNumber.getRevisionNumber(0));
        revision.timestamp = timestamp;
        return revision;
    }

    public boolean containsEntity(OWLAPIProject project, OWLEntity entity) {
        return getEntities(project).contains(entity);
    }

    public ImmutableSet<OWLEntity> getEntities(OWLAPIProject project) {
        if (cachedEntities == null) {
            cachedEntities = getEntitiesInternal(project);
        }
        return cachedEntities;
    }

    private ImmutableSet<OWLEntity> getEntitiesInternal(OWLAPIProject project) {
        ImmutableSet.Builder<OWLEntity> result = ImmutableSet.builder();
        Set<IRI> iris = new HashSet<>();
        AxiomSubjectProvider axiomSubjectProvider = project.getAxiomSubjectProvider();
        for (OWLOntologyChangeRecord change : changes) {
            if (change.getData() instanceof AxiomChangeData) {
                OWLAxiom ax = ((AxiomChangeData) change.getData()).getAxiom();
                Optional<? extends OWLObject> subject = axiomSubjectProvider.getSubject(ax);
                if (subject.isPresent()) {
                    if (subject.get() instanceof OWLEntity) {
                        result.add((OWLEntity) subject.get());
                    }
                    else if (subject.get() instanceof IRI) {
                        iris.add((IRI) subject.get());
                    }
                }
            }
        }
        for (IRI iri : iris) {
            result.add(project.getDataFactory().getOWLClass(iri));
            result.add(project.getDataFactory().getOWLObjectProperty(iri));
            result.add(project.getDataFactory().getOWLDataProperty(iri));
            result.add(project.getDataFactory().getOWLAnnotationProperty(iri));
            result.add(project.getDataFactory().getOWLNamedIndividual(iri));
            result.add(project.getDataFactory().getOWLDatatype(iri));
        }
        return result.build();
    }


    public long getTimestamp() {
        return timestamp;
    }

    public UserId getUserId() {
        return userId;
    }

    public RevisionNumber getRevisionNumber() {
        return revision;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public int compareTo(Revision o) {
        return this.revision.compareTo(o.revision);
    }

    public String getHighLevelDescription(final OWLAPIProject project, OWLEntity entity) {
        return highLevelDescription != null ? highLevelDescription : "";
    }


    public String getHighLevelDescription(final OWLAPIProject project) {
        return getHighLevelDescription(project, null);
    }

    public Iterator<OWLOntologyChangeRecord> iterator() {
        return changes.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(highLevelDescription);
        sb.append("\n");
        for (OWLOntologyChangeRecord change : changes) {
            sb.append(change);
            sb.append("\n");
        }
        return sb.toString();
    }



    public static class RevisionTimeStampComparator implements Comparator<Revision> {

        public int compare(Revision o1, Revision o2) {
            if (o1.timestamp < o2.timestamp) {
                return -1;
            }
            else if (o1.timestamp == o2.timestamp) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

}
