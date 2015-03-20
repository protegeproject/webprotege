package edu.stanford.bmir.protege.web.client.auth;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public interface SignInSuccessfulHandler {

    void handleLoginSuccessful(UserId userId);
}
