package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public interface AuthenticationActionFactory<A extends AbstractAuthenticationAction<R>, R extends AbstractAuthenticationResult> {

    A createAction(ChapSessionId sessionId, UserId userId, ChapResponse chapResponse);
}
