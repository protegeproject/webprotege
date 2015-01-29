package edu.stanford.bmir.protege.web.shared.merge;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class OntologyDiff implements IsSerializable {

    private Diff<OWLAnnotation> annotationDiff;

    private Diff<OWLAxiom> axiomDiff;

    private OntologyDiff() {
    }

    public OntologyDiff(Diff<OWLAnnotation> annotationDiff, Diff<OWLAxiom> axiomDiff) {
        this.annotationDiff = annotationDiff;
        this.axiomDiff = axiomDiff;
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
}
