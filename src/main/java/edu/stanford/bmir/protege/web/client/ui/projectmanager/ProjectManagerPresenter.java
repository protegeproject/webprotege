package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.UploadProjectDialogController;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_EMPTY_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
@Singleton
public class ProjectManagerPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserManager loggedInUserManager;

    private final ProjectManagerView projectManagerView;

    private final Map<ProjectManagerViewFilter, ProjectDetailsFilter> viewCat2Filter = new HashMap<>();

    private ProjectDetailsFilter currentFilter = new ProjectDetailsOrFilter(Collections.emptyList());

    private ProjectDetailsCache projectDetailsCache = new ProjectDetailsCache();

    @Inject
    public ProjectManagerPresenter(final EventBus eventBus,
                                   final ProjectManagerView projectManagerView,
                                   final DispatchServiceManager dispatchServiceManager,
                                   final LoggedInUserManager loggedInUserManager,
                                   final LoggedInUserPresenter loggedInUserPresenter) {
        this.projectManagerView = projectManagerView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserManager = loggedInUserManager;

        viewCat2Filter.put(ProjectManagerViewFilter.OWNED_BY_ME,
                           projectDetails -> !projectDetails.isInTrash() && projectDetails.getOwner().equals(loggedInUserManager.getCurrentUserId()));

        viewCat2Filter.put(ProjectManagerViewFilter.SHARED_WITH_ME,
                           projectDetails -> !projectDetails.isInTrash() && !projectDetails.getOwner().equals(loggedInUserManager.getCurrentUserId()));

        viewCat2Filter.put(ProjectManagerViewFilter.TRASH,
                           projectDetails -> projectDetails.isInTrash() && projectDetails.getOwner().equals(loggedInUserManager.getCurrentUserId()));

        projectManagerView.setCreateProjectRequestHandler(new CreateProjectRequestHandlerImpl(eventBus, dispatchServiceManager,
                                                                                              loggedInUserManager));
        projectManagerView.setUploadProjectRequestHandler(new UploadProjectRequestHandlerImpl(() -> new UploadProjectDialogController(eventBus, dispatchServiceManager, loggedInUserManager)));

        projectManagerView.setViewFilterChangedHandler(() -> applyFilters());

        eventBus.addHandler(ProjectCreatedEvent.TYPE, event -> {
            projectDetailsCache.add(event.getProjectDetails());
            projectManagerView.addProjectData(event.getProjectDetails());
            projectManagerView.setSelectedProject(event.getProjectId());
        });

        eventBus.addHandler(UserLoggedInEvent.TYPE, event -> {
            handleUserChange();
            reloadFromServer();
        });

        eventBus.addHandler(UserLoggedOutEvent.TYPE, event -> {
            handleUserChange();
            reloadFromServer();
        });

        eventBus.addHandler(ProjectMovedToTrashEvent.TYPE, event -> {
            if(projectDetailsCache.setInTrash(event.getProjectId(), true)) {
                reloadFromClientCache();
            }
        });

        eventBus.addHandler(ProjectMovedFromTrashEvent.TYPE, event -> {
            if(projectDetailsCache.setInTrash(event.getProjectId(), false)) {
                reloadFromClientCache();
            }
        });

        eventBus.addHandler(ProjectSettingsChangedEvent.getType(), event -> reloadFromServer());

        projectManagerView.setViewFilters(
                Arrays.asList(ProjectManagerViewFilter.OWNED_BY_ME, ProjectManagerViewFilter.SHARED_WITH_ME)
        );

        handleUserChange();
        reloadFromServer();

        loggedInUserPresenter.start(projectManagerView.getLoggedInUserButton());

    }

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
        dispatchServiceManager.execute(action, result -> {
            projectDetailsCache.setProjectDetails(result.getDetails());
            applyFilters();
            if (selectId.isPresent()) {
                projectManagerView.setSelectedProject(selectId.get());
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
        projectManagerView.setCreateProjectEnabled(loggedInUserManager.isAllowedApplicationAction(CREATE_EMPTY_PROJECT));
        projectManagerView.setUploadProjectEnabled(loggedInUserManager.isAllowedApplicationAction(UPLOAD_PROJECT));
        reloadFromServer();
    }


    public void start(AcceptsOneWidget container) {
        container.setWidget(projectManagerView);
    }
}
