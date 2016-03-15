package edu.stanford.bmir.protege.web.shared.permissions;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 * <p>
 *     The names of well known permissions.
 * </p>
 */
public enum PermissionName {

    READ("Read"),

    COMMENT("Comment"),

    WRITE("Write"),

    ADMIN("Admin"),

    DISPLAY_IN_PROJECT_LIST("DisplayInProjectList"),

    CREATE_USERS("CreateUsers");




    private String permissionName;

    private PermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    /**
     * Overidden to return the permission name (the same value as returned by {@link #getPermissionName()}.
     * @return The string value of this object corresponding to the permission name.
     */
    @Override
    public String toString() {
        return permissionName;
    }
}
