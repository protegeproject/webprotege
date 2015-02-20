package edu.stanford.bmir.protege.web.client.metrics;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.RequestRefreshEvent;
import edu.stanford.bmir.protege.web.client.events.RequestRefreshEventHandler;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.shared.event.HasEventHandlerManagement;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsResult;
import edu.stanford.bmir.protege.web.shared.metrics.MetricsChangedEvent;
import edu.stanford.bmir.protege.web.shared.metrics.MetricsChangedHandler;
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

        view.setRequestRefreshEventHandler(new RequestRefreshEventHandler() {
            @Override
            public void handleRequestRefresh(RequestRefreshEvent requestRefreshEvent) {
                processRequestRefresh();
            }
        });
    }

    public void reload() {
        processRequestRefresh();
    }

    /**
     * Attaches this presenter to the specified event handler.
     * @param eventHandler The event handler.
     */
    public void bind(HasEventHandlerManagement eventHandler) {
        eventHandler.addProjectEventHandler(MetricsChangedEvent.getType(), new MetricsChangedHandler() {
            @Override
            public void handleMetricsChanged(MetricsChangedEvent event) {
                processMetricsChangedEvent(event);
            }
        });
    }

    private void processMetricsChangedEvent(MetricsChangedEvent event) {
        if(event.getProjectId().equals(projectId)) {
            view.setDirty(true);
        }
    }

    private void processRequestRefresh() {
        dispatchServiceManager.execute(new GetMetricsAction(projectId), new AbstractDispatchServiceCallback<GetMetricsResult>() {
            @Override
            public void handleSuccess(GetMetricsResult result) {
                view.setMetrics(result.getMetricValues());
            }
        });
    }

}
