package edu.stanford.bmir.protege.web.shared.permissions;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 */
public class GroupPolicy {

    private GroupId groupId;

    private PermissionsSet permissions;

    public GroupPolicy(GroupId groupId, PermissionsSet permissions) {
        this.groupId = groupId;
        this.permissions = permissions;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public PermissionsSet getPermissions() {
        return permissions;
    }

    @Override
    public int hashCode() {
        return "GroupPermissionSettings".hashCode() + groupId.hashCode() + permissions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GroupPolicy)) {
            return false;
        }
        GroupPolicy other = (GroupPolicy) obj;
        return this.groupId.equals(other.groupId) && this.permissions.equals(other.permissions);
    }
}
