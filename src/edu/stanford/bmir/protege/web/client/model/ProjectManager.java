package edu.stanford.bmir.protege.web.client.model;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.LoadProjectResult;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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

    public void loadProject(ProjectId projectId, final AsyncCallback<Project> projectLoadedCallback) {
        checkNotNull(projectLoadedCallback);
        Project project = map.get(checkNotNull(projectId));
        if(project != null) {
            projectLoadedCallback.onSuccess(project);
            return;
        }

        final LoadProjectAction action = new LoadProjectAction(checkNotNull(projectId));
        DispatchServiceManager.get().execute(action, new AsyncCallback<LoadProjectResult>() {
            @Override
            public void onFailure(Throwable caught) {
                projectLoadedCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(LoadProjectResult result) {
                Project project = registerProject(result.getUserId(), result.getRequestingUserProjectPermissionSet(), result.getProjectDetails());
                projectLoadedCallback.onSuccess(project);
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
