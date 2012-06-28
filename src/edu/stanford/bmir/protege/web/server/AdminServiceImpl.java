package edu.stanford.bmir.protege.web.server;

import java.math.BigInteger;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.AdminService;
import edu.stanford.bmir.protege.web.client.rpc.data.LoginChallengeData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserNameAlreadyExistsException;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
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


    public UserData getCurrentUserInSession() {
        HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        final UserData userData = (UserData) session.getAttribute(SessionConstants.USER_DATA_PARAMETER);
        return userData;
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
        session.setAttribute(SessionConstants.USER_DATA_PARAMETER, null);
    }

    public void changePassword(String userName, String password) {
        Protege3ProjectManager.getProjectManager().getMetaProjectManager().changePassword(userName, password);
    }

    public String getUserEmail(String userName) {
        return Protege3ProjectManager.getProjectManager().getMetaProjectManager().getUserEmail(userName);
    }

    public void setUserEmail(String userName, String email) {
        Protege3ProjectManager.getProjectManager().getMetaProjectManager().setUserEmail(userName, email);
    }

    public List<ProjectData> getProjects(String user) {
        return Protege3ProjectManager.getProjectManager().getMetaProjectManager().getProjectsData(user);
    }

    public Collection<String> getAllowedOperations(String project, String user) {
        Collection<Operation> ops = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getAllowedOperations(project, user);
        Collection<String> opsAsString = new ArrayList<String>();
        for (Operation op : ops) {
            opsAsString.add(op.getName());
        }
        return opsAsString;
    }

    public Collection<String> getAllowedServerOperations(String userName) {
        Collection<Operation> ops = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getAllowedServerOperations(userName);
        Collection<String> opsAsString = new ArrayList<String>();
        for (Operation op : ops) {
            opsAsString.add(op.getName());
        }
        return opsAsString;
    }

    public void refreshMetaproject() {
        Protege3ProjectManager.getProjectManager().getMetaProjectManager().reloadMetaProject();
    }

    public void sendPasswordReminder(String userName) {
        String email = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getUserEmail(userName);
        if (email == null) {
            throw new IllegalArgumentException("User " + userName + " does not have an email configured.");
        }
        changePassword(userName, EmailConstants.RESET_PASSWORD);
        EmailUtil.sendEmail(email, EmailConstants.FORGOT_PASSWORD_SUBJECT, EmailConstants.FORGOT_PASSWORD_EMAIL_BODY,
                ApplicationProperties.getEmailAccount());
    }

    public LoginChallengeData getUserSaltAndChallenge(String userName) {
        String userSalt = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getUserSalt(userName);
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
        return Protege3ProjectManager.getProjectManager().getMetaProjectManager().allowsCreateUser();
    }

    public UserData authenticateToLogin(String userName, String response) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String challenge = (String) session.getAttribute(AuthenticationConstants.LOGIN_CHALLENGE);
        session.setAttribute(AuthenticationConstants.LOGIN_CHALLENGE, null);

        User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
        if (user == null) { //user not in metaproject
            return null;
        }

        UserData userData = null;

        AuthenticationUtil authenticatinUtil = new AuthenticationUtil();
        boolean isverified = authenticatinUtil.verifyChallengedHash(user.getDigestedPassword(), response, challenge);
        if (isverified) {
            userData = AuthenticationUtil.createUserData(userName);
            session.setAttribute(SessionConstants.USER_DATA_PARAMETER, userData);
        }

        return userData;
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
        session.setAttribute(AuthenticationConstants.USERDATA_OBJECT, null);
        session.setAttribute(AuthenticationConstants.LOGIN_METHOD, null);
        session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
        session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);
        session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
    }

    public boolean changePasswordEncrypted(String userName, String encryptedPassword, String salt) {
        User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
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
        MetaProjectManager mpm = Protege3ProjectManager.getProjectManager().getMetaProjectManager();
        UserData userData = mpm.registerUser(userName, password);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(mpm);
        return userData;
    }

    public UserData registerUserViaEncrption(String name, String hashedPassword, String emailId) throws UserNameAlreadyExistsException {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);
        String emptyPassword = "";

        MetaProjectManager metaProjectManager = Protege3ProjectManager.getProjectManager().getMetaProjectManager();
        UserData userData = metaProjectManager.registerUser(name, emptyPassword);

        User user = metaProjectManager.getMetaProject().getUser(name);
        user.setDigestedPassword(hashedPassword, salt);
        user.setEmail(emailId);

        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProjectManager);

        return userData;
    }
}
