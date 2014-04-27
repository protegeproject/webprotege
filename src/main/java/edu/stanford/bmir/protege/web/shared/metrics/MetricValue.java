package edu.stanford.bmir.protege.web.shared.metrics;

import com.google.gwt.user.client.rpc.IsSerializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public abstract class MetricValue implements IsSerializable {

    private String metricName;
    
    private String metricValueBrowserText;

    private boolean empty;

    /**
     * For serialization purposes only
     */
    protected MetricValue() {
    }

    protected MetricValue(String metricName, String metricValueBroswerText, boolean empty) {
        this.metricName = checkNotNull(metricName);
        this.metricValueBrowserText = checkNotNull(metricValueBroswerText);
        this.empty = empty;
    }

    public String getMetricName() {
        return metricName;
    }
    
    public String getBrowserText() {
        return metricValueBrowserText;
    }

    public boolean isEmpty() {
        return empty;
    }
}
