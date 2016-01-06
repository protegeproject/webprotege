package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.UploadProjectDialogController;
import edu.stanford.bmir.protege.web.client.ui.projectlist.ProjectListViewImpl;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedHandler;

import javax.inject.Provider;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectManagerPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private ProjectManagerView projectManagerView;

    private Map<ProjectManagerViewCategory, ProjectListFilter> viewCat2Filter = new HashMap<ProjectManagerViewCategory, ProjectListFilter>();

    private ProjectListFilter selectedFilter;

    private final ProjectListFilter includeAllFilter;

    private ProjectManagerViewCategory currentViewCategory = ProjectManagerViewCategory.HOME;

    private ProjectDetailsCache projectDetailsCache = new ProjectDetailsCache();

    private final EventBus eventBus;

    public ProjectManagerPresenter(LoadProjectRequestHandler loadProjectRequestHandler, final EventBus eventBus, final DispatchServiceManager dispatchServiceManager, final LoggedInUserProvider loggedInUserProvider) {
        this.projectManagerView = new ProjectManagerViewImpl(new ProjectListViewImpl(loggedInUserProvider));
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
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
                return !projectDetails.isInTrash() && projectDetails.getOwner().equals(loggedInUserProvider.getCurrentUserId());
            }
        });

        viewCat2Filter.put(ProjectManagerViewCategory.TRASH, new ProjectListFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return projectDetails.isInTrash() && projectDetails.getOwner().equals(loggedInUserProvider.getCurrentUserId());
            }
        });


        selectedFilter = includeAllFilter;





        projectManagerView.setLoadProjectRequestHandler(loadProjectRequestHandler);

        projectManagerView.setCreateProjectRequestHandler(new CreateProjectRequestHandlerImpl(eventBus, dispatchServiceManager, loggedInUserProvider));
        projectManagerView.setUploadProjectRequestHandler(new UploadProjectRequestHandlerImpl(new Provider<UploadProjectDialogController>() {
            @Override
            public UploadProjectDialogController get() {
                return new UploadProjectDialogController(eventBus, dispatchServiceManager, loggedInUserProvider);
            }
        }));
        projectManagerView.setDownloadProjectRequestHandler(new DownloadProjectRequestHandlerImpl());
        projectManagerView.setTrashManagerRequestHandler(new TrashManagerRequestHandlerImpl(dispatchServiceManager));

        projectManagerView.setViewCategoryChangedHandler(new ViewCategoryChangedHandler() {
            @Override
            public void handleViewCategoryChanged(ProjectManagerViewCategory selectedCategory) {
                setSelectedViewCategory(selectedCategory);
            }
        });

        eventBus.addHandler(ProjectCreatedEvent.TYPE, new ProjectCreatedHandler() {
            @Override
            public void handleProjectCreated(ProjectCreatedEvent event) {
                projectDetailsCache.add(event.getProjectDetails());
                projectManagerView.addProjectData(event.getProjectDetails());
                projectManagerView.setSelectedProject(event.getProjectId());
            }
        });

        eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                handleUserChange();
                reloadFromServer();
            }
        });

        eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                handleUserChange();
                reloadFromServer();
            }
        });

        eventBus.addHandler(ProjectMovedToTrashEvent.TYPE, new ProjectMovedToTrashHandler() {
            @Override
            public void handleProjectMovedToTrash(ProjectMovedToTrashEvent event) {
                if(projectDetailsCache.setInTrash(event.getProjectId(), true)) {
                    reloadFromClientCache();
                }
            }
        });

        eventBus.addHandler(ProjectMovedFromTrashEvent.TYPE, new ProjectMovedFromTrashHandler() {
            @Override
            public void handleProjectMovedFromTrash(ProjectMovedFromTrashEvent event) {
                if(projectDetailsCache.setInTrash(event.getProjectId(), false)) {
                    reloadFromClientCache();
                }
            }
        });

        eventBus.addHandler(ProjectSettingsChangedEvent.getType(), new ProjectSettingsChangedHandler() {
            @Override
            public void handleProjectSettingsChanged(ProjectSettingsChangedEvent event) {
                reloadFromServer();
            }
        });

        handleUserChange();
        reloadFromServer();
    }

    private void setSelectedViewCategory(ProjectManagerViewCategory selectedCategory) {
        if(currentViewCategory.equals(selectedCategory)) {
            return;
        }
        currentViewCategory = selectedCategory;
        selectedFilter = viewCat2Filter.get(selectedCategory);
        if(selectedFilter == null) {
            selectedFilter = includeAllFilter;
        }
        reloadFromClientCache();
    }

    public ProjectManagerView getProjectManagerView() {
        return projectManagerView;
    }


    private void reloadFromServer() {
        reloadFromServer(Optional.<ProjectId>absent());
    }

    private void reloadFromServer(final Optional<ProjectId> selectId) {
        GetAvailableProjectsAction action = new GetAvailableProjectsAction();
        dispatchServiceManager.execute(action, new DispatchServiceCallback<GetAvailableProjectsResult>() {
            @Override
            public void handleSuccess(GetAvailableProjectsResult result) {
                projectDetailsCache.setProjectDetails(result.getDetails());
                displayProjectDetails();
                if (selectId.isPresent()) {
                    projectManagerView.setSelectedProject(selectId.get());
                }
            }
        });
    }

    private void reloadFromClientCache() {
        displayProjectDetails();
    }

    private void displayProjectDetails() {
        List<ProjectDetails> entries = Lists.newArrayList();
        for(ProjectDetails pd : projectDetailsCache.getProjectDetailsList()) {
            if (selectedFilter.isIncluded(pd)) {
                entries.add(pd);
            }
        }
        projectManagerView.setProjectListData(entries);
    }

    private void handleUserChange() {
        final boolean guest = loggedInUserProvider.getCurrentUserId().isGuest();
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
