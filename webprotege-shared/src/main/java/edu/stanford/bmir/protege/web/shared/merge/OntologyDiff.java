package edu.stanford.bmir.protege.web.shared.merge;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class OntologyDiff implements IsSerializable {

    private OntologyDocumentId fromOntologyId;

    private OntologyDocumentId toOntologyId;

    private Diff<OWLAnnotation> annotationDiff;

    private Diff<OWLAxiom> axiomDiff;

    private OntologyDiff() {
    }

    public OntologyDiff(OntologyDocumentId fromOntologyId, OntologyDocumentId toOntologyId, Diff<OWLAnnotation> annotationDiff, Diff<OWLAxiom> axiomDiff) {
        this.fromOntologyId = checkNotNull(fromOntologyId);
        this.toOntologyId = checkNotNull(toOntologyId);
        this.annotationDiff = checkNotNull(annotationDiff);
        this.axiomDiff = checkNotNull(axiomDiff);
    }

    public OntologyDocumentId getFromOntologyId() {
        return fromOntologyId;
    }

    public OntologyDocumentId getToOntologyId() {
        return toOntologyId;
    }

    public boolean isEmpty() {
        return annotationDiff.isEmpty() && axiomDiff.isEmpty();
    }

    public Diff<OWLAnnotation> getAnnotationDiff() {
        return annotationDiff;
    }

    public Diff<OWLAxiom> getAxiomDiff() {
        return axiomDiff;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fromOntologyId, toOntologyId, annotationDiff, axiomDiff);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OntologyDiff)) {
            return false;
        }
        OntologyDiff other = (OntologyDiff) obj;
        return this.fromOntologyId.equals(other.fromOntologyId)
                && this.toOntologyId.equals(other.toOntologyId)
                && this.annotationDiff.equals(other.annotationDiff)
                && this.axiomDiff.equals(other.axiomDiff);
    }


    @Override
    public String toString() {
        return toStringHelper("OntologyDiff")
                .addValue(fromOntologyId)
                .addValue(toOntologyId)
                .addValue(annotationDiff)
                .addValue(axiomDiff)
                .toString();
    }
}
