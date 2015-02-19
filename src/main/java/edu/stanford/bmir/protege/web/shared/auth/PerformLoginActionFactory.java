package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class PerformLoginActionFactory implements AuthenticationActionFactory<PerformLoginAction, PerformLoginResult> {

    @Override
    public PerformLoginAction createAction(ChapSessionId sessionId, UserId userId, ChapResponse chapResponse) {
        return new PerformLoginAction(userId, sessionId, chapResponse);
    }
}
