package edu.stanford.bmir.protege.web.server.dispatch.validators;


import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class UserIsProjectOwnerValidator<A extends HasProjectAction<?>> implements RequestValidator<A> {

    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public UserIsProjectOwnerValidator(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        boolean projectOwner = projectDetailsManager.isProjectOwner(requestContext.getUserId(), action.getProjectId());
        if(projectOwner) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Must be project owner");
        }
    }
}
