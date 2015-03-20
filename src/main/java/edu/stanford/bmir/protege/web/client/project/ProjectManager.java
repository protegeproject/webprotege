package edu.stanford.bmir.protege.web.client.project;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackInvoker;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectResult;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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

    private static final ProjectManager instance = new ProjectManager();

    private Map<ProjectId, Project> map = new HashMap<ProjectId, Project>();


    public ProjectManager() {

    }

    public static ProjectManager get() {
        return instance;
    }

    public void loadProject(ProjectId projectId, final DispatchServiceCallback<Project> projectLoadedCallback) {
        checkNotNull(projectLoadedCallback);
        Project project = map.get(checkNotNull(projectId));
        if(project != null) {
            new DispatchServiceCallbackInvoker<>(projectLoadedCallback).onSuccess(project);
            return;
        }

        final LoadProjectAction action = new LoadProjectAction(checkNotNull(projectId));
        DispatchServiceManager.get().execute(action, new DispatchServiceCallback<LoadProjectResult>() {
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
                Project project = registerProject(result.getUserId(), result.getRequestingUserProjectPermissionSet(), result.getProjectDetails());
                new DispatchServiceCallbackInvoker<>(projectLoadedCallback).onSuccess(project);
            }
        });
    }


    public void unloadProject(ProjectId projectId) {
        unregisterProject(projectId);
    }


    private Project registerProject(final UserId userId, final PermissionsSet userPermissions, final ProjectDetails projectDetails) {
        final ProjectId projectId = projectDetails.getProjectId();
        if(map.containsKey(projectId)) {
            throw new RuntimeException("Double registration of project: " + projectId);
        }

        final Project project = new Project(projectDetails, userPermissions);
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
