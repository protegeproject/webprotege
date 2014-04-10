package edu.stanford.bmir.protege.web.shared.permissions;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 * <p>
 *     An enum of well known group names.
 * </p>
 */
public enum GroupName {

    WORLD("World");


    private String groupName;

    private GroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Gets the group name.
     * @return A String representing the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Overriden to return the group name for this instance.
     * @return A string representing the group name.  This is the same value that is returned by {@link #getGroupName()}.
     */
    @Override
    public String toString() {
        return groupName;
    }
}

