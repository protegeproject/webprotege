package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Feb 2017
 */
public class ProjectPermissionValidator implements RequestValidator {

    private final AccessManager accessManager;

    private final ProjectId projectId;

    private final UserId userId;

    private final ActionId actionId;

    @Inject
    public ProjectPermissionValidator(AccessManager accessManager,
                                      ProjectId projectId,
                                      UserId userId,
                                      ActionId actionId) {
        this.accessManager = accessManager;
        this.projectId = projectId;
        this.userId = userId;
        this.actionId = actionId;
    }

    @Override
    public RequestValidationResult validateAction() {
        if(accessManager.hasPermission(forUser(userId), new ProjectResource(projectId), actionId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Permission denied for " + actionId.getId());
        }
    }
}
