package edu.stanford.bmir.protege.web.server.app;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.*;

/**
 * Accessor methods for the webprotege.properties property values.  The set of properties is read-only.  This is
 * a server side class.
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Matthew Horridge
 */
public class WebProtegeProperties implements Serializable {

    private ImmutableMap<WebProtegePropertyName, Optional<String>> propertyValueMap;

    public static final String WEB_PROTEGE_PROPERTIES_FILE_NAME = "webprotege.properties";

    /**
     * Initialises WebProtegeProperties with property values from a {@link java.util.Properties} object.
     * @param properties A {@link Properties} object which contains the properties to be used in the initialization.
     * Not {@code null}.  Only property values whose property names are equal to the property names specified
     *                   by the {@link edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName} enum will
     *                   be read.
     * @throws NullPointerException if {@code properties} is {@code null}.
     * See the {@link edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName}
     * enum for a list of required properties (which do not have defaults).
     */
    public WebProtegeProperties(Properties properties) {
        checkNotNull(properties);
        ImmutableMap.Builder<WebProtegePropertyName, Optional<String>> builder = ImmutableMap.builder();
        for (WebProtegePropertyName propertyName : values()) {
            final String value = properties.getProperty(propertyName.getPropertyName(), null);
            if (value != null  && !isPlaceholder(propertyName, value)) {
                builder.put(propertyName, Optional.of(value));
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
     * Gets a property value as a string.
     * @param propertyName The name of the property whose value should be retrieved.  Not {@code null}.
     * @return Either the value of the specified property or the default value.  May be {@code null} if the property
     *         does not have a value and {@code defaultValue} is specified as {@code null}.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    private java.util.Optional<String> getOptionalString(WebProtegePropertyName propertyName) {
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


    public File getDataDirectory() {
        String dataDirectory = getRequiredString(DATA_DIRECTORY);
        return new File(dataDirectory);
    }

    @Nonnull
    public Optional<String> getDBPort() {
        return getOptionalString(MONGO_DB_PORT);
    }

    @Nonnull
    public Optional<String> getDBHost() {
        return getOptionalString(MONGO_DB_HOST);
    }

    public long getProjectDormantTime() {
        try {
            return Long.parseLong(getRequiredString(PROJECT_DORMANT_TIME));
        } catch(NumberFormatException e) {
            return Long.parseLong(PROJECT_DORMANT_TIME.getDefaultValue().orElseThrow());
        }
    }

    public Optional<String> getDBUserName() {
        return getOptionalString(MONGO_DB_AUTH_USERNAME);
    }

    public Optional<String> getDBPassword() {
        return getOptionalString(MONGO_DB_AUTH_PASSWORD);
    }

    public Optional<String> getDBAuthenticationSource() {
        return getOptionalString(MONGO_DB_AUTH_SOURCE);
    }

    public Optional<Integer> getEntityGraphEdgeLimit() {
        return getOptionalString(WebProtegePropertyName.ENTITY_GRAPH_EDGE_LIMIT)
                .map(edgeLimit -> {
                    try {
                        return Integer.parseInt(edgeLimit);
                    } catch(NumberFormatException e) {
                        return Integer.parseInt(ENTITY_GRAPH_EDGE_LIMIT.getDefaultValue().orElseThrow());
                    }
                });

    }
}
