package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class GetPerspectiveLayoutActionHandler implements ProjectActionHandler<GetPerspectiveLayoutAction, GetPerspectiveLayoutResult> {

    @Nonnull
    private final PerspectivesManager perspectivesManager;

    @Inject
    public GetPerspectiveLayoutActionHandler(@Nonnull PerspectivesManager perspectivesManager) {
        this.perspectivesManager = checkNotNull(perspectivesManager);
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
        var perspectiveId = action.getPerspectiveId();
        var projectId = action.getProjectId();
        var userId = action.getUserId();
        var projectPerspective = perspectivesManager.getPerspectiveLayout(projectId, userId, perspectiveId);
        return new GetPerspectiveLayoutResult(projectPerspective);
    }
}
