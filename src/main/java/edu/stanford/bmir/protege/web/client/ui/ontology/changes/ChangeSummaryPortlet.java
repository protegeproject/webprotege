package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.project.Project;

import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

public class ChangeSummaryPortlet extends AbstractOWLEntityPortlet {

    public static final String REFRESH_TO_SEE_THE_LATEST_CHANGES = "Click to see the latest changes";
    public static final String LATEST_CHANGES_VISIBLE = "Latest changes displayed";

    private ToolbarButton refreshButton;

    private final ChangeListViewPresenter presenter;

    @Inject
    public ChangeSummaryPortlet(ChangeListViewPresenter presenter, SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.presenter = presenter;
        ChangeListView changeListView = presenter.getView();
        refreshButton = new ToolbarButton(REFRESH_TO_SEE_THE_LATEST_CHANGES);
        refreshButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onRefresh();
            }
        });
        setTopToolbar(refreshButton);
        setHeight(200);
        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
        scrollPanel.setWidth("100%");
        scrollPanel.setHeight("100%");
        add(scrollPanel);
        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                ChangeSummaryPortlet.this.handleProjectChanged(event);
            }
        });
        addApplicationEventHandler(PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                onRefresh();
            }
        });
        onRefresh();

    }

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private void handleProjectChanged(ProjectChangedEvent event) {
        if (lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        refreshButton.setDisabled(false);
        refreshButton.setText("Current revision is " + event.getRevisionNumber().getValue() + ". " + REFRESH_TO_SEE_THE_LATEST_CHANGES);
        lastRevisionNumber = event.getRevisionNumber();
    }

    @Override
    protected void onRefresh() {
        ProjectId projectId = getProjectId();
        presenter.setChangesForProject(projectId);
        setTitle("Changes for project");
        refreshButton.setDisabled(true);
        refreshButton.setText(LATEST_CHANGES_VISIBLE);
    }
}
