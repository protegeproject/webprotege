package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class EntityIdUserRange implements Serializable, Comparable<EntityIdUserRange> {

    private UserId userId;

    private long firstId;
    
    private long lastId;

    private EntityIdUserRange() {
    }
    
    public EntityIdUserRange(UserId userId) {
        this(userId, 0, Long.MAX_VALUE);
    }
    

    public EntityIdUserRange(UserId userId, long firstId, long lastId) {
        this.userId = userId;
        this.firstId = firstId;
        this.lastId = lastId;
    }
    
    public static EntityIdUserRange getBlank() {
        return new EntityIdUserRange(UserId.getNull(), 0, Long.MAX_VALUE);
    }

    public UserId getUserId() {
        return userId;
    }

    public long getFirstId() {
        return firstId;
    }

    public long getLastId() {
        return lastId;
    }

    /**
     * Compares this EntityIdUserRange to another one.  The comparison is based on the first and last range ids and the
     * userId.
     * @param o The EntityIdUserRange to compare to.
     * @return See {@link Comparable#compareTo(Object)}.
     */
    public int compareTo(EntityIdUserRange o) {
        if(this.firstId < o.firstId) {
            return -1;
        }
        else if(this.firstId > o.firstId) {
            return 1;
        }
        if(this.lastId < o.lastId) {
            return -1;
        }
        else if(this.lastId > o.lastId) {
            return 1;
        }
        return this.userId.compareTo(o.userId);
    }

    @Override
    public int hashCode() {
        return "EntityIRIGenerationRange".hashCode() + userId.hashCode() + (int) firstId + (int) lastId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EntityIdUserRange)) {
            return false;
        }
        EntityIdUserRange other = (EntityIdUserRange) obj;
        return this.userId.equals(other.userId) && this.firstId == other.firstId && this.lastId == other.lastId;
    }


}
