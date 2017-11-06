package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.filemanager.ConfigInputStreamSupplier;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.app.WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE_NAME;

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

    private static final Logger logger = LoggerFactory.getLogger(WebProtegePropertiesProvider.class);

    @Nonnull
    private final ConfigInputStreamSupplier configInputStreamSupplier;

    @Inject
    public WebProtegePropertiesProvider(@Nonnull ConfigInputStreamSupplier configInputStreamSupplier) {
        this.configInputStreamSupplier = checkNotNull(configInputStreamSupplier);
    }

    @Override
    public WebProtegeProperties get() {
        return loadProperties();
    }

    private WebProtegeProperties loadProperties() throws WebProtegeConfigurationException {
        try(InputStream bufferedInputStream = createInputStream()) {
            Properties properties = new Properties();
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
            overridePropertiesWithSystemProperties(properties);
            return new WebProtegeProperties(properties);
        } catch (IOException e) {
            throw new WebProtegeConfigurationException(
                    String.format("Could not read %s. Cause: %s",
                            WEB_PROTEGE_PROPERTIES_FILE_NAME,
                            e.getMessage()));
        }
    }

    private InputStream createInputStream() throws IOException {
        return configInputStreamSupplier.getConfigFileInputStream(WEB_PROTEGE_PROPERTIES_FILE_NAME);
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
                logger.info("Overriding {} with system property value: {}", systemPropertyName, value);
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
