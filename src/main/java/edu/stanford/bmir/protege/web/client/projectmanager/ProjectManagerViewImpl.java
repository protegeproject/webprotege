package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.projectlist.AvailableProjectsListViewImpl;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public class ProjectManagerViewImpl extends Composite implements ProjectManagerView, RequiresResize, ProvidesResize {

    interface ProjectListViewImplBinder extends UiBinder<HTMLPanel, ProjectManagerViewImpl> {

    }

    private static ProjectListViewImplBinder ourUiBinder = GWT.create(ProjectListViewImplBinder.class);

    @UiField(provided = true)
    protected AvailableProjectsListViewImpl projectListView;

    @UiField
    protected Button createProjectButton;

    @UiField
    protected Button uploadProjectButton;

    @UiField
    SimplePanel loggedInUserButton;

    @UiField
    protected CheckBox ownedByMeCheckBox;

    @UiField
    protected CheckBox sharedWithMeCheckBox;

    @UiField
    protected CheckBox inTrashCheckBox;


    private CreateProjectRequestHandler createProjectRequestHandler = () -> {};

    private UploadProjectRequestHandler uploadProjectRequestHandler = () -> {};

    private ViewFilterChangedHandler viewFilterChangedHandler = () -> {};

    @Inject
    public ProjectManagerViewImpl(AvailableProjectsListViewImpl projectListView) {
        this.projectListView = projectListView;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        setCreateProjectEnabled(false);
        setUploadProjectEnabled(false);
    }

    @Override
    public void onResize() {
    }

    @Override
    public void setSelectedProject(ProjectId projectId) {
        projectListView.setSelectedProject(projectId);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> handler) {
        return projectListView.addSelectionHandler(handler);
    }

    @Override
    public void setCreateProjectEnabled(boolean enabled) {
        createProjectButton.setEnabled(enabled);
    }

    @Override
    public void setUploadProjectEnabled(boolean enabled) {
        uploadProjectButton.setEnabled(enabled);
    }

    @Override
    public void setViewFilters(List<ProjectManagerViewFilter> viewFilters) {
        boolean showOwnedByMe = viewFilters.contains(ProjectManagerViewFilter.OWNED_BY_ME);
        ownedByMeCheckBox.setValue(showOwnedByMe);
        boolean showSharedWithMe = viewFilters.contains(ProjectManagerViewFilter.SHARED_WITH_ME);
        sharedWithMeCheckBox.setValue(showSharedWithMe);
        boolean showInTrash = viewFilters.contains(ProjectManagerViewFilter.TRASH);
        inTrashCheckBox.setValue(showInTrash);
    }

    @Override
    public List<ProjectManagerViewFilter> getViewFilters() {
        List<ProjectManagerViewFilter> result = new ArrayList<>();
        if(ownedByMeCheckBox.getValue()) {
            result.add(ProjectManagerViewFilter.OWNED_BY_ME);
        }
        if(sharedWithMeCheckBox.getValue()) {
            result.add(ProjectManagerViewFilter.SHARED_WITH_ME);
        }
        if(inTrashCheckBox.getValue()) {
            result.add(ProjectManagerViewFilter.TRASH);
        }
        return result;
    }

    @UiHandler("createProjectButton")
    protected void handleCreateProject(ClickEvent clickEvent) {
        createProjectRequestHandler.handleCreateProjectRequest();
    }

    @UiHandler("uploadProjectButton")
    protected void handleUploadProject(ClickEvent clickEvent) {
        uploadProjectRequestHandler.handleUploadProjectRequest();
    }

    @UiHandler("sharedWithMeCheckBox")
    protected void handleSharedWithMeCheckBoxClicked(ClickEvent event) {
        viewFilterChangedHandler.handleViewFilterChanged();
    }

    @UiHandler("ownedByMeCheckBox")
    protected void handleOwnedByMeCheckBoxClicked(ClickEvent event) {
        viewFilterChangedHandler.handleViewFilterChanged();
    }

    @UiHandler("inTrashCheckBox")
    protected void handleInTrashCheckBoxClicked(ClickEvent event) {
        viewFilterChangedHandler.handleViewFilterChanged();
    }

    @Override
    public void setCreateProjectRequestHandler(CreateProjectRequestHandler handler) {
        this.createProjectRequestHandler = handler;
    }

    @Override
    public void setUploadProjectRequestHandler(UploadProjectRequestHandler handler) {
        this.uploadProjectRequestHandler = handler;
    }

    @Override
    public void setAvailableProjects(List<AvailableProject> data) {
        projectListView.setListData(data);
    }

    @Override
    public void addAvailableProject(AvailableProject details) {
        projectListView.addListData(details);
    }

    @Override
    public void setViewFilterChangedHandler(ViewFilterChangedHandler handler) {
        this.viewFilterChangedHandler = handler;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public AcceptsOneWidget getLoggedInUserButton() {
        return loggedInUserButton;
    }

}
