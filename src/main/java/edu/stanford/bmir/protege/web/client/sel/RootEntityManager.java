package edu.stanford.bmir.protege.web.client.sel;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 03/10/2014
 *
 * A temporary (hopefully very temporary) stopgap until we get property browser history support.
 */
public class RootEntityManager {

    private final EventBus eventBus;

    private final Map<EntityType<?>, OWLEntityData> mostRecentRootEntityByTypeMap = Maps.newHashMap();


    private Optional<OWLEntityData> rootEntity = Optional.absent();

    /**
     * Constructs a RootEntityManager using the specified EventBus
     * @param eventBus The event bus.  Not {@code null}.
     */
    public RootEntityManager(EventBus eventBus) {
        this.eventBus = checkNotNull(eventBus);
    }

    /**
     * Gets the current root entity.  Not {@code null}.
     * @return The current root entity.
     */
    public Optional<OWLEntityData> getRootEntity() {
        return rootEntity;
    }

    /**
     * Gets the most recent root entity of the specified entity type.
     * @param entityType The entity type.  Not {@code null}.
     * @return The most recent root entity of the specified entity type.  Not {@code null}.
     */
    public Optional<OWLEntityData> getMostRecentRootEntityOfType(EntityType<?> entityType) {
        return Optional.fromNullable(mostRecentRootEntityByTypeMap.get(checkNotNull(entityType)));
    }

    /**
     * Sets the root entity.
     * @param entityData The root entity.  Not {@code null}.
     * @param source The source setting the entity.  Not {@code null}.
     * @throws NullPointerException is {@code source} is {@code null}.
     */
    public void setRootEntity(OWLEntityData entityData, Object source) {
        checkNotNull(entityData);
        Optional<OWLEntityData> nextRoot = Optional.of(entityData);
        setRootEntityInternal(nextRoot, checkNotNull(source));
    }

    /**
     * Clears the current root entity.
     * @param source The source clearing the root entity.  Not {@code null}.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public void clearRootEntity(Object source) {
        Optional<OWLEntityData> nextRoot = Optional.absent();
        setRootEntityInternal(nextRoot, checkNotNull(source));
    }

    private void setRootEntityInternal(Optional<OWLEntityData> nextRoot, Object source) {
        if(rootEntity.equals(nextRoot)) {
            return;
        }
        Optional<OWLEntityData> previousEntity = rootEntity;
        rootEntity = nextRoot;
        if(nextRoot.isPresent()) {
            OWLEntityData nextRootEntity = nextRoot.get();
            mostRecentRootEntityByTypeMap.put(nextRootEntity.getEntity().getEntityType(), nextRootEntity);
        }
        // Fire event
        RootEntityChangedEvent event = new RootEntityChangedEvent(previousEntity, rootEntity, source);
        eventBus.fireEvent(event);
    }



}
