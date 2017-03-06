package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.project.UploadProjectDialogController;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
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

    private final Map<ProjectManagerViewFilter, AvailableProjectFilter> viewCat2Filter = new HashMap<>();

    private AvailableProjectFilter currentFilter = new AvailableProjectOrFilter(Collections.emptyList());

    private AvailableProjectsCache availableProjectsCache = new AvailableProjectsCache();

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
                           p -> !p.getProjectDetails().isInTrash() && p.getProjectDetails().getOwner().equals(loggedInUserManager.getCurrentUserId()));

        viewCat2Filter.put(ProjectManagerViewFilter.SHARED_WITH_ME,
                           projectDetails -> !projectDetails.getProjectDetails().isInTrash() && !projectDetails.getProjectDetails().getOwner().equals(loggedInUserManager.getCurrentUserId()));

        viewCat2Filter.put(ProjectManagerViewFilter.TRASH,
                           projectDetails -> projectDetails.getProjectDetails().isInTrash() && projectDetails.getProjectDetails().getOwner().equals(loggedInUserManager.getCurrentUserId()));

        projectManagerView.setCreateProjectRequestHandler(new CreateProjectRequestHandlerImpl(eventBus, dispatchServiceManager,
                                                                                              loggedInUserManager));
        projectManagerView.setUploadProjectRequestHandler(new UploadProjectRequestHandlerImpl(() -> new UploadProjectDialogController(eventBus, dispatchServiceManager, loggedInUserManager)));

        projectManagerView.setViewFilterChangedHandler(() -> applyFilters());

        eventBus.addHandler(ProjectCreatedEvent.TYPE, event -> {
            availableProjectsCache.add(new AvailableProject(event.getProjectDetails(), true, true));
            projectManagerView.addAvailableProject(new AvailableProject(event.getProjectDetails(), true, true));
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
            if(availableProjectsCache.setInTrash(event.getProjectId(), true)) {
                reloadFromClientCache();
            }
        });

        eventBus.addHandler(ProjectMovedFromTrashEvent.TYPE, event -> {
            if(availableProjectsCache.setInTrash(event.getProjectId(), false)) {
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
        List<AvailableProjectFilter> filterList = new ArrayList<>();
        for(ProjectManagerViewFilter filter : selectedFilters) {
            AvailableProjectFilter detailsFilter = viewCat2Filter.get(filter);
            filterList.add(detailsFilter);
        }
        currentFilter = new AvailableProjectOrFilter(filterList);
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
            availableProjectsCache.setAvailableProjects(result.getDetails());
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
        List<AvailableProject> entries = Lists.newArrayList();
        for(AvailableProject pd : availableProjectsCache.getAvailableProjectsList()) {
            if (currentFilter.isIncluded(pd)) {
                entries.add(pd);
            }
        }
        projectManagerView.setAvailableProjects(entries);
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
