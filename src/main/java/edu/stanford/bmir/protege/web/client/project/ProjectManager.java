package edu.stanford.bmir.protege.web.client.project;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackInvoker;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectResult;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
public class ProjectManager {

    private Map<ProjectId, Project> map = new HashMap<ProjectId, Project>();

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public ProjectManager(EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    public void loadProject(ProjectId projectId, final DispatchServiceCallback<Project> projectLoadedCallback) {
        checkNotNull(projectLoadedCallback);
        Project project = map.get(checkNotNull(projectId));
        if(project != null) {
            new DispatchServiceCallbackInvoker<>(projectLoadedCallback).onSuccess(project);
            return;
        }

        final LoadProjectAction action = new LoadProjectAction(checkNotNull(projectId));
        dispatchServiceManager.execute(action, new DispatchServiceCallback<LoadProjectResult>() {
            @Override
            public void handleSubmittedForExecution() {
                projectLoadedCallback.handleSubmittedForExecution();
            }

            @Override
            public void handleExecutionException(Throwable cause) {
                new DispatchServiceCallbackInvoker<>(projectLoadedCallback).onFailure(cause);
            }

            @Override
            public void handleSuccess(LoadProjectResult result) {
                Project project = registerProject(result.getProjectDetails());
                new DispatchServiceCallbackInvoker<>(projectLoadedCallback).onSuccess(project);
            }
        });
    }


    public void unloadProject(ProjectId projectId) {
        unregisterProject(projectId);
    }


    private Project registerProject(final ProjectDetails projectDetails) {
        final ProjectId projectId = projectDetails.getProjectId();
        if(map.containsKey(projectId)) {
            throw new RuntimeException("Double registration of project: " + projectId);
        }

        final Project project = new Project(projectDetails, eventBus, dispatchServiceManager, loggedInUserProvider);
        map.put(projectId, project);
        return project;
    }

    private void unregisterProject(ProjectId projectId) {
        Project project = map.remove(projectId);
        if(project != null) {
            project.dispose();
        }
    }


    public Optional<Project> getProject(ProjectId projectId) {
        Project project = map.get(projectId);
        if(project == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(project);
        }
    }




}
