package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
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
public abstract class MetricCalculator {

    private OWLOntology rootOntology;

    public MetricCalculator(OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    public OWLOntology getRootOntology() {
        return rootOntology;
    }

    public abstract OWLAPIProjectMetricState getStateAfterChanges(List<? extends OWLOntologyChange> changes);


    public abstract MetricValue computeValue();

}
