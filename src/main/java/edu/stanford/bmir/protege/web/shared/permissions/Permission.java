package edu.stanford.bmir.protege.web.shared.permissions;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 * <p>
 *     Represents a permission such as "Read", "Write" etc.  Permission objects compare equal on the name of the
 *     permission that they represent.
 * </p>
 */
public class Permission implements Serializable {

    private static final Permission READ_PERMISSION = new Permission(PermissionName.READ.getPermissionName());

    private static final Permission COMMENT_PERMISSION = new Permission(PermissionName.COMMENT.getPermissionName());

    private static final Permission WRITE_PERMISSION = new Permission(PermissionName.WRITE.getPermissionName());

    private String permissionName;


    /**
     * Constructs a permission.
     * @param permissionName The name of the permission.  Not {@code null}.
     * @throws NullPointerException if {@code permissionName} is {@code null}.
     */
    private Permission(String permissionName) {
        this.permissionName = checkNotNull(permissionName);
    }

    /**
     * For serialization purposes only
     */
    private Permission() {
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Factory methdods
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets a pemission that has the specified name.
     * @param permissionName The name of the permission.  Not {@code null}.
     * @return The {@link Permission} that has the specified name.  Not {@code null}.
     * @throws NullPointerException if {@code permissionName} is {@code null}.
     */
    public static Permission getPermission(String permissionName) {
        checkNotNull(permissionName);
        if(PermissionName.READ.getPermissionName().equals(permissionName)) {
            return READ_PERMISSION;
        }
        else if(PermissionName.WRITE.getPermissionName().equals(permissionName)) {
            return WRITE_PERMISSION;
        }
        else {
            return new Permission(permissionName);
        }
    }

    /**
     * Gets the permission that represents the read permission.
     * @return The permission object representing the read permission.  Not {@code null}.
     */
    public static Permission getReadPermission() {
        return READ_PERMISSION;
    }


    /**
     * Gets the permission that represents the comment permission.
     * @return The permission object representing the comment permission.  Not {@code null}.
     */
    public static Permission getCommentPermission() {
        return COMMENT_PERMISSION;
    }

    /**
     * Gets the permission that represents the write permission.
     * @return The permission object representing the write permission.  Not {@code null}.
     */
    public static Permission getWritePermission() {
        return WRITE_PERMISSION;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Determines if this permission is equal to the read permission.
     * @return {@code true} if this permission is equal to the read permission (the name of the permission is
     * equal to the name of the read permission as defined by {@link PermissionName#READ}), otherwise {@code false}.
     */
    public boolean isReadPermission() {
        return this.equals(READ_PERMISSION);
    }

    /**
     * Determines if this permission is equal to the comment permission.
     * @return {@code true} if this permission is equal to the comment permission (the name of the permission is
     * equal to the name of the comment permission as defined by {@link PermissionName#COMMENT}), otherwise {@code false}.
     */
    public boolean isCommentPermission() {
        return this.equals(COMMENT_PERMISSION);
    }


    /**
     * Determines if this permission is equal to the write permission.
     * @return {@code true} if this permission is equal to the write permission (the name of the permission is
     * equal to the name of the write permission as defined by {@link PermissionName#WRITE}), otherwise {@code false}.
     */
    public boolean isWritePermission() {
        return this.equals(WRITE_PERMISSION);
    }




    @Override
    public int hashCode() {
        return "Permission".hashCode() + permissionName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) obj;
        return this.permissionName.equals(other.permissionName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Permission");
        sb.append("(");
        sb.append(permissionName);
        sb.append(")");
        return sb.toString();
    }
}
