package edu.stanford.bmir.protege.web.server.dispatch.validators;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class ReadPermissionValidator implements RequestValidator {

    private final ProjectId projectId;

    private final UserId userId;

    private final PermissionChecker permissionChecker;

    @Inject
    public ReadPermissionValidator(@Assisted ProjectId projectId, @Assisted UserId userId, PermissionChecker permissionChecker) {
        this.projectId = projectId;
        this.userId = userId;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public RequestValidationResult validateAction() {
        if(permissionChecker.hasReadPermission(projectId, userId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid(new PermissionDeniedException());
        }
    }
}
