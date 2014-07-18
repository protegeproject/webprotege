package edu.stanford.bmir.protege.web.client.rpc.data;

import java.util.Collection;
import java.util.List;

@Deprecated
public class PropertyEntityData extends EntityData {

    private List<EntityData> allowedValues;
    private int minCardinality = 0;
    private int maxCardinality = -1;
    private PropertyType propertyType; //TODO - temporary solution, will be removed
    private boolean isSystem;

    public PropertyEntityData() {
        super(null);
    }

    public PropertyEntityData(String name) {
        super(name);
    }

    public PropertyEntityData(String name, String browserText, Collection<EntityData> types) {
        super(name, browserText, types);
    }

    public int getMinCardinality() {
        return minCardinality;
    }

    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }

    public int getMaxCardinality() {
        return maxCardinality;
    }

    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    public void setAllowedValues(List<EntityData> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public List<EntityData> getAllowedValues() {
        return allowedValues;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }
}
