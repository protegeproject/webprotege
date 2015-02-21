package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class UserIsSignedInValidator<A extends Action<?>> implements RequestValidator<A> {

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        if(requestContext.getUserId().isGuest()) {
            return RequestValidationResult.getInvalid("You must be signed in to perform this operation.  Please sign in");
        }
        else {
            return RequestValidationResult.getValid();
        }
    }

    public static <A extends Action<?>> RequestValidator<A> get() {
        return new UserIsSignedInValidator<>();
    }
}
