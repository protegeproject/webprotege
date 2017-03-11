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
 *
 * Finally, some of the property values will not be visible to clients (in the browser).  This information is encoded
 * by the {@link ClientVisibility} flag.
 */
public enum WebProtegePropertyName {

    @WebProtegePropertiesDocumentation(description = "The name of the WebProtégé application that appears in the browser title bar", example = "WebProtege")
    APPLICATION_NAME("application.name", PropertyValue.ofString("WebProt\u00E9g\u00E9"), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The version of the WebProtégé application", example = "2.5.0")
    APPLICATION_VERSION("application.version", ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The host name that WebProtégé runs on", example = "webprotege.stanford.edu")
    APPLICATION_HOST("application.host", ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "Specifies the port used to access WebProtégé as an int", example = "443")
    APPLICATION_PORT("application.port", PropertyValue.absentByDefault(), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "Specifies the path used to access WebProtégé.  Must start with a /.", example = "/webprotege")
    APPLICATION_PATH("application.path", PropertyValue.absentByDefault(), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The directory where WebProtégé data is stored", example = "/src/webprotege")
    DATA_DIRECTORY("data.directory", ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "The host name of the mongodb server", example = "localhost")
    MONGO_DB_HOST("mongodb.host", PropertyValue.ofString("localhost"), ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "The port number of the mongodb server", example = "27017")
    MONGO_DB_PORT("mongodb.port", PropertyValue.ofInteger(27017), ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "Specifies the scheme used to access WebProtégé", example = "http or https")
    APPLICATION_SCHEME("application.scheme", PropertyValue.ofString("http"), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The email address of the WebProtégé administrator", example = "john.doe@stanford.edu")
    ADMIN_EMAIL("admin.email", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "Specifies whether or not WebProtégé should support authentication with Open Id", example = "false")
    OPEN_ID_ENABLED("openid.enabled", PropertyValue.ofBoolean(true), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "Specifies whether or not users should be allowed to sign up for accounts", example = "false")
    USER_ACCOUNT_CREATION_ENABLED("user.account.creation.enabled", PropertyValue.ofBoolean(true), ClientVisibility.VISIBLE);


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


    public static enum Optionality {

        HAS_DEFAULT_VALUE,

        VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES
    }

    public static enum ClientVisibility {

        VISIBLE,

        HIDDEN
    }


    private String propertyName;

    private Optionality optionality;

    @Nullable
    private String defaultValue;

    private ClientVisibility clientVisibility;


    private WebProtegePropertyName(String propertyName, ClientVisibility clientVisibility) {
        this.propertyName = checkNotNull(propertyName);
        defaultValue = null;
        this.optionality = Optionality.VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES;
        this.clientVisibility = clientVisibility;

    }

    private WebProtegePropertyName(String propertyName, PropertyValue defaultValue, ClientVisibility clientVisibility) {
        this.propertyName = checkNotNull(propertyName);
        this.defaultValue = defaultValue.toOptional().orElse(null);
        optionality = Optionality.HAS_DEFAULT_VALUE;
        this.clientVisibility = clientVisibility;

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

    public boolean isClientProperty() {
        return clientVisibility == ClientVisibility.VISIBLE;
    }
}
