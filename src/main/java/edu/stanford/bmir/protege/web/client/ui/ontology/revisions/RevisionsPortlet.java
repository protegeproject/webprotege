package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
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
public class RevisionsPortlet extends AbstractWebProtegePortlet {

    public static final int INITIAL_HEIGHT = 400;

    private RevisionsListViewPresenter presenter;

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public RevisionsPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserManager loggedInUserManager) {
        super(selectionModel, eventBus, projectId);
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.presenter = new RevisionsListViewPresenter(getProjectId(), eventBus,  new RevisionsListViewImpl(), dispatchServiceManager);
        presenter.reload();
        setWidget(presenter.getWidget());
        setTitle("Revisions");
        presenter.reload();
    }


//    @Override
    protected void onDestroy() {
        presenter.dispose();
//        super.onDestroy();
    }


}
