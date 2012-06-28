package edu.stanford.bmir.protege.web.client.model.event;

import java.util.List;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

public class EntityDeleteEvent extends AbstractOntologyEvent {

    private static final long serialVersionUID = -7968930987143760984L;

    protected List<EntityData> superEntities;

	public EntityDeleteEvent() { }

	public EntityDeleteEvent(EntityData entity, String user, int revision) {
		this(entity, EventType.ENTITY_DELETED, user, null, revision);
	}

	public EntityDeleteEvent(EntityData entity, int type, String user, List<EntityData> superEntities, int revision) {
		super(entity, type, user, revision);
		this.superEntities = superEntities;
	}

	public List<EntityData> getSuperEntities() {
		return superEntities;
	}

    @Override
    public String toString() {
        return "EntityDeletedEvent(" + getEntity() + ", " + getSuperEntities() + ", " + getUser() + ")";
    }

}
