package edu.stanford.bmir.protege.web.client.rpc.data;

/**
 * @author Jack Elliott <jack.elliott@stanford.edu>
 */
public class ApplicationPropertyNames {

    public static final String LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_PROP = "load.ontologies.from.protege.server";

    public static final String PROTEGE_SERVER_PASSWORD_PROP = "webprotege.password";
    public static final String PROTEGE_SERVER_USER_PROP = "webprotege.user";
    public static final String PROTEGE_SERVER_HOSTNAME_PROP = "protege.server.hostname";

    public static final String LOCAL_METAPROJECT_PATH_PROP = "local.metaproject.path";
    public static final String SAVE_INTERVAL_PROP = "server.save.interval.sec";

    public static final String APPLICATION_NAME_PROP = "application.name";
    public static final String APPLICATION_URL_PROP = "application.url";

    public static final String EMAIL_PASSWORD_PROP = "email.password";
    public static final String EMAIL_ACCOUNT_PROP = "email.account";
    public static final String EMAIL_SSL_FACTORY_PROP = "email.ssl.factory";
    public static final String EMAIL_SMTP_PORT_PROP = "email.smtp.port";
    public static final String EMAIL_SMTP_HOST_NAME_PROP = "email.smtp.host.name";
    public static final String EMAIL_RETRY_DELAY_PROP = "email.notification.retry.delay";

    public static final String SERVER_POLLING_TIMEOUT_MINUTES_PROP = "server.polling.timeout.min";
    public static final String APPLICATION_PORT_HTTPS_PROP = "application.https.port";
    public static final String LOGIN_WITH_HTTPS_PROP = "login.with.https";
    public static final String WEBPROTEGE_AUTHENTICATE_WITH_OPENID_PROP = "webprotege.authenticate.with.openid";

    //TODO: to be renamed, use seconds
    public static final String DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_PROP = "daily.notification.startup.delay";
    public static final String HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_PROP = "hourly.notification.startup.delay";
    public static final String IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_PROP = "immediate.notification.startup.delay";
    public static final String IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_PROP = "immediate.notification.interval.delay";

    //TODO: to be renamed: notification....
    public static final String ENABLE_ALL_NOTIFICATION = "enable.all.notification";
    public static final String ENABLE_IMMEDIATE_NOTIFICATION = "enable.immediate.notification";

    //ICD specific
    public static final String ICD_EXPORT_DIR_PROP = "icd.export.dir";

}