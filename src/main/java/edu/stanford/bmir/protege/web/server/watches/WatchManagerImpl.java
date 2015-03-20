package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.*;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class WatchManagerImpl implements WatchManager, HasDispose {

    private final ProjectId projectId;

    private final WatchStore watchStore;

    private final WatchIndex watchIndex;

    private final WatchTriggeredHandler watchTriggeredHandler;

    private final IndirectlyWatchedEntitiesFinder watchedEntitiesFinder;

    private final EventManager<ProjectEvent<?>> eventManager;

    @Inject
    public WatchManagerImpl(ProjectId projectId,
                            WatchStore watchStore,
                            WatchIndex watchIndex,
                            IndirectlyWatchedEntitiesFinder watchedEntitiesFinder,
                            WatchTriggeredHandler watchTriggeredHandler,
                            EventManager<ProjectEvent<?>> eventManager) {
        this.projectId = checkNotNull(projectId);
        this.watchStore = checkNotNull(watchStore);
        this.watchIndex = checkNotNull(watchIndex);
        this.watchTriggeredHandler = checkNotNull(watchTriggeredHandler);
        this.watchedEntitiesFinder = checkNotNull(watchedEntitiesFinder);
        this.eventManager = checkNotNull(eventManager);
    }

    @Override
    public Set<Watch<?>> getWatches(UserId userId) {
        return watchIndex.getWatchesForUser(userId);
    }

    @Override
    public void addWatch(Watch<?> watch, UserId userId) {
        if (watchIndex.addWatch(watch, userId)) {
            watchStore.addWatch(new UserWatch<>(userId, watch));
            eventManager.postEvent(new WatchAddedEvent(projectId, watch, userId));
        }
    }

    @Override
    public void removeWatch(Watch<?> watch, UserId userId) {
        if (watchIndex.removeWatch(watch, userId)) {
            watchStore.removeWatch(new UserWatch<>(userId, watch));
            eventManager.postEvent(new WatchRemovedEvent(projectId, watch, userId));
        }
    }

    @Override
    public Set<Watch<?>> getDirectWatches(OWLEntity watchedObject, UserId userId) {
        Set<Watch<?>> result = new HashSet<>();
        for(Watch<?> watch : watchIndex.getWatchesOnEntity(watchedObject)) {
            if(watchIndex.getUsersForWatch(watch).contains(userId)) {
                result.add(watch);
            }
        }
        return result;
    }

    public void handleEntityFrameChanged(OWLEntity entity) {
        List<Watch<?>> watches = new ArrayList<>();
        watches.addAll(watchIndex.getWatchesOnEntity(entity));
        for (OWLEntity anc : watchedEntitiesFinder.getRelatedWatchedEntities(entity)) {
            watches.addAll(watchIndex.getWatchesOnEntity(anc));
        }
        for (Watch<?> watch : watches) {
            for (UserId userId : watchIndex.getUsersForWatch(watch)) {
                fireWatch(watch, userId, entity);
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void fireWatch(final Watch<?> watch, final UserId userId, final OWLEntity entity) {
        watchTriggeredHandler.handleWatchTriggered(watch, userId, entity);
    }

    @Override
    public void dispose() {
    }


}
