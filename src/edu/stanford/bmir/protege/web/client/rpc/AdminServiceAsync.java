package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public interface AdminServiceAsync {

    void changePassword(String userName, String password, AsyncCallback<Void> callback);

    void getUserEmail(String userName, AsyncCallback<String> callback);

    void setUserEmail(String userName, String email, AsyncCallback<Void> callback);

    void sendPasswordReminder(String userName, AsyncCallback<Void> callback);

    void getProjects(String user, AsyncCallback<Collection<ProjectData>> cb);

    void getAllowedOperations(String project, String user, AsyncCallback<Collection<String>> cb);

    void getAllowedServerOperations(String userName, AsyncCallback<Collection<String>> callback);

    void refreshMetaproject(AsyncCallback<Void> cb);

    void getUserSaltAndChallenge(String userName, AsyncCallback<LoginChallengeData> callback);

    void authenticateToLogin(String userName, String response, AsyncCallback<UserData> callback);

    void checkUserLoggedInMethod(AsyncCallback<String> callback);

    //void getUserName(AsyncCallback<String> callback);

    void clearPreviousLoginAuthenticationData(AsyncCallback<Void> callback);

    void getNewSalt(AsyncCallback<String> callback);

    void changePasswordEncrypted(String userName, String encryptedPassword, String salt, AsyncCallback<Boolean> callback);

    void registerUserViaEncrption(String name, String hashedPassword, String emailId, AsyncCallback<UserData> callback);

    void getCurrentUserInSession(AsyncCallback<UserData> async);

    void logout(AsyncCallback<Void> async);

    void allowsCreateUsers(AsyncCallback<Boolean> async);

}
