package edu.stanford.bmir.protege.web.client.metrics;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class MetricsPortlet extends AbstractOWLEntityPortlet {

    private final DispatchServiceManager dispatchServiceManager;

    private MetricsPresenter metricsPresenter;

    private MetricsView view;

    @Inject
    public MetricsPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void initialize() {
        view = new MetricsViewImpl();
        add(view.asWidget());
        metricsPresenter = new MetricsPresenter(getProjectId(), view, dispatchServiceManager);
        metricsPresenter.bind(this);
        setHeight(500);
        updateDisplay();
    }

    private void updateDisplay() {
        if(metricsPresenter == null) {
            GWT.log("MetricsPresenter is not initialized");
        }
        setTitle("Metrics");
        metricsPresenter.reload();
    }

}
