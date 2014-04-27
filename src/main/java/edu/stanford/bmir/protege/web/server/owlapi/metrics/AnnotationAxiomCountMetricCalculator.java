package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.IntegerMetricValue;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class AnnotationAxiomCountMetricCalculator extends MetricCalculator {

    public AnnotationAxiomCountMetricCalculator(OWLOntology project) {
        super(project);
    }

    @Override
    public MetricValue computeValue() {
        int count = 0;
        for(OWLOntology ontology : getRootOntology().getImportsClosure()) {
            count += (ontology.getAxiomCount() - ontology.getLogicalAxiomCount());
        }
        return new IntegerMetricValue("Annotation axioms", count);
    }

    @Override
    public OWLAPIProjectMetricState getStateAfterChanges(List<? extends OWLOntologyChange> changes) {
        for(OWLOntologyChange change : changes) {
            if(change.isAxiomChange()) {
                if(change.getAxiom() instanceof OWLAnnotationAxiom) {
                    return OWLAPIProjectMetricState.DIRTY;
                }
            }
        }
        return OWLAPIProjectMetricState.CLEAN;
    }
}
