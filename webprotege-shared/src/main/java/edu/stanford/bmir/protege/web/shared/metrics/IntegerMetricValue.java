package edu.stanford.bmir.protege.web.shared.metrics;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class IntegerMetricValue extends MetricValue {

    private int value;

    /**
     * For serialization purposes only
     */
    private IntegerMetricValue() {
    }

    public IntegerMetricValue(String metricName, int value) {
        super(metricName, Integer.toString(value), value == 0);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
