package edu.stanford.bmir.protege.web.server.dispatch.validators;

import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 * <p>
 *     A validator which passes users that have write permission for project specific actions.
 * </p>
 */
public class WritePermissionValidator implements RequestValidator {

    private final PermissionChecker permissionChecker;

    private final ProjectId projectId;

    private final UserId userId;

    @Inject
    public WritePermissionValidator(@Assisted ProjectId projectId, @Assisted UserId userId, PermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
        this.projectId = projectId;
        this.userId = userId;
    }

    @Override
    public RequestValidationResult validateAction() {
        if(permissionChecker.hasWritePermission(projectId, userId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("You do not have permission to edit this project.");
        }
    }


}
