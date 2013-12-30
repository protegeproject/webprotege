package edu.stanford.bmir.protege.web.shared.app;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.WebProtegePropertiesDocumentation;


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

    @WebProtegePropertiesDocumentation(description = "The name of the webprotege application that appears in the browser title bar", example = "WebProtege")
    APPLICATION_NAME("application.name", PropertyValue.ofString("WebProt\u00E9g\u00E9"), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The host name that webprotege runs on", example = "webprotege.stanford.edu")
    APPLICATION_HOST("application.host", ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The directory where webprotege data is stored", example = "/src/webprotege")
    DATA_DIRECTORY("data.directory", ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "The host name of the mongodb server", example = "localhost")
    MONGO_DB_HOST("mongodb.host", PropertyValue.ofString("localhost"), ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "The port number of the mongodb server", example = "27017")
    MONGO_DB_PORT("mongodb.port", PropertyValue.ofInteger(27017), ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "Specifies whether webprotege uses https rather than http as a protocol", example = "false")
    HTTPS_ENABLED("https.enabled", PropertyValue.ofBoolean(false), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "Specifies the port used for https communication", example = "443")
    HTTPS_PORT("https.port", PropertyValue.ofInteger(443), ClientVisibility.VISIBLE),

    @WebProtegePropertiesDocumentation(description = "The email address of the webprotege administrator", example = "john.doe@stanford.edu")
    ADMIN_EMAIL("admin.email", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

    @WebProtegePropertiesDocumentation(description = "Specifies whether or not WebProtege should support authentication with Open Id", example = "false")
    OPEN_ID_ENABLED("openid.enabled", PropertyValue.ofBoolean(true), ClientVisibility.VISIBLE);


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
            return Optional.fromNullable(value);
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

    private Optional<String> defaultValue;

    private ClientVisibility clientVisibility;


    private WebProtegePropertyName(String propertyName, ClientVisibility clientVisibility) {
        this.propertyName = checkNotNull(propertyName);
        defaultValue = Optional.absent();
        this.optionality = Optionality.VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES;
        this.clientVisibility = clientVisibility;

    }

    private WebProtegePropertyName(String propertyName, PropertyValue defaultValue, ClientVisibility clientVisibility) {
        this.propertyName = checkNotNull(propertyName);
        this.defaultValue = defaultValue.toOptional();
        optionality = Optionality.HAS_DEFAULT_VALUE;
        this.clientVisibility = clientVisibility;

    }

    public String getPropertyName() {
        return propertyName;
    }

    public Optional<String> getDefaultValue() {
        return defaultValue;
    }

    public boolean hasDefaultValue() {
        return optionality == Optionality.HAS_DEFAULT_VALUE;
    }

    public boolean isClientProperty() {
        return clientVisibility == ClientVisibility.VISIBLE;
    }
}
