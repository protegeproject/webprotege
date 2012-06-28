package edu.stanford.bmir.protege.web.client.model.event;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

public class EntityRenameEvent extends AbstractOntologyEvent {

    private static final long serialVersionUID = 7418977984772363891L;

    private String oldName;

	public EntityRenameEvent() {}

	public EntityRenameEvent(EntityData entity, String oldName, String user, int revision) {
		super(entity, EventType.ENTITY_RENAMED, user, revision);
		this.oldName = oldName;
	}

	public String getOldName() {
		return oldName;
	}

    @Override
    public String toString() {
        return "EntityRenamedEvent(" + getEntity() + ", " + getOldName() + ", " + getUser() + ")";
    }

}
