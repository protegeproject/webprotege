/**
 * 
 */
package edu.stanford.bmir.protege.web.client.ui.openid.constants;

/**
 * Contains constants for OpenId functionality.
 * 
 * @author z.khan
 * 
 */
public class OpenIdConstants {

    //Related to values stored in Http Session
    public static final String HTTPSESSION_OPENID_URL = "userOpenId";

    public static final String HTTPSESSION_OPENID_ID = "openId.id";// Now email associated with Openid

    public static final String HTTPSESSION_OPENID_PROVIDER = "openId.provider";// Open id provider name

    //Related to Users Property
    public static final String OPENID_PROPERTY_PREFIX = "open.id."; //base string for User's Openid properties

    public static final String OPENID_PROPERTY_ID_SUFFIX = ".id"; //email related to open id

    public static final String OPENID_PROPERTY_URL_SUFFIX = ".url"; //For complete OpenId String

    public static final String OPENID_PROPERTY_PROVIDER_SUFFIX = ".provider"; // For OpenId's provider name

    public static final String OPENID_AUTH_SERVLET_URLPATTERN = "openidauthservlet";

    public static final String CREATED_USER_TO_ASSOC_OPEN_ID = "created.user.to.assoc.openid";

    public static final String AUTHENTICATED_USER_TO_ASSOC_OPEN_ID = "authenticated.user.to.assoc.openid";
    
    //Constants related to Register user to associate OpenId functionality
    public static final String USER_ALREADY_EXISTS = "user.already.exists";
    
    public static final String REGISTER_USER_ERROR = "register.user.error";
    
    public static final String REGISTER_USER_SUCCESS = "register.user.success";
}
