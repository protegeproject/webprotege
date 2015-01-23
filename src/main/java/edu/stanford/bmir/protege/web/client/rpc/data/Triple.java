package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

@Deprecated
public class Triple implements Serializable {
	
	private EntityData entity;
	private PropertyEntityData property;
	private EntityData value; 
	
	public Triple() {
	}
	
	public Triple(EntityData entity, PropertyEntityData property, EntityData value) {
		this.entity = entity;
		this.property = property;		
		this.value = value;
	}

	public EntityData getEntity() {
		return entity;
	}

	public PropertyEntityData getProperty() {
		return property;
	}

	public EntityData getValue() {
		return value;
	}

    @Override
    public int hashCode() {
        return (entity.hashCode() * 13) + (property.hashCode() * 17) + (value.hashCode() * 23);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Triple)) {
            return false;
        }
        Triple other = (Triple) obj;
        return entity.equals(other.entity) && property.equals(other.property) && value.equals(other.value);
    }
}
