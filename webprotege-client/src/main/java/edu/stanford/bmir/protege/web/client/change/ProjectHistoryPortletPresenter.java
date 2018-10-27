package edu.stanford.bmir.protege.web.client.change;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

@Portlet(id = "portlets.ProjectHistory",
         title = "Project History",
         tooltip = "Displays a list of all changes that have been made to the project")
public class ProjectHistoryPortletPresenter extends AbstractWebProtegePortletPresenter {

    public static final FilterId SHOW_DETAILS_FILTER = new FilterId("Show details");

    private final ChangeListPresenter presenter;

    private final UIAction refreshAction;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final FilterView filterView;

    private final Messages messages;

    private final ChangeListView changeListView;

    @Inject
    public ProjectHistoryPortletPresenter(ChangeListPresenter presenter,
                                          LoggedInUserProjectPermissionChecker permissionChecker,
                                          FilterView filterView,
                                          SelectionModel selectionModel,
                                          ProjectId projectId,
                                          Messages messages, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        this.filterView = filterView;
        this.messages = messages;
        presenter.setDownloadVisible(true);
        changeListView = presenter.getView();


        filterView.addFilter(SHOW_DETAILS_FILTER, FilterSetting.ON);
        filterView.addValueChangeHandler(event -> changeListView.setDetailsVisible(event.getValue()
                                                                                        .hasSetting(SHOW_DETAILS_FILTER,
                                                                                                    FilterSetting.ON)));
        refreshAction = new PortletAction(messages.refresh(), "wp-btn-g--refresh", () -> reload());
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setForbiddenMessage(messages.change_permissionDenied());
        portletUi.setWidget(changeListView.asWidget());
        portletUi.addAction(refreshAction);
        portletUi.setToolbarVisible(true);
        portletUi.setFilterView(filterView);
        presenter.setHasBusy(portletUi);
        eventBus.addProjectEventHandler(getProjectId(), ProjectChangedEvent.TYPE, this::handleProjectChanged);
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
                                                presenter.setRevertChangesVisible(true);
                                                presenter.displayChangesForProject();
                                                setForbiddenVisible(false);
                                            }
                                            else {
                                                setForbiddenVisible(true);
                                            }
                                        });

    }
}
