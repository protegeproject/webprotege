package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.WATCH_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class GetWatchesActionHandler extends AbstractHasProjectActionHandler<GetWatchesAction, GetWatchesResult> {

    @Nonnull
    private final WatchManager watchManager;

    @Inject
    public GetWatchesActionHandler(@Nonnull AccessManager accessManager,
                                   @Nonnull WatchManager watchManager) {
        super(accessManager);
        this.watchManager = watchManager;
    }

    @Override
    public Class<GetWatchesAction> getActionClass() {
        return GetWatchesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(WATCH_CHANGES, VIEW_PROJECT);
    }

    @Override
    public GetWatchesResult execute(GetWatchesAction action, ExecutionContext executionContext) {
        Set<Watch> watches = watchManager.getDirectWatches(action.getEntity(), action.getUserId());
        return new GetWatchesResult(watches);
    }
}
