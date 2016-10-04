package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.UploadProjectDialogController;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashHandler;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashHandler;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedHandler;

import javax.inject.Inject;
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

    private Map<ProjectManagerViewFilter, ProjectDetailsFilter> viewCat2Filter = new HashMap<ProjectManagerViewFilter, ProjectDetailsFilter>();

    private ProjectDetailsFilter currentFilter = new ProjectDetailsOrFilter(
            Collections.<ProjectDetailsFilter>emptyList());

    private ProjectDetailsCache projectDetailsCache = new ProjectDetailsCache();

    @Inject
    public ProjectManagerPresenter(final EventBus eventBus,
                                   final ProjectManagerView projectManagerView,
                                   final DispatchServiceManager dispatchServiceManager,
                                   final LoggedInUserProvider loggedInUserProvider,
                                   final LoggedInUserPresenter loggedInUserPresenter,
                                   final LoadProjectRequestHandler loadProjectRequestHandler) {
        this.projectManagerView = projectManagerView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        projectManagerView.setLoadProjectRequestHandler(loadProjectRequestHandler);

        viewCat2Filter.put(ProjectManagerViewFilter.OWNED_BY_ME, new ProjectDetailsFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return !projectDetails.isInTrash() && projectDetails.getOwner().equals(loggedInUserProvider.getCurrentUserId());
            }
        });

        viewCat2Filter.put(ProjectManagerViewFilter.SHARED_WITH_ME, new ProjectDetailsFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return !projectDetails.isInTrash() && !projectDetails.getOwner().equals(loggedInUserProvider.getCurrentUserId());
            }
        });

        viewCat2Filter.put(ProjectManagerViewFilter.TRASH, new ProjectDetailsFilter() {
            @Override
            public boolean isIncluded(ProjectDetails projectDetails) {
                return projectDetails.isInTrash() && projectDetails.getOwner().equals(loggedInUserProvider.getCurrentUserId());
            }
        });

        projectManagerView.setCreateProjectRequestHandler(new CreateProjectRequestHandlerImpl(eventBus, dispatchServiceManager, loggedInUserProvider));
        projectManagerView.setUploadProjectRequestHandler(new UploadProjectRequestHandlerImpl(new Provider<UploadProjectDialogController>() {
            @Override
            public UploadProjectDialogController get() {
                return new UploadProjectDialogController(eventBus, dispatchServiceManager, loggedInUserProvider);
            }
        }));
        projectManagerView.setDownloadProjectRequestHandler(new DownloadProjectRequestHandlerImpl());
        projectManagerView.setTrashManagerRequestHandler(new TrashManagerRequestHandlerImpl(dispatchServiceManager));

        projectManagerView.setViewFilterChangedHandler(new ViewFilterChangedHandler() {
            @Override
            public void handleViewFilterChanged() {
                applyFilters();
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

        projectManagerView.setViewFilters(
                Arrays.asList(ProjectManagerViewFilter.OWNED_BY_ME, ProjectManagerViewFilter.SHARED_WITH_ME)
        );

        handleUserChange();
        reloadFromServer();

        loggedInUserPresenter.start(projectManagerView.getLoggedInUserButton());

    }


//    public void setLoadProjectRequestHandler(LoadProjectRequestHandler loadProjectRequestHandler) {
//        projectListView.setLoadProjectRequestHandler(loadProjectRequestHandler);
//    }


    private void applyFilters() {
        List<ProjectManagerViewFilter> selectedFilters = projectManagerView.getViewFilters();
        List<ProjectDetailsFilter> filterList = new ArrayList<>();
        for(ProjectManagerViewFilter filter : selectedFilters) {
            ProjectDetailsFilter detailsFilter = viewCat2Filter.get(filter);
            filterList.add(detailsFilter);
        }
        currentFilter = new ProjectDetailsOrFilter(filterList);
        reloadFromClientCache();
    }

    public ProjectManagerView getView() {
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
                applyFilters();
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
            if (currentFilter.isIncluded(pd)) {
                entries.add(pd);
            }
        }
        projectManagerView.setProjectListData(entries);
    }

    private void handleUserChange() {
        final boolean guest = loggedInUserProvider.getCurrentUserId().isGuest();
        projectManagerView.setCreateProjectEnabled(!guest);
        projectManagerView.setUploadProjectEnabled(!guest);
        reloadFromServer();
    }


    public void start(AcceptsOneWidget container) {
        container.setWidget(projectManagerView);
    }
}
