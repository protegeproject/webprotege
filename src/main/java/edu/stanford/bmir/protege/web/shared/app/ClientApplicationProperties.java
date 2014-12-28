package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
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
public class ClientApplicationProperties implements IsSerializable {


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
