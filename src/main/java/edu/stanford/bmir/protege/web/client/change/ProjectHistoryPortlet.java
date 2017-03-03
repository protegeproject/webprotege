package edu.stanford.bmir.protege.web.client.change;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

@Portlet(id = "portlets.ProjectHistory",
        title = "Project History",
        tooltip = "Displays a list of all changes that have been made to the project")
public class ProjectHistoryPortlet extends AbstractWebProtegePortlet {

    public static final FilterId SHOW_DETAILS_FILTER = new FilterId("Show details");

    private final ChangeListViewPresenter presenter;

    private final PortletAction refreshAction = new PortletAction("Refresh", (action, event) -> reload());

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);


    @Inject
    public ProjectHistoryPortlet(ChangeListViewPresenter presenter, LoggedInUserProjectPermissionChecker permissionChecker, SelectionModel selectionModel, EventBus eventBus, ProjectId projectId) {
        super(selectionModel, eventBus, projectId);
        setTitle("Project History");
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        presenter.setDownloadVisible(true);
        final ChangeListView changeListView = presenter.getView();
        setWidget(changeListView.asWidget());
        addPortletAction(refreshAction);

        addProjectEventHandler(ProjectChangedEvent.TYPE, event -> handleProjectChanged(event));
        addApplicationEventHandler(ON_PERMISSIONS_CHANGED, event -> reload());

        FilterView filterView = new FilterViewImpl();
        filterView.addFilter(SHOW_DETAILS_FILTER, FilterSetting.ON);
        filterView.addValueChangeHandler(event -> changeListView.setDetailsVisible(event.getValue().hasSetting(SHOW_DETAILS_FILTER, FilterSetting.ON)));
        setFilter(filterView);
        reload();
    }

    private void handleProjectChanged(ProjectChangedEvent event) {
        if (lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        refreshAction.setEnabled(true);
        lastRevisionNumber = event.getRevisionNumber();
    }

    private void reload() {
        refreshAction.setEnabled(false);
        permissionChecker.hasPermission(VIEW_CHANGES,
                                        canViewChanges -> {
                                            ProjectId projectId = getProjectId();
                                            presenter.setRevertChangesVisible(canViewChanges);
                                            presenter.setChangesForProject(projectId);
                                        });

    }
}
