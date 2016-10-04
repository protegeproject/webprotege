package edu.stanford.bmir.protege.web.server.dispatch.validators;

import javax.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class AdminPermissionValidator implements RequestValidator {

    private final ProjectId projectId;

    private final UserId userId;

    private final PermissionChecker permissionChecker;

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public AdminPermissionValidator(@Assisted ProjectId projectId, @Assisted UserId userId, PermissionChecker permissionChecker, ProjectDetailsManager projectDetailsManager) {
        this.projectId = projectId;
        this.userId = userId;
        this.permissionChecker = permissionChecker;
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public RequestValidationResult validateAction() {
        if(projectDetailsManager.isProjectOwner(userId, projectId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Only project admins may change project settings");
        }
    }
}
