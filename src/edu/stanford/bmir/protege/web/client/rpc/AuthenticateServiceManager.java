package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;

/**
 * @author z.khan
 */
public class AuthenticateServiceManager {

    private static AuthenticateServiceAsync proxy;
    static AuthenticateServiceManager instance;

    private AuthenticateServiceManager() {
        proxy = (AuthenticateServiceAsync) GWT.create(AuthenticateService.class);
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
    public void validateUser(String name, String password, AsyncCallback<UserData> cb) {
        proxy.validateUser(name, password, cb);
    }

    //change password with https
    public void changePassword(String name, String password, AsyncCallback<Void> cb) {
        proxy.changePassword(name, password, cb);
    }

    //create new user to associate open id with https
    public void registerUserToAssociateOpenId(String userName, String userPassword, String emailId, AsyncCallback<UserData> cb) {
        proxy.registerUserToAssociateOpenId(userName, userPassword, emailId, cb);
    }

    //sign in to assocaited open id with https
    public void validateUserToAssociateOpenId(String name, String password, AsyncCallback<UserData> cb) {
        proxy.validateUserToAssociateOpenId(name, password, cb);
    }

    public void sendPasswordReminder(String userName, AsyncCallback<Void> cb) {
        proxy.sendPasswordReminder(userName, cb);
    }

    //create new user via https
    public void registerUser(String name, String password, AsyncCallback<UserData> cb) {
        proxy.registerUser(name, password, cb);
    }
}
