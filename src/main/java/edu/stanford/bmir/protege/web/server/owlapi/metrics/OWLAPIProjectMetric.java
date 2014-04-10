package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 * <p>
 *     A base class for computing project metrics.  Metrics values should be computed on demand, in response to ontology
 *     changes.  This class manages the caching and computation of metric values.
 * </p>
 */
public abstract class OWLAPIProjectMetric {

    final private OWLAPIProject project;

    private OWLAPIProjectMetricState state = OWLAPIProjectMetricState.DIRTY;

    private OWLAPIProjectMetricValue metricValue;

    protected OWLAPIProjectMetric(OWLAPIProject project) {
        this.project = project;
    }

    public final OWLAPIProjectMetricValue getMetricValue() {
        if (state == OWLAPIProjectMetricState.DIRTY) {
            metricValue = computeValue();
        }
        return metricValue;
    }

    public OWLAPIProject getProject() {
        return project;
    }

    public OWLOntology getRootOntology() {
        return project.getRootOntology();
    }

    public final void handleChanges(List<OWLOntologyChange> changes) {
        state = getStateAfterChanges(changes);
    }

    public final OWLAPIProjectMetricState getState() {
        return state;
    }


    protected abstract OWLAPIProjectMetricValue computeValue();


    protected abstract OWLAPIProjectMetricState getStateAfterChanges(List<OWLOntologyChange> changes);

}
