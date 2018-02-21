package edu.stanford.bmir.protege.web.client.metrics;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class MetricsPresenter {

    private ProjectId projectId;

    private MetricsView view;

    private DispatchServiceManager dispatchServiceManager;

    public MetricsPresenter(ProjectId projectId, MetricsView view, DispatchServiceManager dispatchServiceManager) {
        this.view = view;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;

        view.setRequestRefreshEventHandler(requestRefreshEvent -> processRequestRefresh());
    }

    public void start() {
        processRequestRefresh();
    }

    public void handleMetricsChanged() {
        view.setDirty(true);
    }

    private void processRequestRefresh() {
        dispatchServiceManager.execute(new GetMetricsAction(projectId), new DispatchServiceCallback<GetMetricsResult>() {
            @Override
            public void handleSuccess(GetMetricsResult result) {
                view.setMetrics(result.getMetricValues());
            }
        });
    }

}
