package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveDetailsAction;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveDetailsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
public class GetPerspectiveDetailsActionHandler extends AbstractProjectActionHandler<GetPerspectiveDetailsAction, GetPerspectiveDetailsResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public GetPerspectiveDetailsActionHandler(@Nonnull AccessManager accessManager,
                                              PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<GetPerspectiveDetailsAction> getActionClass() {
        return GetPerspectiveDetailsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetPerspectiveDetailsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetPerspectiveDetailsResult execute(@Nonnull GetPerspectiveDetailsAction action,
                                               @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var userId = executionContext.getUserId();
        var perspectiveDetails = perspectivesManager.getPerspectiveDetails(projectId, userId);
        return GetPerspectiveDetailsResult.get(perspectiveDetails);
    }
}
