package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.shared.auth.AbstractAuthenticationResult;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticationResponse;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22 Dec 2017
 */
public abstract class AuthenticatedDispatchServiceCallback<R extends AbstractAuthenticationResult> extends DispatchServiceCallbackWithProgressDisplay<R> {

    public AuthenticatedDispatchServiceCallback(@Nonnull DispatchErrorMessageDisplay errorMessageDisplay,
                                                @Nonnull ProgressDisplay progressDisplay) {
        super(errorMessageDisplay, progressDisplay);
    }

    /**
     * Called to indicated whether authentication succeeded or not.  This is called if a successful response
     * is received.
     * @param authenticationResponse The authentication response
     */
    public abstract void handleAuthenticationResponse(@Nonnull AuthenticationResponse authenticationResponse);
}
