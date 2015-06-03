package edu.stanford.bmir.protege.web.server.metrics;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class MetricCalculatorsProvider implements Provider<List<MetricCalculator>> {

    private final OWLOntology rootOntology;

    @Inject
    public MetricCalculatorsProvider(@RootOntology OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    @Override
    public List<MetricCalculator> get() {
        return DefaultMetricsCalculators.getDefaultMetrics(rootOntology);
    }
}
