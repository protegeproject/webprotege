package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesResult;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class GetPerspectivesActionHandler implements ActionHandler<GetPerspectivesAction, GetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public GetPerspectivesActionHandler(PerspectivesManager perspectivesManager) {
        this.perspectivesManager = perspectivesManager;
    }

    @Override
    public Class<GetPerspectivesAction> getActionClass() {
        return GetPerspectivesAction.class;
    }

    @Override
    public RequestValidator<GetPerspectivesAction> getRequestValidator(GetPerspectivesAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPerspectivesResult execute(GetPerspectivesAction action, ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        UserId userId = action.getUserId();
        ImmutableList<PerspectiveId> perspectives = perspectivesManager.getPerspectives(projectId, userId);
        return new GetPerspectivesResult(perspectives);
    }
}
