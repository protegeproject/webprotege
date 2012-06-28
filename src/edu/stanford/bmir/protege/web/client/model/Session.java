package edu.stanford.bmir.protege.web.client.model;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;


public class Session {

    private UserData userData;
    //flushed when userData changes
    private Map<String, String> sessionPropertiesMap = new HashMap<String, String>();

    public Session() {
        this(null);
    }

    public Session(UserData userData) {
        this.userData = userData;
    }

    public String getUserName() {
        return userData == null ? null : userData.getName();
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        UserData oldUserData = this.userData;
        this.userData = userData;

        sessionPropertiesMap.clear();

        SystemEventManager.getSystemEventManager().notifyLoginChanged(oldUserData == null ? null : oldUserData.getName(), userData == null ? null : userData.getName());
    }

    public String getSessionProperty(String property) {
        return sessionPropertiesMap.get(property);
    }

    public void setSessionProperty(String property, String value) {
        sessionPropertiesMap.put(property, value);
    }

    public void setUserName(String userName) {
            setUserData(new UserData(userName));


    }

}
