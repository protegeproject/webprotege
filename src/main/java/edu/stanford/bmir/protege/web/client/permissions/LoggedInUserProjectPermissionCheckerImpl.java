package edu.stanford.bmir.protege.web.client.permissions;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
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

    private final PermissionManager permissionManager;

    @Inject
    public LoggedInUserProjectPermissionCheckerImpl(LoggedInUserProvider loggedInUserProvider, ActiveProjectManager activeProjectManager, PermissionManager permissionManager) {
        this.loggedInUserProvider = loggedInUserProvider;
        this.activeProjectManager = activeProjectManager;
        this.permissionManager = permissionManager;
    }

    @Override
    public void hasWritePermission(DispatchServiceCallback<Boolean> callback) {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(!projectId.isPresent()) {
            callback.onSuccess(false);
            return;
        }
        UserId userId = loggedInUserProvider.getCurrentUserId();
        permissionManager.hasWritePermissionForProject(userId, projectId.get(), callback);
    }

    @Override
    public void hasReadPermission(DispatchServiceCallback<Boolean> callback) {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(!projectId.isPresent()) {
            callback.onSuccess(false);
            return;
        }
        UserId userId = loggedInUserProvider.getCurrentUserId();
        permissionManager.hasReadPermissionForProject(userId, projectId.get(), callback);
    }
}
