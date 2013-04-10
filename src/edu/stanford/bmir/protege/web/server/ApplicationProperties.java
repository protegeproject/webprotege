package edu.stanford.bmir.protege.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import edu.stanford.bmir.protege.web.client.rpc.data.ApplicationPropertyDefaults;
import edu.stanford.bmir.protege.web.client.rpc.data.ApplicationPropertyNames;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;

/**
 * Provides static methods for accessing WebProtege configuration setting. For
 * example, accessing the properties stored in protege.properties.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class ApplicationProperties {

    /*
     * Paths
     */
    private final static String PROJECTS_DIR = "projects"; //not important - just for the default location of the metaproject
    private final static String METAPROJECT_FILE = "metaproject" + File.separator + "metaproject.pprj";
    private static final String LOCAL_METAPROJECT_PATH_DEFAULT = PROJECTS_DIR + File.separator + METAPROJECT_FILE;

    /*
     * Application settings
     */

    private static final String APPLICATION_NAME_DEFAULT = "WebProtege";
    private static final String APPLICATION_URL_DEFAULT = "localhost";

    /*
     * Protege server settings
     */
    private static final String PROTEGE_SERVER_HOSTNAME_DEFAULT = "localhost";

    private final static String PROTEGE_SERVER_USER_DEFAULT = "webprotege";
    private final static String PROTEGE_SERVER_PASSWORD_DEFAULT = "webprotege";

    private static final boolean LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_DEFAULT = false;

    /*
     * Notifications
     */
    private static final Boolean ENABLE_IMMEDIATE_NOTIFICATION_DEFAULT = Boolean.FALSE;
    private static final Boolean ENABLE_ALL_NOTIFICATION_DEFAULT = Boolean.TRUE;
    private static final int IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_DEFAULT = 2 * 1000 * 60;
    private static final int IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT = 0 * 1000 * 60; //TODO: DO NOT COMMIT
    private static final int HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT = 15 * 1000 * 60;
    private static final int DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT = 30 * 1000 * 60;
    private static final int EMAIL_RETRY_DELAY_DEFAULT = 2 * 1000 * 60;

    /*
    * Account invitation expiration setting
    */
    private static final String ACCOUNT_INVITATION_EXPIRATION_PERIOD_IN_DAYS_PROP = "account.invitation.expiration.period.in.days";
    private static final int ACCOUNT_INVITATION_EXPIRATION_PERIOD_IN_DAYS_DEFAULT = 30;

    /*
     * Open id Authentication setting
     */
    private static final boolean WEBPROTEGE_AUTHENTICATE_WITH_OPENID_DEFAULT = true;

    private static final String APPLICATION_PATH_DEFAULT = "";
    private static final boolean LOGIN_WITH_HTTPS_DEFAULT = false;


    /*
     * Automatic save for local projects
     */
    private static final int SAVE_INTERVAL_DEFAULT = 120;
    public static final int NO_SAVE = -1;
    
    
    private static final Properties blacklistedProperties = new Properties();

    private static final String DEFAULT_SMTP_HOST = "smtp.gmail.com";

    private static final String DEFAULT_SMTP_PORT = "465";

    private static final String DEFAULT_EMAIL_ACCOUNT = "webprotege2012@gmail.com";


    static  {
        try {
            File propertyFile = new File(FileUtil.getRealPath(), "blacklist.properties");
            InputStream is = new FileInputStream(propertyFile);
            blacklistedProperties.load(is);
        } catch (Exception e){
            Log.getLogger().warning("Could not retrieve blacklist.properties from " + FileUtil.getRealPath() + ". " + e.getMessage());
            if (Log.getLogger().isLoggable(Level.FINE)) {
                Log.getLogger().log(Level.FINE, "Could not retrieve blacklist.properties.", e);
            }
        }
    }

    public static URI getWeprotegeDirectory() {
        String uri = FileUtil.getRealPath();
        return URIUtilities.createURI(uri);
    }

    public static URI getLocalMetaprojectURI() {
        String path = edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.LOCAL_METAPROJECT_PATH_PROP);
        if (path == null) {
            path = FileUtil.getRealPath() + LOCAL_METAPROJECT_PATH_DEFAULT;
        }
        Log.getLogger().info("Path to local metaproject: " + path);
        return URIUtilities.createURI(path);
    }

    public static String getProtegeServerHostName() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.PROTEGE_SERVER_HOSTNAME_PROP,
                PROTEGE_SERVER_HOSTNAME_DEFAULT);
    }

    static String getProtegeServerUser() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.PROTEGE_SERVER_USER_PROP,
                PROTEGE_SERVER_USER_DEFAULT);
    }

    static String getProtegeServerPassword() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.PROTEGE_SERVER_PASSWORD_PROP,
                PROTEGE_SERVER_PASSWORD_DEFAULT);
    }

    public static boolean getLoadOntologiesFromServer() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getBooleanProperty(
                ApplicationPropertyNames.LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_PROP, LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_DEFAULT);
    }

    public static int getLocalProjectSaveInterval() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.SAVE_INTERVAL_PROP,
                SAVE_INTERVAL_DEFAULT);
    }

    public static String getSmtpHostName() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.EMAIL_SMTP_HOST_NAME_PROP, DEFAULT_SMTP_HOST);
    }

    public static String getSmtpPort() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.EMAIL_SMTP_PORT_PROP, DEFAULT_SMTP_PORT);
    }

    public static String getSslFactory() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.EMAIL_SSL_FACTORY_PROP, "javax.net.ssl.SSLSocketFactory");
    }

    public static String getEmailAccount() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.EMAIL_ACCOUNT_PROP, DEFAULT_EMAIL_ACCOUNT);
    }

    public static String getEmailPassword() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.EMAIL_PASSWORD_PROP, "protege123");
    }

    public static String getLoggingEmail() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.LOGGING_EMAIL_PROP, DEFAULT_EMAIL_ACCOUNT);
    }

    public static String getApplicationName() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.APPLICATION_NAME_PROP,
                APPLICATION_NAME_DEFAULT);
    }

    public static boolean getWebProtegeAuthenticateWithOpenId() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getBooleanProperty(
                ApplicationPropertyNames.WEBPROTEGE_AUTHENTICATE_WITH_OPENID_PROP, WEBPROTEGE_AUTHENTICATE_WITH_OPENID_DEFAULT);
    }

    public static boolean getLoginWithHttps() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getBooleanProperty(
                ApplicationPropertyNames.LOGIN_WITH_HTTPS_PROP, LOGIN_WITH_HTTPS_DEFAULT);
    }

    public static String getApplicationHttpsPort() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(
                ApplicationPropertyNames.APPLICATION_PORT_HTTPS_PROP, ApplicationPropertyDefaults.APPLICATION_PORT_HTTPS_DEFAULT);
    }

    public static int getServerPollingTimeoutMinutes() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.SERVER_POLLING_TIMEOUT_MINUTES_PROP,
                ApplicationPropertyDefaults.SERVER_POLLING_TIMEOUT_MINUTES_DEFAULT);
    }

    public static String getApplicationUrl() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.APPLICATION_URL_PROP, APPLICATION_URL_DEFAULT);
    }

    public static Boolean getImmediateThreadsEnabled() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getBooleanProperty(ApplicationPropertyNames.ENABLE_IMMEDIATE_NOTIFICATION,
                ENABLE_IMMEDIATE_NOTIFICATION_DEFAULT);
    }

    public static Boolean getAllNotificationEnabled() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getBooleanProperty(ApplicationPropertyNames.ENABLE_ALL_NOTIFICATION,
                ENABLE_ALL_NOTIFICATION_DEFAULT);
    }

    public static Integer getEmailRetryDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.EMAIL_RETRY_DELAY_PROP,
                EMAIL_RETRY_DELAY_DEFAULT);
    }

    public static Integer getImmediateThreadStartupDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_PROP
                , IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT);
    }

    public static Integer getHourlyThreadStartupDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_PROP,
                HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT);
    }

    public static Integer getDailyThreadStartupDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_PROP,
                DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT);
    }

    public static Integer getImmediateThreadInterval() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_PROP,
                IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_DEFAULT);
    }

    public static HashMap<String, String> getPropertiesForClient(){
        final Properties applicationProperties1 = edu.stanford.smi.protege.util.ApplicationProperties.getApplicationProperties();
        final Set<String> stringSet = applicationProperties1.stringPropertyNames();
        final HashMap<String, String> applicationProperties = new HashMap<String, String>();
        for (String propertyName : stringSet) {
            applicationProperties.put(propertyName, applicationProperties1.getProperty(propertyName));
        }

        for (String blacklistedProperty : ApplicationProperties.blacklistedProperties.stringPropertyNames()) {
            applicationProperties.remove(blacklistedProperty);
        }

        return applicationProperties;
    }

    public static int getAccountInvitationExpirationPeriodInDays() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ACCOUNT_INVITATION_EXPIRATION_PERIOD_IN_DAYS_PROP,
                ACCOUNT_INVITATION_EXPIRATION_PERIOD_IN_DAYS_DEFAULT);
    }
    

}
