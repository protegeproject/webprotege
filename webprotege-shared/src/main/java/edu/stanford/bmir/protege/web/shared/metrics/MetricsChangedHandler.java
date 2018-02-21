package edu.stanford.bmir.protege.web.shared.metrics;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public interface MetricsChangedHandler {

    void handleMetricsChanged(MetricsChangedEvent event);
}
