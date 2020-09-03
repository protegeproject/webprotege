package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetUserPerspectivesAsProjectDefaultAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetUserPerspectivesAsProjectDefaultResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class SetUserPerspectivesAsProjectDefaultActionHandler extends AbstractProjectActionHandler<SetUserPerspectivesAsProjectDefaultAction, SetUserPerspectivesAsProjectDefaultResult> {

    @Nonnull
    private final PerspectivesManager perspectivesManager;

    @Inject
    public SetUserPerspectivesAsProjectDefaultActionHandler(@Nonnull AccessManager accessManager,
                                                            @Nonnull PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<SetUserPerspectivesAsProjectDefaultAction> getActionClass() {
        return SetUserPerspectivesAsProjectDefaultAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        // Need to be more specific here?
        return BuiltInAction.EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public SetUserPerspectivesAsProjectDefaultResult execute(@Nonnull SetUserPerspectivesAsProjectDefaultAction action,
                                                             @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var userId = action.getUserId();
        perspectivesManager.savePerspectivesAsProjectDefault(projectId, userId);
        return SetUserPerspectivesAsProjectDefaultResult.get();
    }
}
