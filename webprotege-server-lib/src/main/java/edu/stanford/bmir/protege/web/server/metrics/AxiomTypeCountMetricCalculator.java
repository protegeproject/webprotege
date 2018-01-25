package edu.stanford.bmir.protege.web.server.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.IntegerMetricValue;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class AxiomTypeCountMetricCalculator extends MetricCalculator {

    private AxiomType<?> type;

    private String metricName;
    
    public AxiomTypeCountMetricCalculator(OWLOntology project, AxiomType<?> type) {
        super(project);
        this.type = type;
        metricName = type.getName() + " axioms";
    }

    @Override
    public IntegerMetricValue computeValue() {
        int count = 0;
        for(OWLOntology ontology : getRootOntology().getImportsClosure()) {
            count += ontology.getAxiomCount(type);
        }
        return new IntegerMetricValue(metricName, count);
    }

    @Override
    public OWLAPIProjectMetricState getStateAfterChanges(List<? extends OWLOntologyChange> changes) {
        for(OWLOntologyChange change : changes) {
            if(change.isAxiomChange()) {
                if(change.getAxiom().isOfType(type)) {
                    return OWLAPIProjectMetricState.DIRTY;
                }
            }
        }
        return OWLAPIProjectMetricState.CLEAN;
    }
}
