package edu.stanford.bmir.protege.web.shared.app;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */

import edu.stanford.bmir.protege.web.server.WebProtegePropertiesDocumentation;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A list of property names and property values that are understood by WebProtege.  Each property may be optional
 * or required.  Required properties must have a value specified in the server size webprotege.properties file.
 * Optional properties do not need a value specified in the webprotege.properties file.  If optional then a default
 * value must be specified here for the property.
 *
 * In general the property names listed here should correspond to the property names specified in the
 * webprotege.properties file with the dots replaced by underscores and the name coverted to upper case.  For
 * example, data.directory becomes DATA_DIRECTORY.
 */
public enum WebProtegePropertyName {

    @WebProtegePropertiesDocumentation(description = "The version of the WebProtégé application", example = "2.5.0")
    APPLICATION_VERSION("application.version"),

    @WebProtegePropertiesDocumentation(description = "The directory where WebProtégé data is stored", example = "/src/webprotege")
    DATA_DIRECTORY("data.directory"),

    @WebProtegePropertiesDocumentation(description = "The host name of the mongodb server", example = "localhost")
    MONGO_DB_HOST("mongodb.host", PropertyValue.ofString("localhost")),

    @WebProtegePropertiesDocumentation(description = "The port number of the mongodb server", example = "27017")
    MONGO_DB_PORT("mongodb.port", PropertyValue.ofInteger(27017));

    private static class PropertyValue {

        private String value;

        /**
         * For serialization purposes only
         */
        private PropertyValue() {
        }

        private PropertyValue(String value) {
            this.value = value;
        }

        public static PropertyValue ofString(String value) {
            return new PropertyValue(value);
        }

        public static PropertyValue ofInteger(int value) {
            return new PropertyValue(Integer.toString(value));
        }

        public static PropertyValue ofBoolean(boolean b) {
            return new PropertyValue(Boolean.toString(b));
        }

        public static PropertyValue absentByDefault() {
            return new PropertyValue(null);
        }

        public Optional<String> toOptional() {
            return Optional.ofNullable(value);
        }
    }


    public enum Optionality {

        HAS_DEFAULT_VALUE,

        VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES
    }

    public enum ClientVisibility {

        VISIBLE,

        HIDDEN
    }


    private String propertyName;

    private Optionality optionality;

    @Nullable
    private String defaultValue;


    WebProtegePropertyName(String propertyName) {
        this.propertyName = checkNotNull(propertyName);
        defaultValue = null;
        this.optionality = Optionality.VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES;

    }

    WebProtegePropertyName(String propertyName, PropertyValue defaultValue) {
        this.propertyName = checkNotNull(propertyName);
        this.defaultValue = defaultValue.toOptional().orElse(null);
        optionality = Optionality.HAS_DEFAULT_VALUE;

    }

    public String getPropertyName() {
        return propertyName;
    }

    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public boolean hasDefaultValue() {
        return optionality == Optionality.HAS_DEFAULT_VALUE;
    }
}
