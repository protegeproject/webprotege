package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.sidebar.SideBar;
import edu.stanford.bmir.protege.web.client.ui.library.sidebar.SideBarItem;
import edu.stanford.bmir.protege.web.client.ui.projectlist.ProjectListView;
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
public class ProjectManagerViewImpl extends Composite implements ProjectManagerView {

    interface DashboardUiBinder extends UiBinder<HTMLPanel, ProjectManagerViewImpl> {

    }

    private static DashboardUiBinder ourUiBinder = GWT.create(DashboardUiBinder.class);

    @UiField
    protected ProjectListView projectListView;

    @UiField
    protected SideBar<MySideBarItem> sideBar;

    @UiField
    protected Button createProjectButton;

    @UiField
    protected Button uploadProjectButton;

    @UiField HTMLPanel noticesPanel;

    @UiField Button closeNotices;


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

    private ViewCategoryChangedHandler viewCategoryChangedHandler = new ViewCategoryChangedHandler() {
        @Override
        public void handleViewCategoryChanged(ProjectManagerViewCategory selectedCategory) {
            GWT.log("No ViewCategoryChangedHandler registered");
        }
    };


    public ProjectManagerViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        sideBar.addSelectionHandler(new SelectionHandler<MySideBarItem>() {
            @Override
            public void onSelection(SelectionEvent<MySideBarItem> event) {
                handleSideBarSelection(event);
            }
        });
        setCreateProjectEnabled(false);
        setUploadProjectEnabled(false);
    }


    @Override
    public void setSelectedProject(ProjectId projectId) {
        projectListView.setSelectedProject(projectId);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> handler) {
        return projectListView.addSelectionHandler(handler);
    }

    protected void handleSideBarSelection(SelectionEvent<MySideBarItem> event) {
        viewCategoryChangedHandler.handleViewCategoryChanged(new ProjectManagerViewCategory(event.getSelectedItem().getLabel()));
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
        sideBar.clearItems();
        for(ProjectManagerViewCategory viewCategory : viewCategories) {
            sideBar.addItem(new MySideBarItem(viewCategory));
        }
        sideBar.setSelectedItem(0);
    }

    @UiHandler("createProjectButton")
    protected void handleCreateProject(ClickEvent clickEvent) {
        createProjectRequestHandler.handleCreateProjectRequest();
    }

    @UiHandler("uploadProjectButton")
    protected void handleUploadProject(ClickEvent clickEvent) {
        uploadProjectRequestHandler.handleUploadProjectRequest();
    }

    @UiHandler("closeNotices")
    protected void handleCloseNoticesClicked(ClickEvent event) {
        noticesPanel.setVisible(false);
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
        this.viewCategoryChangedHandler = handler;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class MySideBarItem implements SideBarItem {

        private ProjectManagerViewCategory viewCategory;


        private MySideBarItem(ProjectManagerViewCategory viewCategory) {
            this.viewCategory = viewCategory;
        }

        @Override
        public String getLabel() {
            return viewCategory.getLabel();
        }

        @Override
        public int hashCode() {
            return "Dashboard$MySideBarItem".hashCode() + viewCategory.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof MySideBarItem)) {
                return false;
            }
            MySideBarItem other = (MySideBarItem) obj;
            return this.viewCategory.equals(other.viewCategory);
        }

    }
}
