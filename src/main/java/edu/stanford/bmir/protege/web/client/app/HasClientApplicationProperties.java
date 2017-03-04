package edu.stanford.bmir.protege.web.client.app;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/12/15
 */
public interface HasClientApplicationProperties {

    Optional<String> getClientApplicationProperty(WebProtegePropertyName propertyName);

    /**
     * Gets a client application property value. Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The property name.  Not {@code null}.
     * @param defaultValue The value that should be returned if the specified application is not present.  May be {@code null}.
     * @return The value of the property if present, or the default value.  If the value of the property is present then
     * a non-{@code null} value will be returned.  If the value of the property is not present then whether or not
     * a {@code null} value is returned depeneds upon the default value.
     */
    boolean getClientApplicationProperty(WebProtegePropertyName propertyName, boolean defaultValue);
}
