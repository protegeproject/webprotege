package edu.stanford.bmir.protege.web.client.metrics;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class MetricsPortlet extends AbstractWebProtegePortlet {

    private final DispatchServiceManager dispatchServiceManager;

    private MetricsPresenter metricsPresenter;

    private MetricsView view;

    @Inject
    public MetricsPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        this.dispatchServiceManager = dispatchServiceManager;
        view = new MetricsViewImpl();
        getContentHolder().setWidget(view.asWidget());
        metricsPresenter = new MetricsPresenter(getProjectId(), view, dispatchServiceManager);
        metricsPresenter.bind(this);
        updateDisplay();
    }


    private void updateDisplay() {
        setTitle("Metrics");
        metricsPresenter.reload();
    }

}
