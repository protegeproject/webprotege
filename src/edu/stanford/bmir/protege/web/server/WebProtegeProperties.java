package edu.stanford.bmir.protege.web.server;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.WebProtegeProperties.PropertyName.*;

/**
 * Accessor methods for the webprotege.properties property values.  The set of properties is read-only.
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Matthew Horridge
 */
public class WebProtegeProperties implements Serializable {


    public static enum Optionality {

        HAS_DEFAULT_VALUE,

        VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES
    }

    public static enum ClientVisibility {

        VISIBLE,

        HIDDEN
    }


    private static class PropertyValue {

        private String value;

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


    /**
     * A list of property names and property values that are understood by WebProtege.  Each property may be optional
     * or required.  Required properties must have a value specified in the webprotege.properties file.  Optional
     * properties
     * do not need a value specified in the webprotege.properties file.  If optional then a default value must be
     * specified
     * here for the property.
     *
     * In general the property names listed here should correspond to the property names specified in the
     * webprotege.properties file with the dots replaced by underscores and the name coverted to upper case.  For
     * example, data.directory becomes DATA_DIRECTORY
     */
    public static enum PropertyName {

        @WebProtegePropertiesDocumentation(description = "The name of the webprotege application that appears in the browser title bar", example = "WebProtege")
        APPLICATION_NAME("application.name", PropertyValue.ofString("WebProt\u00E9g\u00E9"), ClientVisibility.VISIBLE),

        @WebProtegePropertiesDocumentation(description = "The host name that webprotege runs on", example = "webprotege.stanford.edu")
        APPLICATION_HOST("application.host", ClientVisibility.VISIBLE),

        @WebProtegePropertiesDocumentation(description = "The directory where webprotege data is stored", example = "/src/webprotege")
        DATA_DIRECTORY("data.directory", ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "The host name of the mongodb server", example = "localhost")
        MONGO_DB_HOST("mongodb.host", PropertyValue.ofString("localhost"), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "The port number of the mongodb server", example = "27017")
        MONGO_DB_PORT("mongodb.port", PropertyValue.ofInteger(27107), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "The host name of the email smtp server", example = "smtp.gmail.com")
        EMAIL_HOST("email.host", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "The smtp port number", example = "465")
        EMAIL_PORT("email.port", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "The email account that webprotege should use to send email notifications", example = "john.doe@stanford.edu")
        EMAIL_ACCOUNT("email.account", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "The password for the email account that webprotege should use to send email notifications")
        EMAIL_PASSWORD("email.password", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "Specifies whether webprotege uses https rather than http as a protocol", example = "false")
        HTTPS_ENABLED("https.enabled", PropertyValue.ofBoolean(false), ClientVisibility.VISIBLE),

        @WebProtegePropertiesDocumentation(description = "Specifies the port used for https communication", example = "443")
        HTTPS_PORT("https.port", PropertyValue.ofInteger(443), ClientVisibility.VISIBLE),

        @WebProtegePropertiesDocumentation(description = "The email address of the webprotege administrator", example = "john.doe@stanford.edu")
        ADMIN_EMAIL("admin.email", PropertyValue.absentByDefault(), ClientVisibility.HIDDEN),

        @WebProtegePropertiesDocumentation(description = "Specifies whether or not WebProtege should support authentication with Open Id", example = "false")
        OPEN_ID_ENABLED("openid.enabled", PropertyValue.ofBoolean(true), ClientVisibility.VISIBLE);



        private String propertyName;

        private Optionality optionality;

        private Optional<String> defaultValue;

        private ClientVisibility clientVisibility;


        private PropertyName(String propertyName, ClientVisibility clientVisibility) {
            this.propertyName = checkNotNull(propertyName);
            defaultValue = Optional.absent();
            this.optionality = Optionality.VALUE_MUST_BE_SPECIFIED_IN_PROTEGE_PROPERTIES;
            this.clientVisibility = clientVisibility;

        }

        private PropertyName(String propertyName, PropertyValue defaultValue, ClientVisibility clientVisibility) {
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

    private static ImmutableMap<PropertyName, Optional<String>> propertyValueMap;


    public static final String WEB_PROTEGE_PROPERTIES_FILE_NAME = "webprotege.properties";

    /**
     * Initialises the {@link WebProtegeProperties} object with property values from the specified {@link Properties}
     * object.
     * @param properties The properties object which should be used for initialization.  Not {@code null}.  Only property
     * values whose property names are equal to the property names specified by the {@link PropertyName} enum will be
     * read.
     * @throws WebProtegeConfigurationException If one of the expected property values listed in
     * {@link edu.stanford.bmir.protege.web.server.WebProtegeProperties.PropertyName#values()} does not have a default
     * value and does not have a value specified in {@code properties}.
     *
     */
    public static void initFromProperties(Properties properties) throws WebProtegeConfigurationException {
        if (propertyValueMap != null) {
            throw new IllegalStateException("WebProtegeProperties has already been initialized");
        }
        ImmutableMap.Builder<PropertyName, Optional<String>> builder = ImmutableMap.builder();
        for (PropertyName propertyName : values()) {
            final String value = properties.getProperty(propertyName.getPropertyName(), null);
            if (value != null) {
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
     * Gets a property value as a string.
     * @param propertyName The name of the property whose value should be retrieved.  Not {@code null}.
     * @return Either the value of the specified property or the default value.  May be {@code null} if the property
     *         does not have a value and {@code defaultValue} is specified as {@code null}.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    private static Optional<String> getOptionalString(PropertyName propertyName) {
        return propertyValueMap.get(checkNotNull(propertyName));
    }


    private static String getRequiredString(PropertyName propertyName) {
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


    public static String getApplicationName() {
        return getRequiredString(APPLICATION_NAME);
    }

    public static File getDataDirectory() {
        String dataDirectory = getRequiredString(DATA_DIRECTORY);
        return new File(dataDirectory);
    }

    public static Optional<String> getEmailHostName() {
        return getOptionalString(EMAIL_HOST);
    }

    public static Optional<String> getEmailPort() {
        return getOptionalString(EMAIL_PORT);
    }

    public static Optional<String> getEmailAccount() {
        return getOptionalString(EMAIL_ACCOUNT);
    }

    public static Optional<String> getEmailPassword() {
        return getOptionalString(EMAIL_PASSWORD);
    }

    public static boolean isLoginWithHttps() {
        Optional<String> value = getOptionalString(HTTPS_ENABLED);
        return value.isPresent() && Boolean.getBoolean(value.get());
    }

    public static int getHttpsPort() {
        String value = getRequiredString(HTTPS_PORT);
        return Integer.parseInt(value);
    }

    public static boolean isOpenIdAuthenticationEnabled() {
        return "true".equals(getRequiredString(OPEN_ID_ENABLED));
    }

    public static Optional<String> getAdministratorEmail() {
        return getOptionalString(ADMIN_EMAIL);
    }


    public static String getSslFactory() {
        return "javax.net.ssl.SSLSocketFactory";
//        return edu.stanford.smi.protege.util.ApplicationProperties.getString(ApplicationPropertyNames.EMAIL_SSL_FACTORY_PROP, "javax.net.ssl.SSLSocketFactory");
    }


    public static int getAccountInvitationExpirationPeriodInDays() {
        return Integer.MAX_VALUE;
    }


    public static Map<String, String> getClientMap() {
        Map<String, String> result = new HashMap<String, String>();
        for (PropertyName propertyName : propertyValueMap.keySet()) {
            if (propertyName.isClientProperty()) {
                Optional<String> value = propertyValueMap.get(propertyName);
                if (value.isPresent()) {
                    result.put(propertyName.getPropertyName(), value.get());
                }
            }
        }
        return result;

    }

}
