package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

public class ProjectHistoryPortlet extends AbstractOWLEntityPortlet {

    public static final String REFRESH_TO_SEE_THE_LATEST_CHANGES = "Click to see the latest changes";
    public static final String LATEST_CHANGES_VISIBLE = "Latest changes displayed";

    public static final FilterId SHOW_DETAILS_FILTER = new FilterId("Show details");

    private final ChangeListViewPresenter presenter;

    private final PortletAction refreshAction = new PortletAction("Refresh", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            onRefresh();
        }
    });

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public ProjectHistoryPortlet(ChangeListViewPresenter presenter, LoggedInUserProjectPermissionChecker permissionChecker, SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        presenter.setDownloadVisible(true);
        final ChangeListView changeListView = presenter.getView();
        addPortletAction(refreshAction);
        getContentHolder().setWidget(changeListView.asWidget());
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

        FilterView filterView = new FilterViewImpl();
        filterView.addFilter(SHOW_DETAILS_FILTER, FilterSetting.ON);
        filterView.addValueChangeHandler(new ValueChangeHandler<FilterSet>() {
            @Override
            public void onValueChange(ValueChangeEvent<FilterSet> event) {
                changeListView.setDetailsVisible(event.getValue().hasSetting(SHOW_DETAILS_FILTER, FilterSetting.ON));
            }
        });
        setFilter(filterView);
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
        refreshAction.setEnabled(false);
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                ProjectId projectId = getProjectId();
                presenter.setRevertChangesVisible(result);
                presenter.setChangesForProject(projectId);
            }
        });

    }
}
