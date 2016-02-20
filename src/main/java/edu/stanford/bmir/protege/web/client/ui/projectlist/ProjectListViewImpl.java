package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/10/2013
 */
public class ProjectListViewImpl extends Composite implements ProjectListView {

    private final LoggedInUserProvider loggedInUserProvider;

    private final List<ProjectDetailsPresenter> entries = new ArrayList<>();

    interface ProjectListViewUIImplUiBinder extends UiBinder<HTMLPanel, ProjectListViewImpl> {

    }

    private static ProjectListViewUIImplUiBinder ourUiBinder = GWT.create(ProjectListViewUIImplUiBinder.class);



    @UiField
    protected FlowPanel itemContainer;

    private DownloadProjectRequestHandler downloadProjectRequestHandler = new DownloadProjectRequestHandler() {
        @Override
        public void handleProjectDownloadRequest(ProjectId projectId) {
            GWT.log("handleProjectDownloadRequest:  No handler registered.");
        }
    };

    private LoadProjectRequestHandler loadProjectRequestHandler = new LoadProjectRequestHandler() {
        @Override
        public void handleProjectLoadRequest(ProjectId projectId) {
            GWT.log("handleProjectLoadRequest: No handler registered.");
        }
    };

    private TrashManagerRequestHandler trashManagerRequestHandler = new TrashManagerRequestHandler() {
        @Override
        public void handleMoveProjectToTrash(ProjectId projectId) {
            GWT.log("handleMoveProjectToTrash: No handler registered.");
        }

        @Override
        public void handleRemoveProjectFromTrash(ProjectId projectId) {
            GWT.log("handleRemoveProjectFromTrash: No handler registered.");
        }
    };





    @Inject
    public ProjectListViewImpl(LoggedInUserProvider loggedInUserProvider) {
        this.loggedInUserProvider = loggedInUserProvider;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setDownloadProjectRequestHandler(DownloadProjectRequestHandler handler) {
        this.downloadProjectRequestHandler = checkNotNull(handler);
    }

    @Override
    public void setLoadProjectRequestHandler(LoadProjectRequestHandler handler) {
        this.loadProjectRequestHandler = checkNotNull(handler);
    }

    @Override
    public void setTrashManagerRequestHandler(TrashManagerRequestHandler handler) {
        this.trashManagerRequestHandler = checkNotNull(handler);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    @Override
    public void setSelectedProject(ProjectId projectId) {
    }

    @Override
    public void setListData(List<ProjectDetails> projectDetails) {
        itemContainer.clear();
        entries.clear();
        for(final ProjectDetails details : projectDetails) {
            ProjectDetailsPresenter itemPresenter = getProjectDetailsPresenter(details);
            entries.add(itemPresenter);
            itemContainer.add(itemPresenter.getView());
        }
    }

    private ProjectDetailsPresenter getProjectDetailsPresenter(ProjectDetails details) {
        return new ProjectDetailsPresenter(
                        details, new ProjectDetailsViewImpl(), trashManagerRequestHandler, loadProjectRequestHandler, downloadProjectRequestHandler);
    }

    @Override
    public void addListData(ProjectDetails details) {
        ProjectDetailsPresenter itemPresenter = getProjectDetailsPresenter(details);
        itemContainer.insert(itemPresenter.getView(), 0);
        entries.add(0, itemPresenter);
    }

}
