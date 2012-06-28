package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that holds a compressed form of the triples for a subject
 * to enable efficient processing on the client side.
 * <br /><br />
 * Example of data structure:
 * subj1 -> {prop1 -> {value1, value2}, prop2 -> {value3}, ..}
 *
 * @author ttania
 *
 */
public class EntityPropertyValues implements Serializable{

    private static final long serialVersionUID = -1379727454578048738L;

    private EntityData subject;
    private Map<PropertyEntityData, List<EntityData>> prop2valuesMap = new LinkedHashMap<PropertyEntityData, List<EntityData>>();

    public EntityPropertyValues() { }

    public EntityPropertyValues(EntityData subject) {
        this.subject = subject;
    }

    public void setSubject(EntityData subject) {
        this.subject = subject;
    }
    public EntityData getSubject() {
        return subject;
    }

    public List<EntityData> getPropertyValues(PropertyEntityData prop) {
        return prop2valuesMap.get(prop);
    }

    public void addPropertyValue(PropertyEntityData prop, EntityData value) {
        List<EntityData> values = getPropertyValues(prop);
        if (values == null) {
            values = new ArrayList<EntityData>();
        }
        values.add(value);
        prop2valuesMap.put(prop, values);
    }

    public void addPropertyValues(PropertyEntityData prop, List<EntityData> value) {
        List<EntityData> values = getPropertyValues(prop);
        if (values == null) {
            values = new ArrayList<EntityData>();
        }
        values.addAll(value);
        prop2valuesMap.put(prop, values);
    }

    public void setPropertyValues(PropertyEntityData prop, List<EntityData> values) {
        prop2valuesMap.put(prop, values);
    }

    public Collection<PropertyEntityData> getProperties() {
        return prop2valuesMap.keySet();
    }

}
