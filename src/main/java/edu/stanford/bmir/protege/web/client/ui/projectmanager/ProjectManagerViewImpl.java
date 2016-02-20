package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.client.ui.library.sidebar.SideBar;
import edu.stanford.bmir.protege.web.client.ui.library.sidebar.SideBarItem;
import edu.stanford.bmir.protege.web.client.ui.projectlist.ProjectListViewImpl;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    protected ProjectListViewImpl projectListView;

//    @UiField
//    protected SideBar<MySideBarItem> sideBar;

    @UiField
    protected Button createProjectButton;

    @UiField
    protected Button uploadProjectButton;

    @UiField
    SimplePanel loggedInUserButton;


    private Set<ProjectManagerViewCategory> viewCategories = new HashSet<ProjectManagerViewCategory>();

    private CreateProjectRequestHandler createProjectRequestHandler = new CreateProjectRequestHandler() {
        @Override
        public void handleCreateProjectRequest() {
            GWT.log("No CreateProjectRequestHandler registered");
        }
    };


    private UploadProjectRequestHandler uploadProjectRequestHandler = new UploadProjectRequestHandler() {
        @Override
        public void handleUploadProjectRequest() {
            GWT.log("No UploadProjectRequestHandler registered");
        }
    };

    @Inject
    public ProjectManagerViewImpl(ProjectListViewImpl projectListView) {
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
    public void setViewCategories(List<ProjectManagerViewCategory> viewCategories) {
        if(this.viewCategories.equals(new HashSet<ProjectManagerViewCategory>(viewCategories))) {
            return;
        }
    }

    @UiHandler("createProjectButton")
    protected void handleCreateProject(ClickEvent clickEvent) {
        createProjectRequestHandler.handleCreateProjectRequest();
    }

    @UiHandler("uploadProjectButton")
    protected void handleUploadProject(ClickEvent clickEvent) {
        uploadProjectRequestHandler.handleUploadProjectRequest();
    }

    @Override
    public void setLoadProjectRequestHandler(LoadProjectRequestHandler handler) {
        projectListView.setLoadProjectRequestHandler(checkNotNull(handler));
    }

    @Override
    public void setDownloadProjectRequestHandler(DownloadProjectRequestHandler handler) {
        projectListView.setDownloadProjectRequestHandler(handler);
    }

    @Override
    public void setTrashManagerRequestHandler(TrashManagerRequestHandler handler) {
        projectListView.setTrashManagerRequestHandler(handler);
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
    public void setProjectListData(List<ProjectDetails> data) {
        projectListView.setListData(data);
    }

    @Override
    public void addProjectData(ProjectDetails details) {
        projectListView.addListData(details);
    }

    @Override
    public void setViewCategoryChangedHandler(ViewCategoryChangedHandler handler) {
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
