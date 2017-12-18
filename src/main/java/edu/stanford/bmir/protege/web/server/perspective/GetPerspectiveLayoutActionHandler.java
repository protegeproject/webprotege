package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class GetPerspectiveLayoutActionHandler implements ProjectActionHandler<GetPerspectiveLayoutAction, GetPerspectiveLayoutResult> {

    private PerspectiveLayoutStore perspectiveLayoutStore;

    @Inject
    public GetPerspectiveLayoutActionHandler(PerspectiveLayoutStore perspectiveLayoutStore) {
        this.perspectiveLayoutStore = perspectiveLayoutStore;
    }

    @Nonnull
    @Override
    public Class<GetPerspectiveLayoutAction> getActionClass() {
        return GetPerspectiveLayoutAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetPerspectiveLayoutAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetPerspectiveLayoutResult execute(@Nonnull GetPerspectiveLayoutAction action, @Nonnull ExecutionContext executionContext) {
        PerspectiveId perspectiveId = action.getPerspectiveId();
        ProjectId projectId = action.getProjectId();
        UserId userId = action.getUserId();
        PerspectiveLayout perspectiveLayout = perspectiveLayoutStore.getPerspectiveLayout(projectId, userId, perspectiveId);
        return new GetPerspectiveLayoutResult(perspectiveLayout);
    }
}
