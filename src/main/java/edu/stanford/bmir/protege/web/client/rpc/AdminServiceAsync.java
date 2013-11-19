package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.user.UnrecognizedUserNameException;
import edu.stanford.bmir.protege.web.shared.user.UserEmailAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserNameAlreadyExistsException;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public interface AdminServiceAsync {

    void changePassword(String userName, String password, AsyncCallback<Void> callback);

    void getUserEmail(String userName, AsyncCallback<String> callback);

//    void setUserEmail(String userName, String email, AsyncCallback<Void> callback);

    void sendPasswordReminder(String userName, AsyncCallback<Void> callback) throws UnrecognizedUserNameException;

    void getAllowedOperations(String project, String user, AsyncCallback<PermissionsSet> cb);

    void getAllowedServerOperations(String userName, AsyncCallback<PermissionsSet> callback);

    void getUserSaltAndChallenge(String userName, AsyncCallback<LoginChallengeData> callback);

    void authenticateToLogin(String userNameOrEmail, String response, AsyncCallback<UserId> callback);

    void checkUserLoggedInMethod(AsyncCallback<String> callback);

    void clearPreviousLoginAuthenticationData(AsyncCallback<Void> callback);

    void getNewSalt(AsyncCallback<String> callback);

    void changePasswordEncrypted(String userName, String encryptedPassword, String salt, AsyncCallback<Boolean> callback);

    void registerUserViaEncrption(String name, String hashedPassword, String emailId, AsyncCallback<UserData> callback) throws UserNameAlreadyExistsException, UserEmailAlreadyExistsException;

    void getCurrentUserInSession(AsyncCallback<UserId> async);

    void logout(AsyncCallback<Void> async);

    void allowsCreateUsers(AsyncCallback<Boolean> async);

}
