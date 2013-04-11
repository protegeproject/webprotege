package edu.stanford.bmir.protege.web.client.model;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.permissions.ProjectPermissionManager;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.ui.LayoutManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A project on the client side. A project has information about:
 * <ul>
 * <li>project data (owner, description, etc.)</li>
 * <li>ontology event manager</li>
 * <li>project configuration (layout, portlets configuration, etc.)</li>
 * </ul>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class Project implements HasProjectId, HasDispose {

    private final ProjectDetails projectDetails;

    private final ProjectPermissionManager permissionManager;

    private ProjectLayoutConfiguration projectLayoutConfiguration;

    private LayoutManager layoutManager;

    private EventPollingManager eventPollingManager;


    /**
     * Creates a project from the specified details.
     * @param projectDetails The details.  Not {@code null}.
     * @param permissionsForCurrentUser The permissions for the current logged in user.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public Project(ProjectDetails projectDetails, PermissionsSet permissionsForCurrentUser) {
        this.projectDetails = checkNotNull(projectDetails);
        this.permissionManager = new ProjectPermissionManager(getProjectId());
        this.permissionManager.setUserPermissions(Application.get().getUserId(), checkNotNull(permissionsForCurrentUser));
        this.layoutManager = new LayoutManager(this);


        this.eventPollingManager = EventPollingManager.get(10 * 1000, projectDetails.getProjectId());
        eventPollingManager.start();
    }

    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }

//    public String getProjectName() {
//        return projectDetails.getDisplayName();
//    }

    public String getDisplayName() {
        return projectDetails.getDisplayName();
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void forceGetEvents() {
//        eventPollingManager.pollForProjectEvents();
    }


    public void setProjectLayoutConfiguration(ProjectLayoutConfiguration projectLayoutConfiguration) {
        this.projectLayoutConfiguration = projectLayoutConfiguration;
    }

    public ProjectLayoutConfiguration getProjectLayoutConfiguration() {
        return projectLayoutConfiguration;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

//    public ProjectPermissionManager getProjectPermissionManager() {
//        return permissionManager;
//    }

    public boolean hasWritePermission(UserId userId) {
        return permissionManager.hasPermission(userId, Permission.getWritePermission());
    }

    public boolean hasWritePermission() {
        return hasWritePermission(Application.get().getUserId());
    }

    public void dispose() {
        // TODO: we might notify the session that project has been closed
        permissionManager.dispose();
        eventPollingManager.stop();
    }
}
