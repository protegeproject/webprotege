package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetric;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricIntValue;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricState;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricValue;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectAxiomTypeCountMetric extends OWLAPIProjectMetric {

    private AxiomType<?> type;

    private String metricName;
    
    public OWLAPIProjectAxiomTypeCountMetric(OWLAPIProject project, AxiomType<?> type) {
        super(project);
        this.type = type;
        StringBuilder sb = new StringBuilder();
        String typeName = type.getName();
        for(int i = 0; i < typeName.length(); i++) {
            char ch = typeName.charAt(i);
            if(Character.isUpperCase(ch)) {
                sb.append(" ");
            }
            sb.append(ch);
        }
        metricName = sb.toString().trim() + " axiom count";
    }

    @Override
    protected OWLAPIProjectMetricValue computeValue() {
        return new OWLAPIProjectMetricIntValue(metricName, getRootOntology().getAxiomCount(type, true));
    }

    @Override
    protected OWLAPIProjectMetricState getStateAfterChanges(List<OWLOntologyChange> changes) {
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
