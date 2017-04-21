package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.watches.WatchType.BRANCH;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@ProjectSingleton
public class WatchManagerImpl implements WatchManager {

    private static final Logger logger = LoggerFactory.getLogger(WatchManagerImpl.class);

    private final ProjectId projectId;

    private final WatchRecordRepositoryImpl repository;

    private final IndirectlyWatchedEntitiesFinder indirectlyWatchedEntitiesFinder;

    private final WatchTriggeredHandler watchTriggeredHandler;

    private final EventManager<ProjectEvent<?>> eventManager;


    @Inject
    public WatchManagerImpl(@Nonnull ProjectId projectId,
                            @Nonnull WatchRecordRepositoryImpl repository,
                            @Nonnull IndirectlyWatchedEntitiesFinder indirectlyWatchedEntitiesFinder,
                            @Nonnull WatchTriggeredHandler watchTriggeredHandler,
                            @Nonnull EventManager<ProjectEvent<?>> eventManager) {
        this.projectId = checkNotNull(projectId);
        this.repository = checkNotNull(repository);
        this.indirectlyWatchedEntitiesFinder = checkNotNull(indirectlyWatchedEntitiesFinder);
        this.watchTriggeredHandler = checkNotNull(watchTriggeredHandler);
        this.eventManager = checkNotNull(eventManager);
    }

    public void attach() {
        // Note, there is no need to keep hold of Handler Registrations here as these will be cleaned up and
        // terminated when the relevant project is disposed.
        eventManager.addHandler(ClassFrameChangedEvent.TYPE, event -> {
            handleEntityFrameChanged(event.getEntity(), event.getUserId());
        });
        eventManager.addHandler(ObjectPropertyFrameChangedEvent.TYPE, event -> {
            handleEntityFrameChanged(event.getEntity(), event.getUserId());
        });
        eventManager.addHandler(DataPropertyFrameChangedEvent.TYPE, event -> {
            handleEntityFrameChanged(event.getEntity(), event.getUserId());
        });
        eventManager.addHandler(AnnotationPropertyFrameChangedEvent.TYPE, event -> {
            handleEntityFrameChanged(event.getEntity(), event.getUserId());
        });
        eventManager.addHandler(NamedIndividualFrameChangedEvent.TYPE, event -> {
            handleEntityFrameChanged(event.getEntity(), event.getUserId());
        });
    }

    @Override
    public Set<Watch> getWatches(@Nonnull UserId userId) {
        return repository.findWatchRecords(projectId, userId).stream()
                         .map(this::toWatch)
                         .collect(toSet());
    }

    @Override
    public void addWatch(@Nonnull Watch watch) {
        repository.saveWatchRecord(toWatchRecord(watch));
        eventManager.postEvent(new WatchAddedEvent(projectId, watch));
    }

    @Override
    public void removeWatch(@Nonnull Watch watch) {
        repository.deleteWatchRecord(toWatchRecord(watch));
        eventManager.postEvent(new WatchRemovedEvent(projectId, watch));
    }

    @Override
    public Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedObject, @Nonnull UserId userId) {
        return repository.findWatchRecords(projectId,
                                           userId,
                                           singleton(watchedObject)).stream()
                         .map(this::toWatch)
                         .collect(toSet());
    }

    private void handleEntityFrameChanged(@Nonnull OWLEntity entity, @Nonnull UserId byUser) {
        Collection<Watch> watches = findWatchRecordsForEntity(entity);
        if (watches.isEmpty()) {
            return;
        }
        Set<UserId> userIds = watches.stream()
                                     .map(Watch::getUserId)
                                     .collect(toSet());
        watchTriggeredHandler.handleWatchTriggered(userIds, entity, byUser);

    }

    private Collection<Watch> findWatchRecordsForEntity(OWLEntity entity) {
        List<OWLEntity> entities = new ArrayList<>(indirectlyWatchedEntitiesFinder.getRelatedWatchedEntities(entity));
        entities.add(entity);
        return repository.findWatchRecords(projectId, entities).stream()
                         .distinct()
                         // Filter so that we have watch records that are either watching a branch containing
                         // this entity or records that are directly watching this entity
                         .filter(r -> r.getType() == BRANCH || r.getEntity().equals(entity))
                         .map(this::toWatch)
                         .collect(toSet());
    }


    private WatchRecord toWatchRecord(Watch watch) {
        return new WatchRecord(projectId, watch.getUserId(), watch.getEntity(), watch.getType());
    }

    private Watch toWatch(WatchRecord record) {
        return new Watch(record.getUserId(), record.getEntity(), record.getType());
    }
}
