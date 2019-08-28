package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.project.Ontology;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class OntologyDiffCalculator {


    private AnnotationDiffCalculator annotationDiffCalculator;

    private AxiomDiffCalculator axiomDiffCalculator;

    @Inject
    public OntologyDiffCalculator(AnnotationDiffCalculator annotationDiffCalculator, AxiomDiffCalculator axiomDiffCalculator) {
        this.annotationDiffCalculator = annotationDiffCalculator;
        this.axiomDiffCalculator = axiomDiffCalculator;
    }

    public OntologyDiff computeDiff(Ontology from, Ontology to) {
        Diff<OWLAxiom> axiomDiff = axiomDiffCalculator.computeDiff(from, to);
        Diff<OWLAnnotation> annotationDiff = annotationDiffCalculator.computeDiff(from, to);
        return new OntologyDiff(from.getOntologyId(), to.getOntologyId(), annotationDiff, axiomDiff);
    }
}
