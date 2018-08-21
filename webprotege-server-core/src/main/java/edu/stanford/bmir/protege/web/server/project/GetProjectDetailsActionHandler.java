package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import javax.annotation.Nonnull;
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

    @Nonnull
    @Override
    public Class<GetProjectDetailsAction> getActionClass() {
        return GetProjectDetailsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetProjectDetailsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetProjectDetailsResult execute(@Nonnull GetProjectDetailsAction action, @Nonnull ExecutionContext executionContext) {
        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(action.getProjectId());
        return GetProjectDetailsResult.get(projectDetails);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectDetailsActionHandler")
                .toString();
    }
}
