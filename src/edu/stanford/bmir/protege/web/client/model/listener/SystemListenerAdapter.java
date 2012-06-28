package edu.stanford.bmir.protege.web.client.model.listener;

import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;

public class SystemListenerAdapter implements SystemListener {

    public void onLogin(LoginEvent loginEvent) {
    }

    public void onLogout(LoginEvent loginEvent) {
    }

    public void onPermissionsChanged(PermissionEvent permissionEvent) {
    }

}
