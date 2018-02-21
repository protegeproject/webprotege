package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public abstract class AbstractAuthenticationResult implements Result {

    private AuthenticationResponse response;

    /**
     * For serialization only
     */
    protected AbstractAuthenticationResult() {
    }

    public AbstractAuthenticationResult(AuthenticationResponse response) {
        this.response = checkNotNull(response);
    }

    public AuthenticationResponse getResponse() {
        return response;
    }
}
