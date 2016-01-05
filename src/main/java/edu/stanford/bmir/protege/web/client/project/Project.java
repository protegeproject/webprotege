package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.permissions.ProjectPermissionManager;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.ui.LayoutManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

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

    private final LoggedInUserProvider loggedInUserProvider;

    private ProjectLayoutConfiguration projectLayoutConfiguration;

    private LayoutManager layoutManager;

    private EventPollingManager eventPollingManager;

    /**
     * Creates a project from the specified details.
     * @param projectDetails The details.  Not {@code null}.
     * @param permissionsForCurrentUser The permissions for the current logged in user.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    @Inject
    public Project(ProjectDetails projectDetails, PermissionsSet permissionsForCurrentUser, EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.projectDetails = checkNotNull(projectDetails);
        this.permissionManager = new ProjectPermissionManager(getProjectId(), eventBus, dispatchServiceManager, loggedInUserProvider);
        this.permissionManager.setUserPermissions(loggedInUserProvider.getCurrentUserId(), checkNotNull(permissionsForCurrentUser));
        this.layoutManager = new LayoutManager(projectDetails.getProjectId(), new ProjectLayoutConfiguration());
        this.loggedInUserProvider = loggedInUserProvider;
        this.eventPollingManager = EventPollingManager.get(10 * 1000, projectDetails.getProjectId(), eventBus, dispatchServiceManager, loggedInUserProvider);
        eventPollingManager.start();
    }

    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }

    public String getDisplayName() {
        return projectDetails.getDisplayName();
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectLayoutConfiguration(ProjectLayoutConfiguration projectLayoutConfiguration) {
        this.projectLayoutConfiguration = projectLayoutConfiguration;
    }

    public ProjectLayoutConfiguration getProjectLayoutConfiguration() {
        return projectLayoutConfiguration;
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }
    
    public void dispose() {
        // TODO: we might notify the session that project has been closed
        permissionManager.dispose();
        eventPollingManager.stop();
    }
}
