package edu.stanford.bmir.protege.web.client.model.listener;

import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;

public interface SystemListener {

    void onLogin(LoginEvent loginEvent);

    void onLogout(LoginEvent loginEvent);

    void onPermissionsChanged(PermissionEvent permissionEvent);
}
