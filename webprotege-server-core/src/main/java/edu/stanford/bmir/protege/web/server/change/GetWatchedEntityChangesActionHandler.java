package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.server.watches.WatchedChangesManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class GetWatchedEntityChangesActionHandler extends AbstractProjectActionHandler<GetWatchedEntityChangesAction, GetWatchedEntityChangesResult> {

    @Nonnull
    private final WatchManager watchManager;

    @Nonnull
    private final WatchedChangesManager watchedChangesManager;

    @Inject
    public GetWatchedEntityChangesActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull WatchManager watchManager,
                                                @Nonnull WatchedChangesManager watchedChangesManager) {
        super(accessManager);
        this.watchManager = watchManager;
        this.watchedChangesManager = watchedChangesManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetWatchedEntityChangesAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetWatchedEntityChangesResult execute(@Nonnull GetWatchedEntityChangesAction action, @Nonnull ExecutionContext executionContext) {
        Set<Watch> watches = watchManager.getWatches(action.getUserId());
        ImmutableList<ProjectChange> changes = watchedChangesManager.getProjectChangesForWatches(watches);
        Page<ProjectChange> page = new Page<>(1, 1, changes, changes.size());
        return new GetWatchedEntityChangesResult(page);
    }

    @Nonnull
    @Override
    public Class<GetWatchedEntityChangesAction> getActionClass() {
        return GetWatchedEntityChangesAction.class;
    }
}
