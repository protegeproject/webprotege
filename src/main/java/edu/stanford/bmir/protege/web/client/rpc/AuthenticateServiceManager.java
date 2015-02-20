package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * @author z.khan
 */
public class AuthenticateServiceManager {

    private static AuthenticateServiceAsync proxy;
    static AuthenticateServiceManager instance;

    private AuthenticateServiceManager() {
        proxy = GWT.create(AuthenticateService.class);
    }

    public static AuthenticateServiceManager getInstance() {
        if (instance == null) {
            instance = new AuthenticateServiceManager();
        }
        return instance;
    }

    //log in with https
    public void validateUserAndAddInSession(String name, String password, AsyncCallback<UserData> cb) {
        proxy.validateUserAndAddInSession(name, password, cb);
    }

    //change password
    public void validateUser(UserId userId, String password, AsyncCallback<UserData> cb) {
        proxy.validateUser(userId.getUserName(), password, cb);
    }

    //change password with https
    public void changePassword(UserId userId, String password, AsyncCallback<Void> cb) {
        proxy.changePassword(userId.getUserName(), password, cb);
    }

    //create new user to associate open id with https
    public void registerUserToAssociateOpenId(UserId userId, String userPassword, String emailId, AsyncCallback<UserData> cb) {
        proxy.registerUserToAssociateOpenId(userId.getUserName(), userPassword, emailId, cb);
    }

    //sign in to assocaited open id with https
    public void validateUserToAssociateOpenId(UserId userId, String password, AsyncCallback<UserData> cb) {
        proxy.validateUserToAssociateOpenId(userId.getUserName(), password, cb);
    }

    public void sendPasswordReminder(UserId userId, AsyncCallback<Void> cb) {
        proxy.sendPasswordReminder(userId.getUserName(), cb);
    }
}
