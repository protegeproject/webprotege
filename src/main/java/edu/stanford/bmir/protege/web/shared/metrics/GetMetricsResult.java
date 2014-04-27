package edu.stanford.bmir.protege.web.shared.metrics;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class GetMetricsResult implements Result {

    private ImmutableList<MetricValue> metricValues;

    /**
     * For serialization purposes.
     */
    private GetMetricsResult() {
    }

    /**
     * Constructs a GetMetricsResult.
     * @param metricValues The values.  Not {@code null}.
     * @throws java.lang.NullPointerException if {@code metricValues} is {@code null}.
     */
    public GetMetricsResult(ImmutableList<MetricValue> metricValues) {
        this.metricValues = checkNotNull(metricValues);
    }

    /**
     * Gets the metric values.
     * @return The list of metric values.  Not {@code null}.
     */
    public ImmutableList<MetricValue> getMetricValues() {
        return metricValues;
    }
}
