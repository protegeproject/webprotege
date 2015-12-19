package edu.stanford.bmir.protege.web.client.permissions;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Handles the permissions of a project.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Matthew Horridge
 *
 */
public class ProjectPermissionManager implements HasDispose {

    private final ProjectId projectId;

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    private Map<UserId, PermissionsSet> user2permissionMap = new HashMap<UserId, PermissionsSet>();

    private HandlerRegistration loggedInHandler;

    private HandlerRegistration loggedOutHandler;

    public ProjectPermissionManager(ProjectId projectId, EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        loggedInHandler = eventBus.addHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateProjectPermissions();
            }
        });

        loggedOutHandler = eventBus.addHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                user2permissionMap.remove(event.getUserId());
                updateProjectPermissions();
            }
        });
    }


    public ProjectId getProjectId() {
        return projectId;
    }


    public PermissionsSet getPermissionsSet(UserId userId) {
        PermissionsSet result = user2permissionMap.get(checkNotNull(userId));
        if(result == null) {
            return PermissionsSet.emptySet();
        }
        else {
            return result;
        }
    }


    public boolean hasPermission(UserId userId, Permission permission) {
        PermissionsSet set = getPermissionsSet(userId);
        return set.contains(permission);
    }

    /**
     * Sets the permissions for a given user.
     * @param userId The user. Not {@code null}.
     * @param permissions The permissions.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public void setUserPermissions(UserId userId, PermissionsSet permissions) {
        PermissionsSet old = user2permissionMap.put(checkNotNull(userId), checkNotNull(permissions));
        if(old == null || old.equals(permissions)) {
            eventBus.fireEventFromSource(new PermissionsChangedEvent(projectId), projectId);
        }
    }


    private void updateProjectPermissions() {
        UserId signedInUser = Application.get().getUserId();
        updatePermissionsForUserId(signedInUser);
        for(UserId userId : user2permissionMap.keySet()) {
            updatePermissionsForUserId(userId);
        }
    }

    private void updatePermissionsForUserId(final UserId userId) {
        dispatchServiceManager.execute(new GetPermissionsAction(projectId, userId), new DispatchServiceCallback<GetPermissionsResult>() {
            @Override
            public void handleSuccess(GetPermissionsResult result) {
                setUserPermissions(userId, result.getPermissionsSet());
            }
        });
    }


    public void dispose() {
        loggedInHandler.removeHandler();
        loggedOutHandler.removeHandler();
        user2permissionMap.clear();

    }

}
