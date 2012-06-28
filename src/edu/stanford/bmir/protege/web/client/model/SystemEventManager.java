package edu.stanford.bmir.protege.web.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListener;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;

/**
 * This is a singleton class that handles the system events such as:
 * login/logout, permissions changed, etc.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class SystemEventManager {

    private static SystemEventManager systemEventManager;

    private final Collection<SystemListener> loginListeners = new ArrayList<SystemListener>();
    private final Map<Project, Collection<SystemListener>> permissionListeners = new HashMap<Project, Collection<SystemListener>>();

    private SystemEventManager() {
    }

    public static SystemEventManager getSystemEventManager() {
        if (systemEventManager == null) {
            systemEventManager = new SystemEventManager();
        }
        return systemEventManager;
    }

    /**
     * This listener will only receive log in and log out events.
     *
     * @param listener
     */
    public void addLoginListener(SystemListener listener) {
        loginListeners.add(listener);
    }

    /**
     * This listener will only receive permission events.
     *
     * @param project
     * @param listener
     */
    public void addPermissionsListener(Project project, SystemListener listener) {
        Collection<SystemListener> listeners = permissionListeners.get(project);
        if (listeners == null) {
            listeners = new ArrayList<SystemListener>();
        }
        listeners.add(listener);
        permissionListeners.put(project, listeners);
    }

    public void addSystemListener(SystemListener listener) {
        addLoginListener(listener);
    }

    //TODO: this needs to be separated
    public void addSystemListener(Project project, SystemListener listener) {
        addLoginListener(listener);
        addPermissionsListener(project, listener);
    }

    void notifyLoginChanged(String oldName, String newName) {
        if (newName != null && oldName != null && newName.equals(oldName)) {
            return;
        }
        if (oldName != null) { // logout
            for (SystemListener listener : new ArrayList<SystemListener>(loginListeners)) {
                listener.onLogout(new LoginEvent(oldName));
            }
        }
        if (newName != null) { // login
            for (SystemListener listener : new ArrayList<SystemListener>(loginListeners)) {
                listener.onLogin(new LoginEvent(newName));
            }
        }
    }

    void notifyPermissionsChanged(Project project, String userName, Collection<String> newPermissions) {
        Collection<SystemListener> prjListeners = permissionListeners.get(project);
        if (prjListeners == null) {
            return;
        }
        for (SystemListener listener : new ArrayList<SystemListener>(prjListeners)) {
            listener.onPermissionsChanged(new PermissionEvent(project, userName, newPermissions));
        }
    }

    /**
     * It requests the permissions for this project and the global user.
     *
     * @param project
     */
    public void requestPermissions(Project project) {
        requestPermissions(project, GlobalSettings.getGlobalSettings().getUserName());
    }

    /**
     * It requests the permissions for this user and project from the server by
     * making a remote call.
     *
     * @param project
     * @param user
     */
    public void requestPermissions(Project project, String user) {
        requestPermissions(project, user, null);
    }

    /**
     * It requests the permissions for this user and project from the server by
     * making a remote call. It invokes the callback, if not null.
     *
     * @param project
     * @param user
     * @param callback
     */
    public void requestPermissions(Project project, String user, AsyncCallback<Collection<String>> callback) {
        AdminServiceManager.getInstance().getAllowedOperations(project.getProjectName(), user,
                new GetPermissionsHandler(project, user, callback));
    }

    public void dispose() {
        loginListeners.clear();
        permissionListeners.clear();
    }

    /*
     * Remote calls
     */

    class GetPermissionsHandler extends AbstractAsyncHandler<Collection<String>> {

        private final Project project;
        private final String user;
        private final AsyncCallback<Collection<String>> callback;

        public GetPermissionsHandler(Project project, String user, AsyncCallback<Collection<String>> callback) {
            this.project = project;
            this.user = user;
            this.callback = callback;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Could not retrieve permissions from server for project " + project.getProjectName(), caught);
            // TODO: should we remove at this point all permissions of the user?
            if (callback != null) {
                callback.onFailure(caught);
            }
        }

        @Override
        public void handleSuccess(Collection<String> permissions) {
            project.getProjectPermissionManager().setUserPermissions(user, permissions);
            notifyPermissionsChanged(project, user, permissions);
            if (callback != null) {
                callback.onSuccess(permissions);
            }
        }
    }
}
