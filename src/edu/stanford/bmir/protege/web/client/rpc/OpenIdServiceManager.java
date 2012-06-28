package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;

/**
 * @author z.khan
 */
public class OpenIdServiceManager {

    private static OpenIdServiceAsync proxy;
    static OpenIdServiceManager instance;

    private OpenIdServiceManager() {
        proxy = (OpenIdServiceAsync) GWT.create(OpenIdService.class);
    }

    public static OpenIdServiceManager getInstance() {
        if (instance == null) {
            instance = new OpenIdServiceManager();
        }
        return instance;
    }

    public void validateUserToAssocOpenIdWithEncrypt(String name, String password, AsyncCallback<UserData> cb) {
        proxy.validateUserToAssocOpenIdWithEncrypt(name, password, cb);
    }

    public void getUsersOpenId(String name, AsyncCallback<OpenIdData> cb) {
        proxy.getUsersOpenId(name, cb);
    }

    public void removeAssocToOpenId(String name, String openId, AsyncCallback<OpenIdData> cb) {
        proxy.removeAssocToOpenId(name, openId, cb);
    }

    public void assocNewOpenIdToUser(String name, AsyncCallback<OpenIdData> cb) {
        proxy.assocNewOpenIdToUser(name, cb);
    }

    public void isOpenIdInSessForAddNewOpenId(AsyncCallback<UserData> cb) {
        proxy.isOpenIdInSessForAddNewOpenId(cb);
    }

    public void checkIfOpenIdInSessionForLogin(AsyncCallback<UserData> cb) {
        proxy.checkIfOpenIdInSessionForLogin(cb);
    }

    public void clearCreateUserToAssocOpenIdSessData(AsyncCallback<Void> cb) {
        proxy.clearCreateUserToAssocOpenIdSessData(cb);
    }

    public void checkIfUserCreatedToAssocOpenId(AsyncCallback<UserData> cb) {
        proxy.checkIfUserCreatedToAssocOpenId(cb);
    }

    public void registerUserToAssocOpenIdWithEncrption(String name, String hashedPassword, String emailId,
            AsyncCallback<UserData> cb) {
        proxy.registerUserToAssocOpenIdWithEncrption(name, hashedPassword, emailId, cb);
    }

    public void checkIfUserAuthenticatedToAssocOpenId(AsyncCallback<UserData> cb) {
        proxy.checkIfUserAuthenticatedToAssocOpenId(cb);
    }

    public void clearAuthUserToAssocOpenIdSessData(AsyncCallback<Void> cb) {
        proxy.clearAuthUserToAssocOpenIdSessData(cb);
    }
}
