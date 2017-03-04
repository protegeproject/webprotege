package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.client.app.HasClientApplicationProperties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */
public class ClientApplicationProperties implements IsSerializable, HasClientApplicationProperties {


    private Map<WebProtegePropertyName, String> propertyMap;

    /**
     * For serialization purposes only
     */
    private ClientApplicationProperties() {

    }

    private ClientApplicationProperties(Map<WebProtegePropertyName, String> properties) {
        propertyMap = new HashMap<WebProtegePropertyName, String>(properties);
    }

    /**
     * Get the value for the specified property.
     * @param propertyName The property name that identifies the property whose value is to be retrieved.
     * @return An optional value for the property.  Not {@code null}.
     */
    public Optional<String> getPropertyValue(WebProtegePropertyName propertyName) {
        return Optional.fromNullable(propertyMap.get(checkNotNull(propertyName)));
    }

    /**
     * Gets a client application property. Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The name of the property.  Not {@code null}.
     * @return The optional value of the property.  Not {@code null}.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    public Optional<String> getClientApplicationProperty(WebProtegePropertyName propertyName) {
        return getPropertyValue(propertyName);
    }

    /**
     * Gets a client application property value. Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param protegePropertyName The property name.  Not {@code null}.
     * @param defaultValue The value that should be returned if the specified application is not present.  May be {@code null}.
     * @return The value of the property if present, or the default value.  If the value of the property is present then
     * a non-{@code null} value will be returned.  If the value of the property is not present then whether or not
     * a {@code null} value is returned depeneds upon the default value.
     */
    public String getClientApplicationProperty(WebProtegePropertyName protegePropertyName, String defaultValue) {
        Optional<String> value = getClientApplicationProperty(protegePropertyName);
        if(value.isPresent()) {
            return value.get();
        }
        else {
            return defaultValue;
        }
    }

    /**
     * Gets the specified client application property value as a boolean.  Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The property name.  Not {@code null}.
     * @param defaultValue A default value for the property in case it does not exist or the property value cannot be
     * parsed into a boolean.
     * @return The property value.  Not {@code null}.  If the property does not exist then the value of {@code false}
     * will be returned.
     */
    public boolean getClientApplicationProperty(WebProtegePropertyName propertyName, boolean defaultValue) {
        Optional<String> propertyValue = getClientApplicationProperty(propertyName);
        try {
            return Boolean.parseBoolean(propertyValue.or(Boolean.toString(defaultValue)));
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the specified client application property value as an integer.  Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The property name.  Not {@code null}.
     * @param defaultValue A default value for the property incase it does not exist or the property value cannot be
     * parsed into an integer.
     * @return The property value.  Not {@code null}.
     */
    public int getClientApplicationProperty(WebProtegePropertyName propertyName, int defaultValue) {
        Optional<String> propertyValue = getClientApplicationProperty(propertyName);
        if(!propertyValue.isPresent()) {
            return defaultValue;
        }
        else {
            try {
                return Integer.parseInt(propertyValue.get());
            }
            catch (NumberFormatException e) {
                com.google.gwt.core.shared.GWT.log("NumberFormatException while parsing " + propertyValue.get() + " as an integer.");
                return defaultValue;
            }
        }
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Iterator<WebProtegePropertyName> it = propertyMap.keySet().iterator() ; it.hasNext() ;) {
            WebProtegePropertyName propertyName = it.next();
            sb.append("\"");
            sb.append(propertyName.getPropertyName());
            sb.append("\" : \"");
            sb.append(propertyMap.get(propertyName));
            sb.append("\"");
            if(it.hasNext()) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("ClientApplicationProperties")
                .addValue(propertyMap)
                .toString();
    }

    public static class Builder {

        private Map<WebProtegePropertyName, String> propertyMap = new HashMap<WebProtegePropertyName, String>();

        /**
         * Sets the value for the specified property.
         * @param propertyName The property name.  Not {@code null}.
         * @param propertyValue The value.  Not {@code null}.
         * @throws NullPointerException if either {@code propertyName} or {@code propertyValue} are {@code null}.
         */
        public void setPropertyValue(WebProtegePropertyName propertyName, String propertyValue) {
            propertyMap.put(checkNotNull(propertyName), checkNotNull(propertyValue));
        }

        /**
         * Removes a the property values for the specified property
         * @param propertyName The name that identifies the property.  Not {@code null}.
         * @throws NullPointerException if {@code propertyName} is {@code null}.
         */
        public void clearPropertyValue(WebProtegePropertyName propertyName) {
            propertyMap.remove(checkNotNull(propertyName));
        }

        /**
         * Builds a ClientApplicationProperties map from the property values contained in this builder.
         * @return The ClientApplicationProperties with the property values set.
         */
        public ClientApplicationProperties build() {
            return new ClientApplicationProperties(propertyMap);
        }
    }
}
