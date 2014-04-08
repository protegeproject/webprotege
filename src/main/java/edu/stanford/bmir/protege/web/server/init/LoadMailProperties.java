package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.app.App;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.mail.MailManager;
import edu.stanford.bmir.protege.web.server.mail.WebProtegeLoggerMessagingExceptionHandler;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 */
public class LoadMailProperties implements ConfigurationTask {

    private static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(LoadMailProperties.class);

    public static final String MAIL_PROPERTIES_FILE_NAME = "mail.properties";

    public static final String MAIL_PROPERTIES_PREFIX = "mail.smtp";

    public static final String MAIL_SMTP_USER_MISSING_MESSAGE = "Mail properties are not configured correctly.  The mail.smtp.auth has been specified, which means that authentication will be used to send emails, but the mail.smtp.user property has not been specified.  Please specify a user name for the smtp mail server by using this property.  Mail properties may be specified using a mail.properties file placed in the root directory of the WebProtégé web-app directory.";

    public static final String MAIL_SMTP_PASSWORD_MISSING_MESSAGE = "Mail properties are not configured correctly.  The mail.smtp.auth has been specified, which means that authentication will be used to send emails, but the mail.smtp.password property has not been specified.  Please specify a password for the smtp mail server by using this property.  Mail properties may be specified using a mail.properties file placed in the root directory of the WebProtégé web-app directory.";

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        Properties mailProperties = new Properties();
        String fileName = MAIL_PROPERTIES_FILE_NAME;
        File file = new File(new File(servletContext.getRealPath("")), fileName);
        if(file.exists()) {
            LOGGER.info("Attempting to load mail properties from %s", file.getAbsolutePath());
            try {
                InputStream inStream = new BufferedInputStream(new FileInputStream(file));
                mailProperties.load(inStream);
                inStream.close();
            } catch (IOException e) {
                throw new WebProtegeConfigurationException("Could not read " + file.getAbsolutePath() + ". Message: " + e.getMessage());
            }
        }
        overridePropertiesWithSystemProperties(mailProperties);
        if("true".equals(mailProperties.getProperty(MailManager.MAIL_SMTP_AUTH))) {
            if(mailProperties.getProperty(MailManager.MAIL_SMTP_USER) == null) {
                throw new WebProtegeConfigurationException(MAIL_SMTP_USER_MISSING_MESSAGE);
            }
            if(mailProperties.getProperty(MailManager.MAIL_SMTP_PASSWORD) == null) {
                throw new WebProtegeConfigurationException(MAIL_SMTP_PASSWORD_MISSING_MESSAGE);
            }
        }
        App.get().setMailManager(new MailManager(mailProperties, new WebProtegeLoggerMessagingExceptionHandler()));

    }

    /**
     * Overrides any property values in the specified {@link Properties} object with property values that are specified
     * via the command line (with a -D argument) or via environment variables.
     * @param properties The properties object whose property values should be replaced.  Not {@code null}.
     */
    private static void overridePropertiesWithSystemProperties(Properties properties) {
        checkNotNull(properties);
        Properties systemProperties = getSystemProperties();
        for(String systemPropertyName : systemProperties.stringPropertyNames()) {
            if(systemPropertyName.startsWith(MAIL_PROPERTIES_PREFIX)) {
                LOGGER.info("Overriding %s with system property", systemPropertyName);
                String propertyValue = systemProperties.getProperty(systemPropertyName);
                properties.setProperty(systemPropertyName, propertyValue);
            }
        }
    }

    private static Properties getSystemProperties() {
        try {
            return System.getProperties();
        } catch (SecurityException e) {
            LOGGER.severe(e);
            return new Properties();
        }
    }
}
