package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetric;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricIntValue;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricState;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricValue;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectLogicalAxiomCount extends OWLAPIProjectMetric {

    public OWLAPIProjectLogicalAxiomCount(OWLAPIProject project) {
        super(project);
    }

    @Override
    protected OWLAPIProjectMetricValue computeValue() {
        int count = 0;
        for(OWLOntology ontology : getRootOntology().getImportsClosure()) {
            count += ontology.getLogicalAxiomCount();
        }
        return new OWLAPIProjectMetricIntValue("Logical Axiom count (logical statement count)", getRootOntology().getLogicalAxiomCount());
    }

    @Override
    protected OWLAPIProjectMetricState getStateAfterChanges(List<OWLOntologyChange> changes) {
        for(OWLOntologyChange change : changes) {
            if(change.isAxiomChange()) {
                return OWLAPIProjectMetricState.DIRTY;
            }
        }
        return OWLAPIProjectMetricState.CLEAN;
    }
}
