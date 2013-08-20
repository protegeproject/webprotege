package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
        UserId userId = requestContext.getUserId();
        ProjectId projectId = action.getProjectId();
        // TODO
        return RequestValidationResult.getValid();
    }
}
