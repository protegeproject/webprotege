package edu.stanford.bmir.protege.web.server.dispatch.validators;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UserHasProjectReadPermissionValidator<A extends Action<?> & HasProjectId> implements RequestValidator<A> {

    private final PermissionChecker permissionChecker;

    @Inject
    public UserHasProjectReadPermissionValidator(PermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    public static <A extends Action<?> & HasProjectId> UserHasProjectReadPermissionValidator<A> get() {
        return new UserHasProjectReadPermissionValidator<A>();
    }

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        if(permissionChecker.hasReadPermission(action.getProjectId(), requestContext.getUserId())) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid(new PermissionDeniedException());
        }
    }
}
