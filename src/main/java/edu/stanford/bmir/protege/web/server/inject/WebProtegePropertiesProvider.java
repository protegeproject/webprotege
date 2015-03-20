package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class WebProtegePropertiesProvider implements Provider<WebProtegeProperties> {

    /**
     * A prefix for properties specified on the command line or via environment variables
     */
    private static final String SYSTEM_PROPERTY_PREFIX = "webprotege.";

    private final Logger logger;

    @Inject
    public WebProtegePropertiesProvider() {
        logger = Logger.getLogger("WebProtegeProperties");
    }

    @Override
    public WebProtegeProperties get() {
        return loadProperties();
    }

    private WebProtegeProperties loadProperties() throws WebProtegeConfigurationException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE_NAME);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        Properties properties = new Properties();
        try {
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
        } catch (IOException e) {
            throw new WebProtegeConfigurationException(
                    String.format("Could not read %s from class path. Message: %s",
                            WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE_NAME,
                            e.getMessage()));
        }
        overridePropertiesWithSystemProperties(properties);
        return new WebProtegeProperties(properties);
    }

    /**
     * Overrides any property values in the specified {@link Properties} object with property values that are specified
     * via the command line (with a -D argument) or via environment variables.
     * @param properties The properties object whose property values should be replaced.  Not {@code null}.
     */
    private void overridePropertiesWithSystemProperties(Properties properties) {
        checkNotNull(properties);
        for(WebProtegePropertyName propertyName : WebProtegePropertyName.values()) {
            String systemPropertyName = SYSTEM_PROPERTY_PREFIX + propertyName.getPropertyName();
            String value = getSystemProperty(systemPropertyName);
            if(value != null) {
                properties.setProperty(propertyName.getPropertyName(), value);
                logger.info("WebProtege configuration (using system variable): " + systemPropertyName + " = " + value);
            }
        }
    }

    private  String getSystemProperty(String property) {
        String value = null;
        try {
            value = System.getProperty(property, null);
            if (value == null) {
                value = System.getenv(property);
            }
        } catch (SecurityException e) {
            logger.info("Cannot access system or environment variable: " + property + " Message: " + e.getMessage());
        }
        return value;
    }

}
