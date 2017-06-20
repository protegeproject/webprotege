package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
public class GetProjectDetailsActionHandler implements ApplicationActionHandler<GetProjectDetailsAction, GetProjectDetailsResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetProjectDetailsActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
    }

    @Override
    public Class<GetProjectDetailsAction> getActionClass() {
        return GetProjectDetailsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetProjectDetailsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetProjectDetailsResult execute(GetProjectDetailsAction action, ExecutionContext executionContext) {
        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(action.getProjectId());
        return new GetProjectDetailsResult(projectDetails);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectDetailsActionHandler")
                .toString();
    }
}
