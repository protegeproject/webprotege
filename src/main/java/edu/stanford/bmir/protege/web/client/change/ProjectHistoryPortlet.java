package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

public class ProjectHistoryPortlet extends AbstractWebProtegePortlet {

    public static final FilterId SHOW_DETAILS_FILTER = new FilterId("Show details");

    private final ChangeListViewPresenter presenter;

    private final PortletAction refreshAction = new PortletAction("Refresh", (action, event) -> reload());

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);


    @Inject
    public ProjectHistoryPortlet(ChangeListViewPresenter presenter, LoggedInUserProjectPermissionChecker permissionChecker, SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
        setTitle("Project History");
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
        presenter.setDownloadVisible(true);
        final ChangeListView changeListView = presenter.getView();
        getContentHolder().setWidget(changeListView.asWidget());
        addPortletAction(refreshAction);

        addProjectEventHandler(ProjectChangedEvent.TYPE, event -> handleProjectChanged(event));
        addApplicationEventHandler(PermissionsChangedEvent.TYPE, event -> reload());

        FilterView filterView = new FilterViewImpl();
        filterView.addFilter(SHOW_DETAILS_FILTER, FilterSetting.ON);
        filterView.addValueChangeHandler(new ValueChangeHandler<FilterSet>() {
            @Override
            public void onValueChange(ValueChangeEvent<FilterSet> event) {
                changeListView.setDetailsVisible(event.getValue().hasSetting(SHOW_DETAILS_FILTER, FilterSetting.ON));
            }
        });
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
        permissionChecker.hasReadPermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                ProjectId projectId = getProjectId();
                presenter.setRevertChangesVisible(result);
                presenter.setChangesForProject(projectId);
            }
        });

    }
}
