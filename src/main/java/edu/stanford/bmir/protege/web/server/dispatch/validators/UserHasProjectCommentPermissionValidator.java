package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UserHasProjectCommentPermissionValidator<A extends HasProjectAction<R>, R extends Result> implements RequestValidator<A> {

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        if(requestContext.getUserId().isGuest()) {
           return RequestValidationResult.getInvalid(new NotSignedInException());
        }
        return RequestValidationResult.getValid();
    }
}
