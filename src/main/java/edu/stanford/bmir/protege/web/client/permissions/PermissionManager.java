package edu.stanford.bmir.protege.web.client.permissions;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserIdProjectIdKey;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the permissions for projects and users.
 */
public class PermissionManager implements HasDispose, PermissionChecker {

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private final HandlerRegistration loggedInHandler;

    private final HandlerRegistration loggedOutHandler;

    private final ActiveProjectManager activeProjectManager;

    private Map<UserIdProjectIdKey, PermissionsSet> cache = new HashMap<>();

    @Inject
    public PermissionManager(EventBus eventBus, DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
        loggedInHandler = eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                firePermissionsChanged();
            }
        });

        loggedOutHandler = eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                firePermissionsChanged();
            }
        });
        eventBus.addHandler(PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                GWT.log("[PermissionManager] Permissions changed.  Clearing the cache.");
                cache.clear();
            }
        });
    }

    private void firePermissionsChanged() {
        cache.clear();
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if(projectId.isPresent()) {
            eventBus.fireEventFromSource(new PermissionsChangedEvent(projectId.get()), projectId.get());
        }

    }

    private void hasPermission(final UserId userId, final ProjectId projectId, final Permission permission, final DispatchServiceCallback<Boolean> callback) {
        final UserIdProjectIdKey key = new UserIdProjectIdKey(userId, projectId);
        PermissionsSet cachedPermissionSet = cache.get(key);
//        if(cachedPermissionSet != null) {
//            GWT.log("[PermissionManager] Using cached value for key: " + key);
//            callback.onSuccess(cachedPermissionSet.contains(permission));
//            return;
//        }
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, userId), new DispatchServiceCallback<GetPermissionsResult>() {
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

//    /**
//     * Sets the permissions for a given user.
//     * @param userId The user. Not {@code null}.
//     * @param permissions The permissions.  Not {@code null}.
//     * @throws NullPointerException if any parameters are {@code null}.
//     */
//    private void setUserPermissions(UserId userId, PermissionsSet permissions) {
//        PermissionsSet old = user2permissionMap.put(checkNotNull(userId), checkNotNull(permissions));
//        if(old == null || !old.equals(permissions)) {
//            eventBus.fireEventFromSource(new PermissionsChangedEvent(projectId), projectId);
//        }
//    }


//    private void updateProjectPermissions() {
//        UserId signedInUser = loggedInUserProvider.getCurrentUserId();
//        updatePermissionsForUserId(signedInUser);
//        for(UserId userId : user2permissionMap.keySet()) {
//            updatePermissionsForUserId(userId);
//        }
//    }

//    private void updatePermissionsForUserId(final UserId userId) {
//        dispatchServiceManager.execute(new GetPermissionsAction(projectId, userId), new DispatchServiceCallback<GetPermissionsResult>() {
//            @Override
//            public void handleSuccess(GetPermissionsResult result) {
//                setUserPermissions(userId, result.getPermissionsSet());
//            }
//        });
//    }


    public void dispose() {
        loggedInHandler.removeHandler();
        loggedOutHandler.removeHandler();
    }

}
