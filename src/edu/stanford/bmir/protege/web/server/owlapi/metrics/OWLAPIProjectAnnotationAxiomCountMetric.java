package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
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
public class OWLAPIProjectAnnotationAxiomCountMetric extends OWLAPIProjectMetric {

    public OWLAPIProjectAnnotationAxiomCountMetric(OWLAPIProject project) {
        super(project);
    }

    @Override
    protected OWLAPIProjectMetricValue computeValue() {
        int count = 0;
        for(OWLOntology ontology : getRootOntology().getImportsClosure()) {
            count += (ontology.getAxiomCount() - ontology.getLogicalAxiomCount());
        }
        return new OWLAPIProjectMetricIntValue("Annotation axiom count (annotation statement count)", count);
    }

    @Override
    protected OWLAPIProjectMetricState getStateAfterChanges(List<OWLOntologyChange> changes) {
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
