package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesActionHandler extends AbstractProjectActionHandler<SetPerspectivesAction, SetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public SetPerspectivesActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = perspectivesManager;
    }

    @Nonnull
    @Override
    public Class<SetPerspectivesAction> getActionClass() {
        return SetPerspectivesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetPerspectivesAction action) {
        if(action.getUserId().isEmpty()) {
            return BuiltInAction.EDIT_PROJECT_SETTINGS;
        }
        else {
            return BuiltInAction.VIEW_PROJECT;
        }
    }

    @Nonnull
    @Override
    public SetPerspectivesResult execute(@Nonnull SetPerspectivesAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var userId = action.getUserId();
        var perspectiveDescriptors = action.getPerspectiveDescriptors();
        var executingUser = executionContext.getUserId();
        if(userId.isPresent()) {
            perspectivesManager.setPerspectives(projectId, userId.get(), perspectiveDescriptors);
        }
        else {
            perspectivesManager.savePerspectivesAsProjectDefault(projectId, perspectiveDescriptors, executingUser);
        }
        var resettablePerspectives = perspectivesManager.getResettablePerspectiveIds(projectId, executingUser);
        return SetPerspectivesResult.get(perspectiveDescriptors, resettablePerspectives);
    }
}
