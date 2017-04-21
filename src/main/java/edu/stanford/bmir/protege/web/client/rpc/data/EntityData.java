package edu.stanford.bmir.protege.web.client.rpc.data;

import edu.stanford.bmir.protege.web.shared.watches.Watch;

import java.io.Serializable;
import java.util.*;

@Deprecated
public class EntityData implements Serializable {

    private static final long serialVersionUID = 8012327979110652428L;
    private String name;
    private String browserText;
    private int localAnnotationsCount;
    private int childrenAnnotationsCount;
    private Collection<EntityData> types;
    private ValueType valueType;
//    private WatchType watchType;
    private Map<String, String> properties;

    private Set<Watch> watches;

    /**
     * TODO: Should be for serialization purposes only.
     */
    protected EntityData() {
    }

    public EntityData(String name) {
        this(name, name);
    }

    public EntityData(String name, String browserText) {
        this(name, browserText, null);
    }


//    public EntityData(OWLEntity entity) {
//        this(entity, entity.getIRI().toString());
//    }

//    public EntityData(OWLEntity entity, String browserText) {
//        this(entity, browserText, null);
//    }

//    public EntityData(OWLEntity entity, String browserText, Collection<EntityData> types) {
//        this.name = name;
//        this.browserText = browserText;
//        this.types = types;
//    }
    public EntityData(String name, String browserText, Collection<EntityData> types) {
        this.name = name;
        this.browserText = browserText;
        this.types = types;
    }

    public void copyValuesFrom(EntityData sourceEntityData) {
        setName(sourceEntityData.getName());
        setBrowserText(sourceEntityData.getBrowserText());
        setLocalAnnotationsCount(sourceEntityData.getLocalAnnotationsCount());
        setChildrenAnnotationsCount(sourceEntityData.getChildrenAnnotationsCount());
        setValueType(sourceEntityData.getValueType());
//        setWatchType(sourceEntityData.getWatchType());
        Collection<EntityData> sourceTypes = sourceEntityData.getTypes();
        if (sourceTypes != null) {
            setTypes(new ArrayList<EntityData>(sourceTypes));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrowserText() {
        return browserText;
    }

    public void setBrowserText(String browserText) {
        this.browserText = browserText;
    }

    public Collection<EntityData> getTypes() {
        return types;
    }

    public void setTypes(Collection<EntityData> types) {
        this.types = types;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntityData)) {
            return false;
        }
        return ((EntityData) obj).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        if (name != null) {
            return name.length() * 11 + 42 + name.hashCode();
        }
        return 42;
    }

    @Override
    public String toString() {
        return browserText;
    }

    public int getLocalAnnotationsCount() {
        return localAnnotationsCount;
    }

    public void setLocalAnnotationsCount(int localAnnotationsCount) {
        this.localAnnotationsCount = localAnnotationsCount;
    }

    public int getChildrenAnnotationsCount() {
        return childrenAnnotationsCount;
    }

    public void setChildrenAnnotationsCount(int childrenAnnotationsCount) {
        this.childrenAnnotationsCount = childrenAnnotationsCount;
    }

//    public WatchType getWatchType() {
//        return watchType;
//    }
//
//    public void setWatchType(WatchType watchType) {
//        this.watchType = watchType;
//    }


    public Set<Watch> getWatches() {
        if(watches == null) {
            return Collections.emptySet();
        }
        else {
            return new HashSet<>(watches);
        }
    }

    public void clearWatches() {
        watches = null;
    }

    public void addWatch(Watch watch) {
        if(watches == null) {
            setWatches(Collections.singleton(watch));
        }
        else {
            watches.add(watch);
        }
    }


    public void setWatches(Set<Watch> watches) {
        this.watches = new HashSet<Watch>(watches);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getProperty(String prop) {
        if (properties == null) {
            return null;
        }
        return properties.get(prop);
    }

    public void setProperty(String prop, String value) {
        if (properties == null) {
            properties = new LinkedHashMap<String, String>();
        }
        properties.put(prop, value);
    }


}
