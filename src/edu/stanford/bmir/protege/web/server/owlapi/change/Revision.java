package edu.stanford.bmir.protege.web.server.owlapi.change;

import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

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

    private Set<OWLEntity> cachedEntities = null;

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

    public Set<OWLEntity> getEntities(OWLAPIProject project) {
        if (cachedEntities == null) {
            cachedEntities = getEntitiesInternal(project);
        }
        return cachedEntities;
    }

    private Set<OWLEntity> getEntitiesInternal(OWLAPIProject project) {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        Set<IRI> iris = new HashSet<IRI>();
        for (OWLOntologyChangeRecord change : changes) {
            if (change.getData() instanceof AxiomChangeData) {
                OWLAxiom ax = ((AxiomChangeData) change.getData()).getAxiom();
                AxiomSubjectProvider axiomSubjectProvider = new AxiomSubjectProvider();
                OWLObject object = axiomSubjectProvider.getSubject(ax);
                if (object instanceof OWLEntity) {
                    result.add((OWLEntity) object);
                }
                else if (object instanceof IRI) {
                    iris.add((IRI) object);
                }
            }
        }
        for (IRI iri : iris) {
            result.addAll(project.getRootOntology().getEntitiesInSignature(iri));
        }
        return result;
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
        StringBuilder sb = new StringBuilder();
        if (highLevelDescription != null) {
            sb.append(highLevelDescription);
        }
        sb.append("<div style=\"width: 100%;\">");
        sb.append("<div><b>Details:</b></div>");
        sb.append("<div style=\"margin-left: 20px\">");
        int counter = 0;
        for (final OWLOntologyChangeRecord changeRecord : changes) {
            if (changeRecord.getData() instanceof AxiomChangeData) {
                AxiomChangeData info = (AxiomChangeData) changeRecord.getData();
                OWLAxiom axiom = info.getAxiom();
                if (entity == null || isEntitySubjectOfChange(entity, info)) {
                    sb.append("<div style=\"overflow: hidden;\">");
                    String ren = changeRecord.getData().accept(new OWLOntologyChangeDataVisitor<String, RuntimeException>() {

                        public String visit(AddAxiomData addAxiom) {
                            final RenderingManager rm = project.getRenderingManager();
                            return "<b>Added: </b> " + rm.getBrowserText(addAxiom.getAxiom());
                        }

                        public String visit(RemoveAxiomData removeAxiom) {
                            final RenderingManager rm = project.getRenderingManager();
                            return new StringBuilder().append("<b>Removed: </b> ").append(rm.getBrowserText(removeAxiom.getAxiom())).toString();
                        }

                        public String visit(SetOntologyIDData setOntologyID) {
                            return new StringBuilder().append("Changed ontology id from ").append(changeRecord.getOntologyID()).append(" to ").append(setOntologyID.getNewId()).toString();
                        }

                        public String visit(AddImportData addImport) {
                            return new StringBuilder().append("Added import: ").append(addImport.getDeclaration().getIRI().toQuotedString()).toString();
                        }

                        public String visit(RemoveImportData removeImport) {
                            return new StringBuilder().append("Removed import: ").append(removeImport.getDeclaration().getIRI().toQuotedString()).toString();
                        }

                        public String visit(AddOntologyAnnotationData addOntologyAnnotation) {
                            return new StringBuilder().append("Added annotation to ontology: ").append(project.getRenderingManager().getBrowserText(addOntologyAnnotation.getAnnotation())).toString();
                        }

                        public String visit(RemoveOntologyAnnotationData removeOntologyAnnotation) {
                            return new StringBuilder().append("Removed annotation from ontology: ").append(project.getRenderingManager().getBrowserText(removeOntologyAnnotation.getAnnotation())).toString();
                        }
                    });
                    sb.append(ren);
                    sb.append("</div>");

                }
                if (counter == DESCRIPTION_MAX_CHANGE_COUNT && getSize() > DESCRIPTION_MAX_CHANGE_COUNT) {
                    sb.append("<div>");
                    sb.append(" + ");
                    sb.append(getSize() - DESCRIPTION_MAX_CHANGE_COUNT);
                    sb.append(" more");
                    sb.append("</div>");
                    break;
                }
                counter++;
            }
        }

        sb.append("</div>");
        sb.append("</div>");

        return sb.toString();
    }

    private boolean isEntitySubjectOfChange(OWLEntity entity, AxiomChangeData change) {
        OWLObject changeSubject = getChangeSubject(change);
        return entity.equals(changeSubject) || entity.getIRI().equals(changeSubject);
    }

    private OWLObject getChangeSubject(AxiomChangeData change) {
        AxiomSubjectProvider subjectProvider = new AxiomSubjectProvider();

        return subjectProvider.getSubject(change.getAxiom());
    }

    public String getHighLevelDescription(final OWLAPIProject project) {
        return getHighLevelDescription(project, null);
    }

//    public List<OWLOntologyChangeRecord> getChanges() {
//        return changes;
//    }

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
