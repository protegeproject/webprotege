package edu.stanford.bmir.protege.web.server.owlapi.metrics;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectMetricValue {

    private String metricName;
    
    private String metricValueBroswerText;

    protected OWLAPIProjectMetricValue(String metricName, String metricValueBroswerText) {
        this.metricName = metricName;
        this.metricValueBroswerText = metricValueBroswerText;
    }

    public String getMetricName() {
        return metricName;
    }
    
    public String getBrowserText() {
        return metricValueBroswerText;
    }
    
}
