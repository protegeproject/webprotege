package edu.stanford.bmir.protege.web.server;

import java.math.BigInteger;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.AdminService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.smi.protege.server.metaproject.Operation;
import edu.stanford.smi.protege.server.metaproject.User;

/**
 * Administrative services for user management
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AdminServiceImpl extends WebProtegeRemoteServiceServlet implements AdminService {

    private static final long serialVersionUID = 7616699639338297327L;


    public UserId getCurrentUserInSession() {
        HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        return SessionConstants.getUserId(session);
    }

    /*
     //weird..
    public String getUserName() {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession sess = request.getSession();
        long initTime = System.currentTimeMillis();
        long currTime = initTime;
        String userName = null;
        long min = 1000 * 60 * 5;
        do {
            UserData uData = (UserData) sess.getAttribute(AuthenticationConstants.USERDATA_OBJECT);
            if (uData != null) {
                userName = uData.getName();
                break;
            }
            currTime = System.currentTimeMillis();
        } while ((currTime - initTime) < min);
        return userName;
    }
 */

    public void logout() {
        HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        SessionConstants.removeAttribute(SessionConstants.USER_ID, session);
    }

    public void changePassword(String userName, String password) {
        MetaProjectManager.getManager().changePassword(userName, password);
    }

    public String getUserEmail(String userName) {
        return MetaProjectManager.getManager().getUserEmail(userName);
    }

    public void setUserEmail(String userName, String email) {
        MetaProjectManager.getManager().setUserEmail(userName, email);
    }

    public List<ProjectData> getProjects(String user) {
        return MetaProjectManager.getManager().getProjectsData(user);
    }

    public PermissionsSet getAllowedOperations(String project, String user) {
        Collection<Operation> ops = MetaProjectManager.getManager().getAllowedOperations(project, user);
        return toPermissionSet(ops);
    }

    public PermissionsSet getAllowedServerOperations(String userName) {
        Collection<Operation> ops = MetaProjectManager.getManager().getAllowedServerOperations(userName);
        return toPermissionSet(ops);
    }

    private PermissionsSet toPermissionSet(Collection<Operation> ops) {
        PermissionsSet.Builder builder = PermissionsSet.builder();
        for (Operation op : ops) {
            builder.addPermission(Permission.getPermission(op.getName()));
        }
        return builder.build();
    }

//    public void refreshMetaproject() {
//        MetaProjectManager.getManager().reloadMetaProject();
//    }

    public void sendPasswordReminder(String userName) {
        String email = MetaProjectManager.getManager().getUserEmail(userName);
        if (email == null) {
            throw new IllegalArgumentException("User " + userName + " does not have an email configured.");
        }
        changePassword(userName, EmailConstants.RESET_PASSWORD);
        if(!WebProtegeProperties.getEmailAccount().isPresent()) {
            throw new WebProtegeConfigurationException("Email is not configured on the server.  Please contact the administrator");
        }
        EmailUtil.sendEmail(email, EmailConstants.FORGOT_PASSWORD_SUBJECT, EmailConstants.FORGOT_PASSWORD_EMAIL_BODY);
    }

    public LoginChallengeData getUserSaltAndChallenge(String userName) {
        String userSalt = MetaProjectManager.getManager().getUserSalt(userName);
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

    public UserId authenticateToLogin(String userName, String response) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();

        String challenge = (String) session.getAttribute(AuthenticationConstants.LOGIN_CHALLENGE);
        session.setAttribute(AuthenticationConstants.LOGIN_CHALLENGE, null);

        User user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
        if (user == null) { //user not in metaproject
            return UserId.getGuest();
        }
        AuthenticationUtil authenticatinUtil = new AuthenticationUtil();
        boolean isverified = authenticatinUtil.verifyChallengedHash(user.getDigestedPassword(), response, challenge);
        if (isverified) {
            UserId userId = UserId.getUserId(userName);
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
        User user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
        if (user == null) {
            return false;
        }
        user.setDigestedPassword(encryptedPassword, salt);
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

    //used only for https
    public UserData registerUser(String userName, String password) {
        MetaProjectManager mpm = MetaProjectManager.getManager();
        UserData userData = mpm.registerUser(userName, password);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(mpm);
        return userData;
    }

    public UserData registerUserViaEncrption(String name, String hashedPassword, String emailId) throws UserNameAlreadyExistsException {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);
        String emptyPassword = "";

        MetaProjectManager metaProjectManager = MetaProjectManager.getManager();
        UserData userData = metaProjectManager.registerUser(name, emptyPassword);

        User user = metaProjectManager.getMetaProject().getUser(name);
        user.setDigestedPassword(hashedPassword, salt);
        user.setEmail(emailId);

        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProjectManager);

        return userData;
    }
}
