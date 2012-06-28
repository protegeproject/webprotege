package edu.stanford.bmir.protege.web.client.model;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

/**
 * Class that holds all the global settings of the client, such as the global
 * session.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class GlobalSettings {

    private static GlobalSettings globalSettingsInstance;

    private Session globalSession = new Session(null);

    private GlobalSettings() {
    }

    public static GlobalSettings getGlobalSettings() {
        if (globalSettingsInstance == null) {
            globalSettingsInstance = new GlobalSettings();
        }
        return globalSettingsInstance;
    }

    public String getUserName() {
        return globalSession == null ? null : globalSession.getUserName();
    }

    /**
     * Gets the UserId of the logged in user.
     * @return The UserId of the logged in user.  Not <code>null</code>.
     */
    public UserId getUserId() {
        String userName = getUserName();
        return UserId.getUserId(userName);
    }

    public UserData getUser() {
        return globalSession == null ? null : globalSession.getUserData();
    }

    public Session getGlobalSession() {
        return globalSession;
    }

    public void setUser(UserData userData) {
        if (globalSession == null) {
            globalSession = new Session(userData);
        }
        globalSession.setUserData(userData);
    }


    public String getSessionProperty(String prop) {
        return (globalSession == null) ? null : globalSession.getSessionProperty(prop);
    }

    public void setSessionProperty(String prop, String value) {
        if (globalSession != null) {
            globalSession.setSessionProperty(prop, value);
        }
    }

    /**
     * Determines if the current session user is logged in.
     * @return <code>true</code> if the current session user is logged in, otherwise <code>false</code>.
     */
    public boolean isLoggedIn() {
        return getUser() != null;
    }
}
