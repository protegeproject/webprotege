package edu.stanford.bmir.protege.web.server.dispatch.validators;

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
 * Date: 8/19/13
 */
public class UserHasProjectAdminPermissionValidator<A extends Action<R> & HasProjectId, R extends Result> implements RequestValidator<A> {

    @SuppressWarnings("unchecked")
    public static <A extends Action<? extends Result> & HasProjectId> UserHasProjectAdminPermissionValidator<A, ?> get() {
        return new UserHasProjectAdminPermissionValidator();
    }

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        // TODO
        return RequestValidationResult.getValid();
    }
}
