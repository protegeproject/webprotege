package edu.stanford.bmir.protege.web.client.revisions;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
@Portlet(id = "portlets.Revisions", title = "Project Revisions")
public class RevisionsPortletPresenter extends AbstractWebProtegePortletPresenter {

    private RevisionsListViewPresenter presenter;

    @Inject
    public RevisionsPortletPresenter(SelectionModel selectionModel,
                                     EventBus eventBus,
                                     DispatchServiceManager dispatchServiceManager,
                                     ProjectId projectId) {
        super(selectionModel, projectId);
        this.presenter = new RevisionsListViewPresenter(getProjectId(),
                                                        eventBus,
                                                        new RevisionsListViewImpl(),
                                                        dispatchServiceManager);
        presenter.reload();
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(presenter.getWidget());
    }

    @Override
    public void dispose() {
        super.dispose();
        presenter.dispose();
    }
}
