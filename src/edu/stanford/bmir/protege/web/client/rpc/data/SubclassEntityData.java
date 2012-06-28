package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.Collection;

public class SubclassEntityData extends EntityData implements Serializable {

    private static final long serialVersionUID = 9136753213032322411L;

    private int subclassCount;

    /**
     * Ser
     */
    private SubclassEntityData() {
        super();
    }

    public SubclassEntityData(String name, String browserText, Collection<EntityData> type, int subclassCount) {
        super(name, browserText, type);
        this.subclassCount = subclassCount;
    }

    public void setSubclassCount(int subclassCount) {
        this.subclassCount = subclassCount;
    }

    public int getSubclassCount() {
        return subclassCount;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SubclassEntity(");
        buffer.append(this.getName());
        buffer.append(", ");
        buffer.append(this.getSubclassCount());
        buffer.append(")");
        return buffer.toString();
    }

}
