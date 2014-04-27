package edu.stanford.bmir.protege.web.shared.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class BooleanMetricValue extends MetricValue {

    public static final String TRUE_DISPLAY_TEXT = "Yes";

    public static final String FALSE_DISPLAY_TEXT = "No";

    /**
     * For serialization purposes only
     */
    private BooleanMetricValue() {
    }

    public BooleanMetricValue(String metricName, boolean value) {
        super(metricName, value ? TRUE_DISPLAY_TEXT : FALSE_DISPLAY_TEXT, false);
    }

}
