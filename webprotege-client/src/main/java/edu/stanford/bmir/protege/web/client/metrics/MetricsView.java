package edu.stanford.bmir.protege.web.client.metrics;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.events.HasRequestRefreshEventHandler;
import edu.stanford.bmir.protege.web.shared.HasDirty;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public interface MetricsView extends IsWidget, HasDirty, HasRequestRefreshEventHandler {

    void setDirty(boolean dirty);

    void setMetrics(List<MetricValue> metrics);
}
