package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.AuthenticateService;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdUtil;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.User;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.logging.Level;

/**
 * Service for Authenticate module for authenticating user.
 *
 * @author z.khan
 *
 */
public class AuthenticateServiceImpl extends WebProtegeRemoteServiceServlet implements AuthenticateService {

    private static final long serialVersionUID = 5326582825556868383L;

    private boolean isAuthenticateWithOpenId() {
        return WebProtegeProperties.get().isOpenIdAuthenticationEnabled();
    }

    public UserData validateUserAndAddInSession(String name, String password) {
        UserId userId = UserId.getUserId(name);

        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        session.setAttribute(AuthenticationConstants.LOGIN_METHOD, AuthenticationConstants.LOGIN_METHOD_WEBPROTEGE_ACCOUNT);

        if (!MetaProjectManager.getManager().hasValidCredentials(name, password)) {
            SessionConstants.removeAttribute(SessionConstants.USER_ID, session);
            return null;
        }

        UserData userData = AuthenticationUtil.createUserData(userId);
        SessionConstants.setAttribute(SessionConstants.USER_ID, userId, session);
        return userData;
    }

    public UserData validateUser(String name, String password) {
        if (!MetaProjectManager.getManager().hasValidCredentials(name, password)) {
            return null;
        }
        Log.getLogger().info("User " + name + " logged in at: " + new Date());
        return AuthenticationUtil.createUserData(UserId.getUserId(name));
    }

    public void changePassword(String userName, String password) {
        MetaProjectManager.getManager().changePassword(userName, password);
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

        User user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
        if (user != null) {
            UserData userData = AuthenticationUtil.createUserData(UserId.getUserId(userName));
            userData.setProperty(OpenIdUtil.REGISTRATION_RESULT_PROP, OpenIdConstants.USER_ALREADY_EXISTS);
            return userData;
        }

        UserData userData = MetaProjectManager.getManager().registerUser(userName, emailId, password);
        user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
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

        SessionConstants.setAttribute(SessionConstants.USER_ID, UserId.getUserId(userName), session);

        return userData;
    }

    public UserData validateUserToAssociateOpenId(String userName, String password) {

        if (!isAuthenticateWithOpenId()) {
            return null;
        }

        UserData userData = null;

        try {
            if (!MetaProjectManager.getManager().hasValidCredentials(userName, password)) {
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

            User user = MetaProjectManager.getManager().getMetaProject().getUser(userName);

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

            userData = AuthenticationUtil.createUserData(UserId.getUserId(userName));

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
        String email = MetaProjectManager.getManager().getUserEmail(userName);
        if (email == null) {
            throw new IllegalArgumentException("User " + userName + " does not have an email configured.");
        }
        changePassword(userName, EmailConstants.RESET_PASSWORD);
        EmailUtil.sendEmail(email, EmailConstants.FORGOT_PASSWORD_SUBJECT, EmailConstants.FORGOT_PASSWORD_EMAIL_BODY);

    }

    public UserData registerUser(String userName, String password, String email) {
        return MetaProjectManager.getManager().registerUser(userName, email, password);
    }

}
