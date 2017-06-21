package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class SetEntityWatchesActionHandler extends AbstractHasProjectActionHandler<SetEntityWatchesAction, SetEntityWatchesResult> {

    private EventManager<ProjectEvent<?>> eventManager;

    private WatchManager watchManager;

    @Inject
    public SetEntityWatchesActionHandler(@Nonnull AccessManager accessManager,
                                         EventManager<ProjectEvent<?>> eventManager,
                                         WatchManager watchManager) {
        super(accessManager);
        this.eventManager = eventManager;
        this.watchManager = watchManager;
    }

    @Override
    public Class<SetEntityWatchesAction> getActionClass() {
        return SetEntityWatchesAction.class;
    }

    @Override
    public SetEntityWatchesResult execute(SetEntityWatchesAction action, ExecutionContext executionContext) {
        EventTag startTag = eventManager.getCurrentTag();
        UserId userId = action.getUserId();
        Set<Watch> watches = watchManager.getDirectWatches(action.getEntity(), userId);
        for(Watch watch : watches) {
            watchManager.removeWatch(watch);
        }
        for(Watch watch : action.getWatches()) {
            watchManager.addWatch(watch);
        }
        return new SetEntityWatchesResult(eventManager.getEventsFromTag(startTag));
    }

}
