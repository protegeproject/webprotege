package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserNameAlreadyExistsException;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AdminServiceManager {

    private static AdminServiceAsync proxy;
    static AdminServiceManager instance;

    private AdminServiceManager() {
        proxy = (AdminServiceAsync) GWT.create(AdminService.class);
    }

    public static AdminServiceManager getInstance() {
        if (instance == null) {
            instance = new AdminServiceManager();
        }
        return instance;
    }

    public void getUserEmail(String userName, AsyncCallback<String> callback) {
        proxy.getUserEmail(userName, callback);
    }

    public void setUserEmail(String userName, String email, AsyncCallback<Void> callback) {
        proxy.setUserEmail(userName, email, callback);
    }

    public void getProjects(String user, AsyncCallback<Collection<ProjectData>> cb) {
        proxy.getProjects(user, cb);
    }

    public void getAllowedOperations(String project, String user, AsyncCallback<Collection<String>> cb) {
        proxy.getAllowedOperations(project, user, cb);
    }

    public void getAllowedServerOperations(String userName, AsyncCallback<Collection<String>> cb) {
        proxy.getAllowedServerOperations(userName, cb);
    }

    public void changePassword(String name, String password, AsyncCallback<Void> cb) {
        proxy.changePassword(name, password, cb);
    }

    public void refreshMetaproject(AsyncCallback<Void> cb) {
        proxy.refreshMetaproject(cb);
    }

    public void sendPasswordReminder(String userName, AsyncCallback<Void> cb) {
        proxy.sendPasswordReminder(userName, cb);
    }

    public void getUserSaltAndChallenge(String userName, AsyncCallback<LoginChallengeData> cb) {
        proxy.getUserSaltAndChallenge(userName, cb);
    }

    public void authenticateToLogin(String userName, String response, AsyncCallback<UserData> cb) {
        proxy.authenticateToLogin(userName, response, cb);
    }

    public void checkUserLoggedInMethod(AsyncCallback<String> cb) {
        proxy.checkUserLoggedInMethod(cb);
    }

 /*   public void getUserName(AsyncCallback<String> cb) {
        proxy.getUserName(cb);
    }
*/
    public void clearPreviousLoginAuthenticationData(AsyncCallback<Void> cb) {
        proxy.clearPreviousLoginAuthenticationData(cb);
    }

    public void changePasswordEncrypted(String userName, String encryptedPassword, String salt,
            AsyncCallback<Boolean> cb) {
        proxy.changePasswordEncrypted(userName, encryptedPassword, salt, cb);
    }

    public void getNewSalt(AsyncCallback<String> cb) {
        proxy.getNewSalt(cb);
    }

    public void registerUserViaEncrption(String name, String hashedPassword, String emailId, AsyncCallback<UserData> cb) throws UserNameAlreadyExistsException {
        proxy.registerUserViaEncrption(name, hashedPassword, emailId, cb);
    }
    public void getCurrentUserInSession(AsyncCallback<UserData> cb) {
        proxy.getCurrentUserInSession(cb);
    }

    public void logout(AsyncCallback<Void> cb) {
        proxy.logout(cb);
    }

    public void allowsCreateUsers(AsyncCallback<Boolean> async){
        proxy.allowsCreateUsers(async);
    }
}
