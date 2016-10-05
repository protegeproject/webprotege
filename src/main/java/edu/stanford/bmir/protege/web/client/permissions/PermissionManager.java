package edu.stanford.bmir.protege.web.client.permissions;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserIdProjectIdKey;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the permissions for projects and users.
 * @author Matthew Horridge
 */
@Singleton
public class PermissionManager implements HasDispose, PermissionChecker {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final HandlerRegistration loggedInHandler;

    private final HandlerRegistration loggedOutHandler;

    private final ActiveProjectManager activeProjectManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final Map<UserIdProjectIdKey, PermissionsSet> cache = new HashMap<>();

    @Inject
    public PermissionManager(EventBus eventBus, DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager, LoggedInUserProvider loggedInUserProvider) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
        this.loggedInUserProvider = loggedInUserProvider;
        loggedInHandler = eventBus.addHandler(UserLoggedInEvent.TYPE, event -> firePermissionsChanged());
        loggedOutHandler = eventBus.addHandler(UserLoggedOutEvent.TYPE, event -> firePermissionsChanged());
    }

    /**
     * Fires a {@link edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent} for the
     * current project on the event bus.
     */
    public void firePermissionsChanged() {
        cache.clear();
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        final Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if (!projectId.isPresent()) {
            return;
        }
        dispatchServiceManager.execute(new GetPermissionsAction(projectId.get(), userId), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSuccess(GetPermissionsResult getPermissionsResult) {
                cache.put(new UserIdProjectIdKey(userId, projectId.get()), getPermissionsResult.getPermissionsSet());
                GWT.log("[PermissionManager] Firing permissions changed for project: " + projectId);
                eventBus.fireEventFromSource(new PermissionsChangedEvent(projectId.get()).asGWTEvent(), projectId.get());
            }
        });

    }

    private void hasPermission(final UserId userId, final ProjectId projectId, final Permission permission, final DispatchServiceCallback<Boolean> callback) {
        final UserIdProjectIdKey key = new UserIdProjectIdKey(userId, projectId);
        PermissionsSet cachedPermissionSet = cache.get(key);
        if(cachedPermissionSet != null) {
            GWT.log("[PermissionManager] Using cached value for key: " + key);
            callback.onSuccess(cachedPermissionSet.contains(permission));
            return;
        }
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, userId), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSubmittedForExecution() {
                GWT.log("[PermissionManager] Retrieving permissions from the server for key: " + key);
            }

            @Override
            public void handleSuccess(GetPermissionsResult result) {
                cache.put(key, result.getPermissionsSet());
                boolean hasPermission = result.getPermissionsSet().contains(permission);
                callback.onSuccess(hasPermission);
            }

            @Override
            public void handleErrorFinally(Throwable throwable) {
                callback.handleErrorFinally(throwable);
            }
        });
    }

    @Override
    public void hasWritePermissionForProject(UserId userId, ProjectId projectId, DispatchServiceCallback<Boolean> callback) {
        hasPermission(userId, projectId, Permission.getWritePermission(), callback);
    }

    @Override
    public void hasReadPermissionForProject(UserId userId, ProjectId projectId, DispatchServiceCallback<Boolean> callback) {
        hasPermission(userId, projectId, Permission.getReadPermission(), callback);
    }

    @Override
    public void hasCommentPermissionForProject(UserId userId, ProjectId projectId, DispatchServiceCallback<Boolean> callback) {
        hasPermission(userId, projectId, Permission.getCommentPermission(), callback);
    }

    public void dispose() {
        loggedInHandler.removeHandler();
        loggedOutHandler.removeHandler();
    }

}
