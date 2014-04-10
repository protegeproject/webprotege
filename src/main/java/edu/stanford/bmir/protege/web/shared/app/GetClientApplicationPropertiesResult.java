package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */
public class GetClientApplicationPropertiesResult implements Result {

    private ClientApplicationProperties clientApplicationProperties;

    /**
     * For serialization purposes only
     */
    private GetClientApplicationPropertiesResult() {
    }

    /**
     * Constructs a {@link GetClientApplicationPropertiesResult} containing the specified properties.
     * @param clientApplicationProperties The properties.  Not {@code null}.
     * @throws NullPointerException if {@code clientApplicationProperties} is {@code null}.
     */
    public GetClientApplicationPropertiesResult(ClientApplicationProperties clientApplicationProperties) {
        this.clientApplicationProperties = checkNotNull(clientApplicationProperties);
    }

    /**
     * Gets the properties.
     * @return The properties.  Not {@code null}.
     */
    public ClientApplicationProperties getClientApplicationProperties() {
        return clientApplicationProperties;
    }
}
