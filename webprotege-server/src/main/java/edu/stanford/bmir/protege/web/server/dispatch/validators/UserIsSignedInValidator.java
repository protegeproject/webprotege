package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class UserIsSignedInValidator implements RequestValidator {

    private final UserId userId;

    public UserIsSignedInValidator(UserId userId) {
        this.userId = userId;
    }

    @Override
    public RequestValidationResult validateAction() {
        if(userId.isGuest()) {
            return RequestValidationResult.getInvalid("You must be signed in to perform this operation.  Please sign in.");
        }
        else {
            return RequestValidationResult.getValid();
        }
    }

    public static RequestValidator get(UserId userId) {
        return new UserIsSignedInValidator(userId);
    }
}
