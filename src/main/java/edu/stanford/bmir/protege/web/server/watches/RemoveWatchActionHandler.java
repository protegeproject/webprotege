package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.RemoveWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchActionHandler extends AbstractHasProjectActionHandler<RemoveWatchesAction, RemoveWatchesResult> {

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final WatchManager watchManager;

    @Inject
    public RemoveWatchActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                    @Nonnull WatchManager watchManager) {
        super(accessManager);
        this.eventManager = eventManager;
        this.watchManager = watchManager;
    }

    @Override
    public RemoveWatchesResult execute(RemoveWatchesAction action, ExecutionContext executionContext) {
        EventTag tag = eventManager.getCurrentTag();
        for(Watch watch : action.getWatches()) {
            watchManager.removeWatch(watch);
        }
        return new RemoveWatchesResult(eventManager.getEventsFromTag(tag));
    }

    @Override
    public Class<RemoveWatchesAction> getActionClass() {
        return RemoveWatchesAction.class;
    }
}
