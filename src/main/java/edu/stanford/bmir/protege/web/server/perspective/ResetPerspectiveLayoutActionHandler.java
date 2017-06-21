package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.perspective.ResetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.shared.perspective.ResetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
public class ResetPerspectiveLayoutActionHandler extends AbstractHasProjectActionHandler<ResetPerspectiveLayoutAction, ResetPerspectiveLayoutResult> {

    @Nonnull
    private final PerspectiveLayoutStore store;

    @Inject
    public ResetPerspectiveLayoutActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull PerspectiveLayoutStore store) {
        super(accessManager);
        this.store = store;
    }

    @Override
    public Class<ResetPerspectiveLayoutAction> getActionClass() {
        return ResetPerspectiveLayoutAction.class;
    }

    @Override
    public ResetPerspectiveLayoutResult execute(ResetPerspectiveLayoutAction action,
                                                   ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        PerspectiveId perspectiveId = action.getPerspectiveId();
        PerspectiveLayout defaultLayout = store.getPerspectiveLayout(projectId, perspectiveId);
        // Only reset the perspective if there is a default for it
        UserId userId = executionContext.getUserId();
        if (defaultLayout.getRootNode().isPresent()) {
            store.clearPerspectiveLayout(projectId,
                                         userId,
                                         perspectiveId);
        }
        PerspectiveLayout perspectiveLayout = store.getPerspectiveLayout(projectId,
                                                                         userId,
                                                                         perspectiveId);
        return new ResetPerspectiveLayoutResult(perspectiveLayout);
    }
}
