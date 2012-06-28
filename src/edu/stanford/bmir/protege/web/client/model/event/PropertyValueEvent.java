package edu.stanford.bmir.protege.web.client.model.event;

import java.util.Collection;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;

public class PropertyValueEvent extends AbstractOntologyEvent {

    private static final long serialVersionUID = -4412884172296158104L;

    private PropertyEntityData property;
	private Collection<EntityData> addedValues;
	private Collection<EntityData> removedValues;
	private int eventType;

	public PropertyValueEvent() {}

	public PropertyValueEvent(EntityData entity, PropertyEntityData property,
			Collection<EntityData> addedValues, Collection<EntityData> removedValues, int eventType, String user, int revision) {
		super(entity, eventType, user, revision);
		this.addedValues = addedValues;
		this.removedValues = removedValues;
		this.eventType = eventType;
		this.property = property;
	}

	public PropertyEntityData getProperty() {
		return property;
	}

	public Collection<EntityData> getAddedValues() {
		return addedValues;
	}

	public Collection<EntityData> getRemovedValues() {
		return removedValues;
	}

	public int getEventType() {
		return eventType;
	}

    @Override
    public String toString() {
        return "PropertyValueEvent(" + getEntity() + ", " + getProperty() + ", " + getUser() + ")";
    }

}
