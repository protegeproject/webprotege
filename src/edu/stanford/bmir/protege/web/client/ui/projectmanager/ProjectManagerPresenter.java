package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectManagerPresenter {

    private ProjectManagerView projectManagerView;

    private Map<ProjectManagerViewCategory, ProjectListFilter> viewCat2Filter = new HashMap<ProjectManagerViewCategory, ProjectListFilter>();

    private ProjectListFilter selectedFilter;

    private final ProjectListFilter includeAllFilter;

    public ProjectManagerPresenter(LoadProjectRequestHandler loadProjectRequestHandler) {
        this.projectManagerView = new ProjectManagerViewImpl();

        includeAllFilter = new ProjectListFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return !projectDetails.isInTrash();
            }
        };

        viewCat2Filter.put(ProjectManagerViewCategory.HOME, includeAllFilter);

        viewCat2Filter.put(ProjectManagerViewCategory.OWNED_BY_ME, new ProjectListFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return !projectDetails.isInTrash() && projectDetails.getOwner().equals(Application.get().getUserId());
            }
        });

        viewCat2Filter.put(ProjectManagerViewCategory.TRASH, new ProjectListFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return projectDetails.isInTrash() && projectDetails.getOwner().equals(Application.get().getUserId());
            }
        });


        selectedFilter = includeAllFilter;





        projectManagerView.setLoadProjectRequestHandler(loadProjectRequestHandler);

        projectManagerView.setCreateProjectRequestHandler(new CreateProjectRequestHandlerImpl());
        projectManagerView.setUploadProjectRequestHandler(new UploadProjectRequestHandlerImpl());
        projectManagerView.setDownloadProjectRequestHandler(new DownloadProjectRequestHandlerImpl());
        projectManagerView.setTrashManagerRequestHandler(new TrashManagerRequestHandlerImpl());

        projectManagerView.setViewCategoryChangedHandler(new ViewCategoryChangedHandler() {
            @Override
            public void handleViewCategoryChanged(ProjectManagerViewCategory selectedCategory) {
                selectedFilter = viewCat2Filter.get(selectedCategory);
                if(selectedFilter == null) {
                    selectedFilter = includeAllFilter;
                }
                reload();
            }
        });

        EventBusManager.getManager().registerHandler(ProjectCreatedEvent.TYPE, new ProjectCreatedHandler() {
            @Override
            public void handleProjectCreated(ProjectCreatedEvent event) {
                reload(Optional.of(event.getProjectId()));
            }
        });

        EventBusManager.getManager().registerHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                handleUserChange();
                reload();
            }
        });

        EventBusManager.getManager().registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                handleUserChange();
                reload();
            }
        });

        EventBusManager.getManager().registerHandler(ProjectMovedToTrashEvent.TYPE, new ProjectMovedToTrashHandler() {
            @Override
            public void handleProjectMovedToTrash(ProjectMovedToTrashEvent event) {
                reload(Optional.of(event.getProjectId()));
            }
        });

        EventBusManager.getManager().registerHandler(ProjectMovedFromTrashEvent.TYPE, new ProjectMovedFromTrashHandler() {
            @Override
            public void handleProjectMovedFromTrash(ProjectMovedFromTrashEvent event) {
                reload(Optional.of(event.getProjectId()));
            }
        });
        handleUserChange();
        reload();
    }


    public ProjectManagerView getProjectManagerView() {
        return projectManagerView;
    }


    private void reload() {
        reload(Optional.<ProjectId>absent());
    }

    private void reload(final Optional<ProjectId> selectId) {
        GWT.log("Reloading project view");


        GetAvailableProjectsAction action = new GetAvailableProjectsAction();
        DispatchServiceManager.get().execute(action, new AsyncCallback<GetAvailableProjectsResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log(caught.getMessage());
                MessageBox.alert("There was a problem retrieving the list of projects from the server.  Please refresh your browser to try again.");
            }

            @Override
            public void onSuccess(GetAvailableProjectsResult result) {
                final List<ProjectDetails> projectDetails = result.getDetails();
                List<ProjectDetails> entries = new ArrayList<ProjectDetails>(projectDetails.size());
                for(ProjectDetails pd : projectDetails) {
                    if (selectedFilter.isIncluded(pd)) {
                        entries.add(pd);
                    }
                }
                projectManagerView.setProjectListData(entries);
                if(selectId.isPresent()) {
                    projectManagerView.setSelectedProject(selectId.get());
                }
            }
        });
    }

    private void handleUserChange() {
        final boolean guest = Application.get().getUserId().isGuest();
        projectManagerView.setCreateProjectEnabled(!guest);
        projectManagerView.setUploadProjectEnabled(!guest);
        if (guest) {
            projectManagerView.setViewCategories(Arrays.asList(ProjectManagerViewCategory.HOME));
        }
        else {
            projectManagerView.setViewCategories(Arrays.asList(ProjectManagerViewCategory.HOME, ProjectManagerViewCategory.OWNED_BY_ME, ProjectManagerViewCategory.TRASH));
        }
    }


}
