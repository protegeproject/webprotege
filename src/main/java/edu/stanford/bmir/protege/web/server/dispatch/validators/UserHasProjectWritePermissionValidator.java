package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 * <p>
 *     A validator which passes users that have write permission for project specific actions.
 * </p>
 */
public class UserHasProjectWritePermissionValidator<A extends Action<?> & HasProjectId> implements RequestValidator<A> {

    public static <A extends Action<?> & HasProjectId> UserHasProjectWritePermissionValidator<A> get() {
        return new UserHasProjectWritePermissionValidator<A>();
    }

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        if(requestContext.getUserId().isGuest()) {
            return RequestValidationResult.getInvalid(new NotSignedInException());
        }
        return RequestValidationResult.getValid();
    }


}
