package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UnrecognizedUserNameException;
import edu.stanford.bmir.protege.web.shared.user.UserEmailAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserNameAlreadyExistsException;

import java.util.Collection;

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

    public void getUserEmail(UserId userId, AsyncCallback<String> callback) {
        proxy.getUserEmail(userId.getUserName(), callback);
    }

//    public void setUserEmail(UserId userId, String email, AsyncCallback<Void> callback) {
//        proxy.setUserEmail(userId.getUserName(), email, callback);
//    }

    public void getAllowedOperations(ProjectId projectId, UserId userId, AsyncCallback<PermissionsSet> cb) {
        proxy.getAllowedOperations(projectId.getId(), userId.getUserName(), cb);
    }

    public void getAllowedServerOperations(UserId userId, AsyncCallback<PermissionsSet> cb) {
        proxy.getAllowedServerOperations(userId.getUserName(), cb);
    }

    public void changePassword(UserId userId, String password, AsyncCallback<Void> cb) {
        proxy.changePassword(userId.getUserName(), password, cb);
    }

    public void sendPasswordReminder(UserId userId, AsyncCallback<Void> cb) throws UnrecognizedUserNameException{
        proxy.sendPasswordReminder(userId.getUserName(), cb);
    }

    public void getUserSaltAndChallenge(UserId userId, AsyncCallback<LoginChallengeData> cb) {
        proxy.getUserSaltAndChallenge(userId.getUserName(), cb);
    }

    //FIXME: userId - should be a string, can be the user id or the email
    public void authenticateToLogin(UserId userId, String response, AsyncCallback<UserId> cb) {
        proxy.authenticateToLogin(userId.getUserName(), response, cb);
    }

    public void checkUserLoggedInMethod(AsyncCallback<String> cb) {
        proxy.checkUserLoggedInMethod(cb);
    }

    public void clearPreviousLoginAuthenticationData(AsyncCallback<Void> cb) {
        proxy.clearPreviousLoginAuthenticationData(cb);
    }

    public void changePasswordEncrypted(UserId userId, String encryptedPassword, String salt,
            AsyncCallback<Boolean> cb) {
        proxy.changePasswordEncrypted(userId.getUserName(), encryptedPassword, salt, cb);
    }

    public void getNewSalt(AsyncCallback<String> cb) {
        proxy.getNewSalt(cb);
    }

    public void registerUserViaEncrption(String name, String hashedPassword, String emailId, AsyncCallback<UserData> cb) throws UserNameAlreadyExistsException, UserEmailAlreadyExistsException {
        proxy.registerUserViaEncrption(name, hashedPassword, emailId, cb);
    }
    public void getCurrentUserInSession(AsyncCallback<UserId> cb) {
        proxy.getCurrentUserInSession(cb);
    }

    public void logout(AsyncCallback<Void> cb) {
        proxy.logout(cb);
    }

    public void allowsCreateUsers(AsyncCallback<Boolean> async){
        proxy.allowsCreateUsers(async);
    }
}
