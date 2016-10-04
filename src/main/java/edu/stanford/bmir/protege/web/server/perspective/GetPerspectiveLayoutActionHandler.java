package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class GetPerspectiveLayoutActionHandler implements ActionHandler<GetPerspectiveLayoutAction, GetPerspectiveLayoutResult> {

    private PerspectiveLayoutStore perspectiveLayoutStore;

    @Inject
    public GetPerspectiveLayoutActionHandler(PerspectiveLayoutStore perspectiveLayoutStore) {
        this.perspectiveLayoutStore = perspectiveLayoutStore;
    }

    @Override
    public Class<GetPerspectiveLayoutAction> getActionClass() {
        return GetPerspectiveLayoutAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetPerspectiveLayoutAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPerspectiveLayoutResult execute(GetPerspectiveLayoutAction action, ExecutionContext executionContext) {
        PerspectiveId perspectiveId = action.getPerspectiveId();
        ProjectId projectId = action.getProjectId();
        UserId userId = action.getUserId();
        PerspectiveLayout perspectiveLayout = perspectiveLayoutStore.getPerspectiveLayout(projectId, userId, perspectiveId);
        return new GetPerspectiveLayoutResult(perspectiveLayout);
    }
}
