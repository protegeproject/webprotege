package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.revision.ProjectChangesManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesActionHandler extends AbstractProjectActionHandler<GetProjectChangesAction, GetProjectChangesResult> {

    @Nonnull
    private final ProjectChangesManager changesManager;

    @Inject
    public GetProjectChangesActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectChangesManager changesManager) {
        super(accessManager);
        this.changesManager = changesManager;
    }

    @Nonnull
    @Override
    public Class<GetProjectChangesAction> getActionClass() {
        return GetProjectChangesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectChangesAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetProjectChangesResult execute(@Nonnull final GetProjectChangesAction action, @Nonnull ExecutionContext executionContext) {
        Page<ProjectChange> changeList = changesManager.getProjectChanges(action.getSubject(),
                                                                          action.getPageRequest());
        return GetProjectChangesResult.get(changeList);
    }
}
