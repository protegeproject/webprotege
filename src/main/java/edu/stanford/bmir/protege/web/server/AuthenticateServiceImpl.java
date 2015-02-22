package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.AuthenticateService;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdUtil;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.app.App;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionAttribute;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
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
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(session);
        session.setAttribute(AuthenticationConstants.LOGIN_METHOD, AuthenticationConstants.LOGIN_METHOD_WEBPROTEGE_ACCOUNT);

        if (!MetaProjectManager.getManager().hasValidCredentials(name, password)) {
            webProtegeSession.clearUserInSession();
            return null;
        }

        UserData userData = AuthenticationUtil.createUserData(userId);
        webProtegeSession.setAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER, userId);
        return userData;
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

        User existingUser = getMetaProject().getUser(userName);
        UserId userId = UserId.getUserId(userName);
        if (existingUser != null) {
            UserData userData = AuthenticationUtil.createUserData(userId);
            userData.setProperty(OpenIdUtil.REGISTRATION_RESULT_PROP, OpenIdConstants.USER_ALREADY_EXISTS);
            return userData;
        }

        Salt salt = new SaltProvider().get();
        PasswordDigestAlgorithm passwordDigestAlgorithm = new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider());
        SaltedPasswordDigest saltedPasswordDigest = passwordDigestAlgorithm.getDigestOfSaltedPassword(password, salt);
        EmailAddress email = new EmailAddress(emailId);
        UserData userData = MetaProjectManager.getManager().registerUser(userId, email, saltedPasswordDigest, salt);
        User user = getMetaProject().getUser(userName);

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
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(session);
        webProtegeSession.setUserInSession(userId);

        return userData;
    }

    private MetaProject getMetaProject() {
        return MetaProjectManager.getManager().getMetaProject();
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

            User user = getMetaProject().getUser(userName);

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
}
