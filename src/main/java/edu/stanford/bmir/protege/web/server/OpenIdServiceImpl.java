package edu.stanford.bmir.protege.web.server;

import com.google.common.base.Optional;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.OpenIdService;
import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.OpenIdUtil;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.User;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.logging.Level;

/**
 * Administrative services for maintaining openid url, id and provider name with
 * user.
 *
 * @author z.khan
 *
 */
public class OpenIdServiceImpl extends RemoteServiceServlet implements OpenIdService {

    private static final long serialVersionUID = 3551138317576962582L;

    public OpenIdData getUsersOpenId(String name) {
        OpenIdData oIdData = new OpenIdData();
        try {

            List<String> openIdList = new ArrayList<String>();
            List<String> openIdAccId = new ArrayList<String>();
            List<String> openIdProvider = new ArrayList<String>();
            User user = MetaProjectManager.getManager().getMetaProject().getUser(name);
            Collection<PropertyValue> propColl = user.getPropertyValues();
            for (Iterator<PropertyValue> iterator = propColl.iterator(); iterator.hasNext();) {
                PropertyValue propertyValue = iterator.next();
                if (propertyValue.getPropertyName().startsWith(OpenIdConstants.OPENID_PROPERTY_PREFIX)
                        && propertyValue.getPropertyName().endsWith(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX)) {
                    String openIdAccNamePropName = propertyValue.getPropertyName().replace(
                            OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX, OpenIdConstants.OPENID_PROPERTY_ID_SUFFIX);
                    String openIdProvdNamePropName = propertyValue.getPropertyName()
                    .replace(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX,
                            OpenIdConstants.OPENID_PROPERTY_PROVIDER_SUFFIX);
                    String openIdAccNamePropValue = user.getPropertyValue(openIdAccNamePropName);
                    String openIdProvdNamePropValue = user.getPropertyValue(openIdProvdNamePropName);
                    openIdList.add(propertyValue.getPropertyValue());
                    openIdAccId.add(openIdAccNamePropValue);
                    openIdProvider.add(openIdProvdNamePropValue);
                }
            }
            oIdData.setName(name);
            oIdData.setOpenIdList(openIdList);
            oIdData.setOpenIdAccId(openIdAccId);
            oIdData.setOpenIdProvider(openIdProvider);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Errors at retrieving Users OpenId:", e);
        }
        return oIdData;
    }

    @SuppressWarnings("finally")
    public OpenIdData removeAssocToOpenId(String name, String opnId) {
        OpenIdData openIdData = new OpenIdData();
        try {
            User user = MetaProjectManager.getManager().getMetaProject().getUser(name);
            Collection<PropertyValue> propColl = user.getPropertyValues();
            for (Iterator<PropertyValue> iterator = propColl.iterator(); iterator.hasNext();) {
                PropertyValue propertyValue = iterator.next();
                if (propertyValue.getPropertyName().startsWith(OpenIdConstants.OPENID_PROPERTY_PREFIX)
                        && propertyValue.getPropertyName().endsWith(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX)) {
                    String openIdAccNamePropName = propertyValue.getPropertyName().replace(
                            OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX, OpenIdConstants.OPENID_PROPERTY_ID_SUFFIX);
                    String openIdProvdNamePropName = propertyValue.getPropertyName()
                    .replace(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX, OpenIdConstants.OPENID_PROPERTY_PROVIDER_SUFFIX);
                    String openIdAccNamePropValue = user.getPropertyValue(openIdAccNamePropName);
                    String openIdProvdNamePropValue = user.getPropertyValue(openIdProvdNamePropName);
                    if (propertyValue.getPropertyValue().trim().equalsIgnoreCase(opnId)) {

                        user.removePropertyValue(propertyValue.getPropertyName(), propertyValue.getPropertyValue());
                        user.removePropertyValue(openIdAccNamePropName, openIdAccNamePropValue);
                        user.removePropertyValue(openIdProvdNamePropName, openIdProvdNamePropValue);
                    }
                }
            }
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Errors at removing Users Association to OpenId:", e);
        } finally {
            openIdData = getUsersOpenId(name);
            openIdData.setName(name);
            return openIdData;
        }
    }

    public OpenIdData assocNewOpenIdToUser(String name) {
        OpenIdData openIdData = new OpenIdData();
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String userOpenId = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
            String openIdAccName = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID);
            String openIdProvider = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);

            if (userOpenId == null) {
                return null;
            }

            User user = MetaProjectManager.getManager().getMetaProject().getUser(name);
            String openIdPropBase = OpenIdConstants.OPENID_PROPERTY_PREFIX;

            for (int index = 1;; index++) {
                String opnId = user.getPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX);
                if (opnId == null) {
                    user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX, userOpenId);
                    user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_ID_SUFFIX, openIdAccName);
                    user.addPropertyValue(openIdPropBase + index + OpenIdConstants.OPENID_PROPERTY_PROVIDER_SUFFIX, openIdProvider);

                    session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
                    session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
                    session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);
                    break;
                }
            }
            openIdData = getUsersOpenId(name);
            openIdData.setName(name);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Errors at Associating New OpenId to  User:", e);

        }
        return openIdData;
    }

    /* returns UserData object .
     * UserData objects name field is null if open id is not already associated,
     * else name field contains name of the user associated with open id.
     * null if open id attribute is not present in session
     * (non-Javadoc)
     * @see edu.stanford.bmir.protege.web.client.rpc.OpenIdService#isOpenIdInSession()
     */
    public UserData isOpenIdInSessForAddNewOpenId() {
        UserData userData = null;
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String openIdUrl = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);

            if (openIdUrl != null) {
                Optional<UserId> userId = MetaProjectManager.getManager().getUserAssociatedWithOpenId(openIdUrl);
                if (userId.isPresent()) {
                    userData = new UserData(userId.get());
                    session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
                    session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
                    session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);
                }
            }
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Errors at isOpenIdInSessForAddNewOpenId  User:", e);
        }
        return userData;
    }

    private boolean isAuthenticateWithOpenId() {
        return WebProtegeProperties.get().isOpenIdAuthenticationEnabled();
    }

    public UserData checkIfOpenIdInSessionForLogin() {
        UserData userData = null;
        if (isAuthenticateWithOpenId()) {
            try {
                HttpServletRequest request = this.getThreadLocalRequest();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String openIdUrl = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
                    if (openIdUrl != null) {
                        Optional<UserId> userId = MetaProjectManager.getManager().getUserAssociatedWithOpenId(openIdUrl);
                        if (userId.isPresent()) { //user is associated with openid
                            userData = new UserData(userId.get());
                            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
                            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
                            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);
                            SessionConstants.setAttribute(SessionConstants.USER_ID, userId.get(), session);
                        }
                    }
                }
            } catch (Exception e) {
                Log.getLogger().log(Level.WARNING, "Errors at checkIfOpenIdInSessionForLogin  User:", e);
            }
        }
        return userData;
    }

    public void clearCreateUserToAssocOpenIdSessData() {
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            session.setAttribute(OpenIdConstants.CREATED_USER_TO_ASSOC_OPEN_ID, null);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Error at clearCreateUserToAssocOpenIdSessData : ", e);
        }
    }

    public UserData checkIfUserCreatedToAssocOpenId() {
        UserData userCreated = null;
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            userCreated = (UserData) session.getAttribute(OpenIdConstants.CREATED_USER_TO_ASSOC_OPEN_ID);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Error at checkIfUserCreatedToAssocOpenId : ", e);
        }

        return userCreated;
    }

    public void clearAuthUserToAssocOpenIdSessData() {
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            session.setAttribute(OpenIdConstants.AUTHENTICATED_USER_TO_ASSOC_OPEN_ID, null);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Error at clearAuthUserToAssocOpenIdSessData : ", e);
        }
    }

    public UserData checkIfUserAuthenticatedToAssocOpenId() {
        UserData userAuthenticated = null;
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            userAuthenticated = (UserData) session.getAttribute(OpenIdConstants.AUTHENTICATED_USER_TO_ASSOC_OPEN_ID);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Error at checkIfUserAuthenticatedToAssocOpenId : ", e);
        }

        return userAuthenticated;
    }

    public UserData registerUserToAssocOpenIdWithEncrption(String userName, String hashedPassword, String emailId) {

        if (!isAuthenticateWithOpenId()) {
            return null;
        }

        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        String userOpenId = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
        String openIdAccName = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID);
        String openIdProvider = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);
        String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);

        if (userOpenId == null && salt != null) {
            UserData userData = AuthenticationUtil.createUserData(UserId.getUserId(userName));
            userData.setProperty(OpenIdUtil.REGISTRATION_RESULT_PROP, OpenIdConstants.REGISTER_USER_ERROR);
            return userData;
        }

        User user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
        if (user != null) {
            UserData userData = AuthenticationUtil.createUserData(UserId.getUserId(userName));
            userData.setProperty(OpenIdUtil.REGISTRATION_RESULT_PROP, OpenIdConstants.USER_ALREADY_EXISTS);
            return userData;
        }

        UserData userData = MetaProjectManager.getManager().registerUser(userName, emailId, "");

        user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
        user.setEmail(emailId);
        user.setDigestedPassword(hashedPassword, salt);

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

        return userData;
    }

    public UserData validateUserToAssocOpenIdWithEncrypt(String userName, String response) {
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            HttpSession session = request.getSession();
            String challenge = (String) session.getAttribute(AuthenticationConstants.LOGIN_CHALLENGE);
            session.setAttribute(AuthenticationConstants.LOGIN_CHALLENGE, null);
            User user = MetaProjectManager.getManager().getMetaProject().getUser(userName);
            if (user == null) {
                return null;
            }
            String storedHashedPswd = user.getDigestedPassword();
            AuthenticationUtil authenticatinUtil = new AuthenticationUtil();

            if (!authenticatinUtil.verifyChallengedHash(storedHashedPswd, response, challenge)) {
                return null;
            }

            String userOpenId = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
            String openIdAccName = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID);
            String openIdProvider = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);

            if (userOpenId == null) {
                return null;
            }

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

            session.setAttribute(OpenIdConstants.AUTHENTICATED_USER_TO_ASSOC_OPEN_ID, userName);
            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL, null);
            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID, null);
            session.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, null);

            OWLAPIMetaProjectStore.getStore().saveMetaProject(MetaProjectManager.getManager());
        } catch (Exception e) {
            Log.getLogger().log(Level.SEVERE, "Exception in validateUserToAssociateOpenId", e);
        }

        return AuthenticationUtil.createUserData(UserId.getUserId(userName));
    }
}
