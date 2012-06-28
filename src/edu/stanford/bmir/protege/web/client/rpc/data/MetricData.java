package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MetricData implements Serializable {
	private String metricName;
	private String metricValue;

	public MetricData() {
	}

	public MetricData(String metricName, String metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

	public String getMetricName() {
		return metricName;
	}

	public String getMetricValue() {
		return metricValue;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public void setMetricValue(String metricValue) {
		this.metricValue = metricValue;
	}
}
