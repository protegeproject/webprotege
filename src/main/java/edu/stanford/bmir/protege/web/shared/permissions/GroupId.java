package edu.stanford.bmir.protege.web.shared.permissions;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 * <p>
 *     Represents an id for a group of users.
 * </p>
 */
public class GroupId implements Serializable {

    private static final GroupId WORLD_GROUP_ID = new GroupId(GroupName.WORLD.getGroupName());

    private String groupName;

    private GroupId(String groupName) {
        this.groupName = groupName;
    }

    /**
     * For serialization purposes only
     */
    private GroupId() {

    }

    /**
     * Gets an instance of {@link GroupId} for the specified group name.
     * @param groupName The group name for the group.  Not {@code null}.
     * @return The {@link GroupId} for the specified name.
     */
    public static GroupId get(String groupName) {
        return new GroupId(checkNotNull(groupName));
    }

    public static GroupId get(GroupName groupName) {
        return get(groupName.getGroupName());
    }

    /**
     * Gets the {@link GroupId} identifier for the "World" group.  This has a name identified by {@link GroupName#WORLD}.
     * @return The {@link GroupId} for the World group.  Not {@code null}.
     */
    public static GroupId getWorld() {
        return WORLD_GROUP_ID;
    }

    /**
     * Gets the name of the group that this object identifies.
     * @return The group name.  Not {@code null}.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Determines if this {@link GroupId} identifies the "World" group which every user belongs to.
     * @return {@code true} if this {@link GroupId} identifies the "World" group (i.e. has a group name equal to
     * the value defined by {@link GroupName#WORLD}) otherwise {@code false}.
     */
    public boolean isWorld() {
        return GroupName.WORLD.getGroupName().equals(groupName);
    }

    @Override
    public int hashCode() {
        return "GroupId".hashCode() + groupName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GroupId)) {
            return false;
        }
        GroupId other = (GroupId) obj;
        return this.getGroupName().equals(other.groupName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GroupId");
        sb.append("(");
        sb.append(groupName);
        sb.append(")");
        return sb.toString();
    }
}
