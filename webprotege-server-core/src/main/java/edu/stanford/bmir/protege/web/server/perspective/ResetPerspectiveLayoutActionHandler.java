package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.perspective.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
public class ResetPerspectiveLayoutActionHandler extends AbstractProjectActionHandler<ResetPerspectiveLayoutAction, ResetPerspectiveLayoutResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public ResetPerspectiveLayoutActionHandler(@Nonnull AccessManager accessManager,
                                               PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<ResetPerspectiveLayoutAction> getActionClass() {
        return ResetPerspectiveLayoutAction.class;
    }

    @Nonnull
    @Override
    public ResetPerspectiveLayoutResult execute(@Nonnull ResetPerspectiveLayoutAction action,
                                                @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var perspectiveId = action.getPerspectiveId();
        var userId = executionContext.getUserId();
        perspectivesManager.resetPerspectiveLayout(projectId, userId, perspectiveId);
        var perspectiveLayout = perspectivesManager.getPerspectiveLayout(projectId, userId, perspectiveId);
        return new ResetPerspectiveLayoutResult(perspectiveLayout);
    }
}
