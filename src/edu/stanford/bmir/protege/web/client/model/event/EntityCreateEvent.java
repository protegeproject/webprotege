package edu.stanford.bmir.protege.web.client.model.event;

import java.util.List;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

public class EntityCreateEvent extends AbstractOntologyEvent {

    private static final long serialVersionUID = 6772147385301326320L;

    protected List<EntityData> superEntities;

    public EntityCreateEvent() { }

    public EntityCreateEvent(EntityData entity, String user, int revision) {
        this(entity, EventType.ENTITY_CREATED, user, null, revision);
    }

    public EntityCreateEvent(EntityData entity, int type, String user, List<EntityData> superEntities, int revision) {
        super(entity, type, user, revision);
        this.superEntities = superEntities;
    }

    public List<EntityData> getSuperEntities() {
        return superEntities;
    }

    @Override
    public String toString() {
        return "EntityCreatedEvent(" + getEntity() + ", " + getType() + ", " + getSuperEntities() + ", " + getUser() + ")";
    }

}
