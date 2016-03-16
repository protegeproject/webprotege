package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

public class ProjectHistoryPortlet extends AbstractOWLEntityPortlet {

    public static final String REFRESH_TO_SEE_THE_LATEST_CHANGES = "Click to see the latest changes";
    public static final String LATEST_CHANGES_VISIBLE = "Latest changes displayed";

    private final ChangeListViewPresenter presenter;

    private final PortletAction refreshAction = new PortletAction("Refresh", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            onRefresh();
        }
    });

    @Inject
    public ProjectHistoryPortlet(ChangeListViewPresenter presenter, SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.presenter = presenter;
        ChangeListView changeListView = presenter.getView();
        addPortletAction(refreshAction);
        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
        getContentHolder().setWidget(scrollPanel);
        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                ProjectHistoryPortlet.this.handleProjectChanged(event);
            }
        });
        addApplicationEventHandler(PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                onRefresh();
            }
        });
        onRefresh();
        setTitle("Project History");
    }

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private void handleProjectChanged(ProjectChangedEvent event) {
        if (lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        refreshAction.setEnabled(true);
        lastRevisionNumber = event.getRevisionNumber();
    }

    @Override
    protected void onRefresh() {
        ProjectId projectId = getProjectId();
        presenter.setChangesForProject(projectId);
        refreshAction.setEnabled(false);
    }
}
