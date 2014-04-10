package edu.stanford.bmir.protege.web.server.app;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.*;

/**
 * Accessor methods for the webprotege.properties property values.  The set of properties is read-only.  This is
 * a server side class.  Client properties may be obtained using the {@link #getClientApplicationProperties()} method.
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Matthew Horridge
 */
public class WebProtegeProperties implements Serializable {



    private static WebProtegeProperties instance;

    private ImmutableMap<WebProtegePropertyName, Optional<String>> propertyValueMap;

    public static final String WEB_PROTEGE_PROPERTIES_FILE_NAME = "webprotege.properties";


    /**
     * Internal constructor to initialize properties.
     * @param properties A {@link Properties} object which contains the properties to be used in the initialization.
     * Not {@code null}.
     * @throws NullPointerException if {@code properties} is {@code null}.
     * @throws WebProtegeConfigurationException if required property values are missing.  See the {@link edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName}
     * enum for a list of required properties (which do not have defaults).
     */
    private WebProtegeProperties(final Properties properties) {
        checkNotNull(properties);
        ImmutableMap.Builder<WebProtegePropertyName, Optional<String>> builder = ImmutableMap.builder();
        for (WebProtegePropertyName propertyName : values()) {
            final String value = properties.getProperty(propertyName.getPropertyName(), null);
            if (value != null  && !isPlaceholder(propertyName, value)) {
                builder.put(propertyName, Optional.<String>of(value));
            }
            else {
                if (propertyName.hasDefaultValue()) {
                    builder.put(propertyName, propertyName.getDefaultValue());
                }
                else {
                    throw new WebProtegeConfigurationException("Property " + propertyName.getPropertyName() +
                            " does not have a default value and no value has been specified in the " + WEB_PROTEGE_PROPERTIES_FILE_NAME +
                            " file, or as a Java argument, or environment variable." +
                            " To fix this error, you may: " +
                            "(1) Specify a value for this property in the " + WEB_PROTEGE_PROPERTIES_FILE_NAME + " file; or " +
                            "(2) Add the property as a Java argument when starting your servlet container (-Dwebprotege." + propertyName.getPropertyName() + "=your_value); or " +
                            "(3) Add the property as an environment variable (webprotege." + propertyName.getPropertyName() + "=your_value).");
                }
            }
        }
        propertyValueMap = builder.build();
    }

    /**
     * Determines whether or not the value of the property is a placeholder for the property.  The value is a placeholder
     * if for a given property name, propname it is equal to "${propname}".
     * @param propertyName The name of the property.
     * @param value The value.
     * @return {@code true} if the value of the property is a placeholder of the property value, otherwise {@code false}.
     */
    private static boolean isPlaceholder(WebProtegePropertyName propertyName, String value) {
        return ("${" + propertyName.name() + "}").equals(value);
    }

    /**
     * Gets the one and only instance of {@link WebProtegeProperties}.
     * @return The singleton instance of {@link WebProtegeProperties}.  Not {@code null}.
     */
    public static WebProtegeProperties get() {
        if(instance == null) {
            throw new IllegalStateException("WebProtegeProperties has not been initialized.  WebProtegeProperties.initFromProperties(Properties) must be called ONCE by some initializer.");
        }
        return instance;
    }

    /**
     * Initialises the {@link WebProtegeProperties} object with property values from the specified {@link Properties}
     * object.  This method should only be called once by some initial setup for the application.
     * @param properties The properties object which should be used for initialization.  Not {@code null}.  Only property
     * values whose property names are equal to the property names specified by the {@link edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName} enum will be
     * read.
     * @throws WebProtegeConfigurationException If one of the expected property values listed in
     * {@link edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName#values()} does not have a default
     * value and does not have a value specified in {@code properties}.
     *
     */
    public static void initFromProperties(Properties properties) throws WebProtegeConfigurationException {
        if (instance != null) {
            throw new IllegalStateException("WebProtegeProperties has already been initialized");
        }
        instance = new WebProtegeProperties(properties);
    }

    /**
     * Gets a property value as a string.
     * @param propertyName The name of the property whose value should be retrieved.  Not {@code null}.
     * @return Either the value of the specified property or the default value.  May be {@code null} if the property
     *         does not have a value and {@code defaultValue} is specified as {@code null}.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    private Optional<String> getOptionalString(WebProtegePropertyName propertyName) {
        return propertyValueMap.get(checkNotNull(propertyName));
    }


    private String getRequiredString(WebProtegePropertyName propertyName) {
        Optional<String> value = propertyValueMap.get(propertyName);
        if (!value.isPresent()) {
            throw new RuntimeException("value is not present for required property value");
        }
        return value.get();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Public interface
    ////


    public String getApplicationName() {
        return getRequiredString(APPLICATION_NAME);
    }

    public File getDataDirectory() {
        String dataDirectory = getRequiredString(DATA_DIRECTORY);
        return new File(dataDirectory);
    }

    public String getApplicationHostName() {
        return getRequiredString(APPLICATION_HOST);
    }

    public  boolean isOpenIdAuthenticationEnabled() {
        return "true".equals(getRequiredString(OPEN_ID_ENABLED));
    }

    public  Optional<String> getAdministratorEmail() {
        return getOptionalString(ADMIN_EMAIL);
    }
    
    public Optional<String> getDBPort() {
        return getOptionalString(MONGO_DB_PORT);
    }

    public Optional<String> getDBHost() {
        return getOptionalString(MONGO_DB_HOST);
    }




    public  int getAccountInvitationExpirationPeriodInDays() {
        return Integer.MAX_VALUE;
    }

    /**
     * Gets the application properties that are visible to the client.  This is a subset of the application properties
     * that are visible to the server.
     * @return The client visible application properties.  Not {@code null}.
     */
    public ClientApplicationProperties getClientApplicationProperties() {
        ClientApplicationProperties.Builder builder = new ClientApplicationProperties.Builder();
        for (WebProtegePropertyName propertyName : propertyValueMap.keySet()) {
            if (propertyName.isClientProperty()) {
                Optional<String> value = propertyValueMap.get(propertyName);
                if (value.isPresent()) {
                    builder.setPropertyValue(propertyName, value.get());
                }
            }
        }
        return builder.build();
    }


}
