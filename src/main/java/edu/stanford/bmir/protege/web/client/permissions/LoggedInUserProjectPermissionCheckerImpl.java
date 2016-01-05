package edu.stanford.bmir.protege.web.client.permissions;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/01/16
 */
public class LoggedInUserProjectPermissionCheckerImpl implements LoggedInUserProjectPermissionChecker {

    private final LoggedInUserProvider loggedInUserProvider;

    private final ActiveProjectManager activeProjectManager;

    private final ProjectPermissionManager projectPermissionManager;

    @Inject
    public LoggedInUserProjectPermissionCheckerImpl(LoggedInUserProvider loggedInUserProvider, ActiveProjectManager activeProjectManager, ProjectPermissionManager projectPermissionManager) {
        this.loggedInUserProvider = loggedInUserProvider;
        this.activeProjectManager = activeProjectManager;
        this.projectPermissionManager = projectPermissionManager;
    }

    @Override
    public boolean hasWritePermission() {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(!projectId.isPresent()) {
            return false;
        }
        UserId userId = loggedInUserProvider.getCurrentUserId();
        return projectPermissionManager.hasWritePermissionForProject(userId, projectId.get());
    }

    @Override
    public boolean hasReadPermission() {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(!projectId.isPresent()) {
            return false;
        }
        UserId userId = loggedInUserProvider.getCurrentUserId();
        return projectPermissionManager.hasReadPermissionForProject(userId, projectId.get());
    }
}
