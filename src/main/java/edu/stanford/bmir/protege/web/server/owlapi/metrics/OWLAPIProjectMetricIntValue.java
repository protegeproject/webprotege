package edu.stanford.bmir.protege.web.server.owlapi.metrics;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectMetricIntValue extends OWLAPIProjectMetricValue {

    public OWLAPIProjectMetricIntValue(String metricName, int value) {
        super(metricName, Integer.toString(value));
    }
}
