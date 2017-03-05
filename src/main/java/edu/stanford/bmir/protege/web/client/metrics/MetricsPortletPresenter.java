package edu.stanford.bmir.protege.web.client.metrics;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.metrics.MetricsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class MetricsPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private MetricsPresenter metricsPresenter;

    private MetricsView view;

    @Inject
    public MetricsPortletPresenter(SelectionModel selectionModel,
                                   DispatchServiceManager dispatchServiceManager,
                                   ProjectId projectId) {
        super(selectionModel, projectId);
        this.dispatchServiceManager = dispatchServiceManager;
        view = new MetricsViewImpl();
        metricsPresenter = new MetricsPresenter(getProjectId(), view, dispatchServiceManager);
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setViewTitle("Metrics");
        portletUi.setWidget(view.asWidget());
        eventBus.addProjectEventHandler(getProjectId(),
                                        MetricsChangedEvent.getType(),
                                        event -> metricsPresenter.handleMetricsChanged());

        metricsPresenter.start();
    }
}
