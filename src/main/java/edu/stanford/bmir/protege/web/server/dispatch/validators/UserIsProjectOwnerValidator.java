package edu.stanford.bmir.protege.web.server.dispatch.validators;


import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class UserIsProjectOwnerValidator implements RequestValidator {

    private ProjectDetailsManager projectDetailsManager;

    private UserId userId;

    private ProjectId projectId;

    @Inject
    public UserIsProjectOwnerValidator(@Assisted ProjectId projectId, @Assisted UserId userId, ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
        this.userId = userId;
        this.projectId = projectId;
    }

    @Override
    public RequestValidationResult validateAction() {
        boolean projectOwner = projectDetailsManager.isProjectOwner(userId, projectId);
        if(projectOwner) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Only project owners can perform the requested operation.");
        }
    }
}
