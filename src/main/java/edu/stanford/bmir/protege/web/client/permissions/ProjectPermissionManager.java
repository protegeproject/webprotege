package edu.stanford.bmir.protege.web.client.permissions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
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

    private ProjectId projectId;

    private Map<UserId, PermissionsSet> user2permissionMap = new HashMap<UserId, PermissionsSet>();

    private HandlerRegistration loggedInHandler;

    private HandlerRegistration loggedOutHandler;

    public ProjectPermissionManager(ProjectId projectId) {
        this.projectId = projectId;

        loggedInHandler = EventBusManager.getManager().registerHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateProjectPermissions();
            }
        });

        loggedOutHandler = EventBusManager.getManager().registerHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
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
            EventBusManager.getManager().postEvent(new PermissionsChangedEvent(projectId));
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
        AdminServiceManager.getInstance().getAllowedOperations(projectId, userId, new AsyncCallback<PermissionsSet>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Failure getting permissions for logged in user");
            }

            @Override
            public void onSuccess(PermissionsSet result) {
                setUserPermissions(userId, result);
            }
        });
    }


    public void dispose() {
        loggedInHandler.removeHandler();
        loggedOutHandler.removeHandler();
        user2permissionMap.clear();

    }

}
