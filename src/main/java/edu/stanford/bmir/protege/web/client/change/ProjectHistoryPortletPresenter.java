package edu.stanford.bmir.protege.web.client.change;

import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
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
public class ProjectHistoryPortletPresenter extends AbstractWebProtegePortletPresenter {

    public static final FilterId SHOW_DETAILS_FILTER = new FilterId("Show details");

    private final ChangeListViewPresenter presenter;

    private final PortletAction refreshAction = new PortletAction("Refresh", (action, event) -> reload());

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final FilterView filterView;

    private final ChangeListView changeListView;

    @Inject
    public ProjectHistoryPortletPresenter(ChangeListViewPresenter presenter,
                                          LoggedInUserProjectPermissionChecker permissionChecker,
                                          FilterView filterView,
                                          SelectionModel selectionModel,
                                          ProjectId projectId) {
        super(selectionModel, projectId);
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        this.filterView = filterView;
        presenter.setDownloadVisible(true);
        changeListView = presenter.getView();


        filterView.addFilter(SHOW_DETAILS_FILTER, FilterSetting.ON);
        filterView.addValueChangeHandler(event -> changeListView.setDetailsVisible(event.getValue()
                                                                                        .hasSetting(SHOW_DETAILS_FILTER,
                                                                                                    FilterSetting.ON)));
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setForbiddenMessage("You do not have permission to view changes in this project");
        portletUi.setWidget(changeListView.asWidget());
        portletUi.addPortletAction(refreshAction);
        portletUi.setToolbarVisible(true);
        portletUi.setFilterView(filterView);
        eventBus.addProjectEventHandler(getProjectId(), ProjectChangedEvent.TYPE, event -> handleProjectChanged(event));
        eventBus.addApplicationEventHandler(ON_PERMISSIONS_CHANGED, event -> reload());
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
                                            if (canViewChanges) {
                                                ProjectId projectId = getProjectId();
                                                presenter.setRevertChangesVisible(canViewChanges);
                                                presenter.setChangesForProject(projectId);
                                                setForbiddenVisible(false);
                                            }
                                            else {
                                                setForbiddenVisible(true);
                                            }
                                        });

    }
}
