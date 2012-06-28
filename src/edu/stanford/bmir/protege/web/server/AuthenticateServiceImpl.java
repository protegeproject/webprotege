package edu.stanford.bmir.protege.web.server;

import java.util.Date;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.AuthenticateService;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdUtil;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.smi.protege.server.metaproject.User;
import edu.stanford.smi.protege.util.Log;

/**
 * Service for Authenticate module for authenticating user.
 *
 * @author z.khan
 *
 */
public class AuthenticateServiceImpl extends WebProtegeRemoteServiceServlet implements AuthenticateService {

    private static final long serialVersionUID = 5326582825556868383L;

    private boolean isAuthenticateWithOpenId() {
        return ApplicationProperties.getWebProtegeAuthenticateWithOpenId();
    }

    public UserData validateUserAndAddInSession(String name, String password) {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConstants.LOGIN_METHOD, AuthenticationConstants.LOGIN_METHOD_WEBPROTEGE_ACCOUNT);

        if (!Protege3ProjectManager.getProjectManager().getMetaProjectManager().hasValidCredentials(name, password)) {
            session.setAttribute(AuthenticationConstants.USERDATA_OBJECT, null);
            return null;
        }

        UserData userData = AuthenticationUtil.createUserData(name);
        session.setAttribute(AuthenticationConstants.USERDATA_OBJECT, userData);
        return userData;
    }

    public UserData validateUser(String name, String password) {
        if (!Protege3ProjectManager.getProjectManager().getMetaProjectManager().hasValidCredentials(name, password)) {
            return null;
        }
        Log.getLogger().info("User " + name + " logged in at: " + new Date());
        return AuthenticationUtil.createUserData(name);
    }

    public void changePassword(String userName, String password) {
        Protege3ProjectManager.getProjectManager().getMetaProjectManager().changePassword(userName, password);
    }

    public UserData registerUserToAssociateOpenId(String userName, String password, String emailId) {

        if (!isAuthenticateWithOpenId()) {
            return null;
        }

        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String userOpenId = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
        String openIdAccName = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID);
        String openIdProvider = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);

        if (userOpenId == null) {
            return null;
        }

        User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
        if (user != null) {
            UserData userData = AuthenticationUtil.createUserData(userName);
            userData.setProperty(OpenIdUtil.REGISTRATION_RESULT_PROP, OpenIdConstants.USER_ALREADY_EXISTS);
            return userData;
        }

        UserData userData = Protege3ProjectManager.getProjectManager().getMetaProjectManager().registerUser(userName, password);
        user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);
        user.setEmail(emailId);

        String openIdPropBase = OpenIdConstants.OPENID_PROPERTY_PREFIX;

        for (int index = 1;; index++) {
            String opnId = user.getPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX);
            if (opnId == null) {
                user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX, userOpenId);
                user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_ID_SUFFIX, openIdAccName);
                user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_PROVIDER_SUFFIX, openIdProvider);
                break;
            }
        }

        Log.getLogger().info("User " + userName + " created at: " + new Date() + " with OpenId: " + userOpenId);

        session.setAttribute(OpenIdConstants.CREATED_USER_TO_ASSOC_OPEN_ID, userData);
        session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
        session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
        session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);
        userData.setProperty(OpenIdUtil.REGISTRATION_RESULT_PROP, OpenIdConstants.REGISTER_USER_SUCCESS);

        session.setAttribute(AuthenticationConstants.USERDATA_OBJECT, userData);

        return userData;
    }

    public UserData validateUserToAssociateOpenId(String userName, String password) {

        if (!isAuthenticateWithOpenId()) {
            return null;
        }

        UserData userData = null;

        try {
            if (!Protege3ProjectManager.getProjectManager().getMetaProjectManager().hasValidCredentials(userName, password)) {
                return null;
            }
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String userOpenId = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
            String openIdAccName = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID);
            String openIdProvider = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);

            if (userOpenId == null) {
                return null;
            }

            User user = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getUser(userName);

            String openIdPropBase = OpenIdConstants.OPENID_PROPERTY_PREFIX;

            for (int index = 1;; index++) {
                String opnId = user.getPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX);
                if (opnId == null) {
                    user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX, userOpenId);
                    user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_ID_SUFFIX, openIdAccName);
                    user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_PROVIDER_SUFFIX, openIdProvider);
                    break;
                }
            }

            Log.getLogger().info("User " + userName + " logged in at: " + new Date() + " with OpenId: " + userOpenId);

            userData = AuthenticationUtil.createUserData(userName);

            session.setAttribute(OpenIdConstants.AUTHENTICATED_USER_TO_ASSOC_OPEN_ID, userData);
            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);
        } catch (Exception e) {
            Log.getLogger().log(Level.SEVERE, "Exception in validateUserToAssociateOpenId", e);
        }

        return userData ;

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

    public UserData registerUser(String userName, String password) {
        return Protege3ProjectManager.getProjectManager().getMetaProjectManager().registerUser(userName, password);
    }

}
