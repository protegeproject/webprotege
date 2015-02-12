package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.AdminService;
import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.server.app.App;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.metaproject.ServerSettingsManager;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.user.UnrecognizedUserNameException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;
import edu.stanford.smi.protege.server.metaproject.Operation;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Random;

/**
 * Administrative services for user management
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AdminServiceImpl extends WebProtegeRemoteServiceServlet implements AdminService {

    private static final long serialVersionUID = 7616699639338297327L;

    public void logout() {
        HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        SessionConstants.removeAttribute(SessionConstants.USER_ID, session);
    }

    public PermissionsSet getAllowedOperations(String project, String user) {
        Collection<Operation> ops = MetaProjectManager.getManager().getAllowedOperations(project, user);
        return toPermissionSet(ops);
    }

    private PermissionsSet toPermissionSet(Collection<Operation> ops) {
        PermissionsSet.Builder builder = PermissionsSet.builder();
        for (Operation op : ops) {
            builder.addPermission(Permission.getPermission(op.getName()));
        }
        return builder.build();
    }

    public LoginChallengeData getUserSaltAndChallenge(String userNameOrEmail) {
    	User user = MetaProjectManager.getManager().getUser(userNameOrEmail);
        if (user == null) {
            return null;
        }
        String userSalt = user.getSalt();
        if (userSalt == null) {
            return null;
        }
        String encodedChallenge = generateSalt();
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConstants.LOGIN_CHALLENGE, encodedChallenge);
        return new LoginChallengeData(userSalt, encodedChallenge);
    }

    public boolean allowsCreateUsers() {
        return MetaProjectManager.getManager().allowsCreateUser();
    }

    public UserId authenticateToLogin(String userNameOrEmail, String response) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();

        final String challenge = (String) session.getAttribute(AuthenticationConstants.LOGIN_CHALLENGE);
        session.setAttribute(AuthenticationConstants.LOGIN_CHALLENGE, null);

        User user = MetaProjectManager.getManager().getUser(userNameOrEmail);
        if (user == null) { //user not in metaproject
            return UserId.getGuest();
        }
        AuthenticationUtil authenticatinUtil = new AuthenticationUtil();
        boolean isverified = authenticatinUtil.verifyChallengedHash(user.getDigestedPassword(), response, challenge);
        if (isverified) {
            UserId userId = UserId.getUserId(user.getName());
            SessionConstants.setAttribute(SessionConstants.USER_ID, userId, session);
            return userId;
        }
        else {
            return UserId.getGuest();
        }

    }

    private static String encodeBytes(byte[] bytes) {
        int stringLength = 2 * bytes.length;
        BigInteger bi = new BigInteger(1, bytes);
        String encoded = bi.toString(16);
        while (encoded.length() < stringLength) {
            encoded = "0" + encoded;
        }
        return encoded;
    }

    private String generateSalt() {
        byte[] salt = new byte[8];
        Random random = new Random();
        random.nextBytes(salt);
        String encodedSalt = encodeBytes(salt);
        return encodedSalt;
    }

    public String checkUserLoggedInMethod() {
        String loginMethod = null;
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession sess = request.getSession();
        loginMethod = (String) sess.getAttribute(AuthenticationConstants.LOGIN_METHOD);
        return loginMethod;
    }

    public void clearPreviousLoginAuthenticationData() {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConstants.LOGIN_METHOD, null);
        SessionConstants.removeAttribute(SessionConstants.USER_ID, session);
        SessionConstants.removeAttribute(SessionConstants.OPEN_ID_ACCOUNT, session);
    }

    public boolean changePasswordEncrypted(String userName, String encryptedPassword, String salt) {
        UserId userId = UserId.getUserId(userName);
        MetaProjectManager.getManager().setDigestedPassword(userId, encryptedPassword, salt);
        return true;
    }

    public String getNewSalt() {
        Random random = new Random();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        String newSalt = encodeBytes(salt);
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConstants.NEW_SALT, newSalt);
        return newSalt;
    }

//    //used only for https
//    public UserData registerUser(String userName, String password, String email) {
//        MetaProjectManager mpm = MetaProjectManager.getManager();
//        throwUserRegistrationExceptionIfAccountCreationIsDisabled(mpm);
//        UserData userData = mpm.registerUser(userName, email, password);
//        OWLAPIMetaProjectStore.getStore().saveMetaProject(mpm);
//        return userData;
//    }

    public UserData registerUserViaEncrption(String name, String hashedPassword, String emailId) throws UserRegistrationException {
        MetaProjectManager metaProjectManager = MetaProjectManager.getManager();

        throwUserRegistrationExceptionIfAccountCreationIsDisabled(metaProjectManager);

        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);
        String emptyPassword = "";

        UserData userData = metaProjectManager.registerUser(name, emailId, emptyPassword);

        UserId userId = UserId.getUserId(name);
        MetaProjectManager.getManager().setDigestedPassword(userId, hashedPassword, salt);
        MetaProjectManager.getManager().setEmail(userId, emailId);
        return userData;
    }

    private void throwUserRegistrationExceptionIfAccountCreationIsDisabled(ServerSettingsManager serverSettingsManager) {
        if(!serverSettingsManager.allowsCreateUser()) {
            throw new UserRegistrationException("Account creation is disabled");
        }
    }
}
