package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserNameAlreadyExistsException;

import java.util.Collection;

/**
 * A service for accessing administrative and configuration data stored in
 * Protege's Metaproject. Examples of data stored in the Metaproject are:
 * <ul>
 * <li>user names and passwords</li>
 * <li>user groups and permissions</li>
 * <li>projects available on the server</li>
 * <li>project locations, owners, and descriptions</li>
 * </ul>
 * The list above is not meant to be exhaustive.
 * <p />
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService {

    UserData registerUserViaEncrption(String name, String hashedPassword, String emailId) throws UserNameAlreadyExistsException;

    void changePassword(String userName, String password);

    String getUserEmail(String userName);

    void setUserEmail(String userName, String email);

    void sendPasswordReminder(String userName);

    PermissionsSet getAllowedOperations(String project, String user);

    PermissionsSet getAllowedServerOperations(String userName);


    LoginChallengeData getUserSaltAndChallenge(String userName);

    UserId authenticateToLogin(String userName, String response);

    /**
     * Checks whether user logged in and returns the login method(openid or
     * webprotege account)
     */
    String checkUserLoggedInMethod();

    void clearPreviousLoginAuthenticationData();

    String getNewSalt();

    boolean changePasswordEncrypted(String userName, String encryptedPassword, String salt);

    UserId getCurrentUserInSession();

    void logout();

    boolean allowsCreateUsers();
}
