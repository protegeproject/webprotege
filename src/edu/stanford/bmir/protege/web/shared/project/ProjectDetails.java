package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.HasProjectId;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 * <p>
 *     Instances of this class record the main details about a project.
 * </p>
 */
public class ProjectDetails implements Serializable, Comparable<ProjectDetails>, HasProjectId {

    private ProjectId projectId;

    private UserId owner;


    private String displayName;

    private String description;



    private boolean inTrash;

    /**
     * For serialization purposes only
     */
    private ProjectDetails() {

    }

    /**
     * Constructs a {@link ProjectDetails} object.
     * @param projectId The {@link ProjectId} that identifies the project which these details describe.
     * @param displayName The human readable name for the project.  Not {@code null}.
     * @param owner The owner of the project. Not {@code null}.
     * @param description A description of the project.  Not {@code null}. May be empty.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public ProjectDetails(ProjectId projectId, String displayName, String description, UserId owner, boolean inTrash) {
        this.projectId = checkNotNull(projectId);
        this.displayName = checkNotNull(displayName);
        this.owner = checkNotNull(owner);
        this.description = checkNotNull(description);
        this.inTrash = inTrash;
    }

    /**
     * Gets the {@link ProjectId} of the project that these details describe.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the human readable name for the project.
     * @return The human readable name.  Not {@code null}.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the {@link UserId} that identifies the owner of the project.
     * @return The {@link UserId} for the project described by these details.  Not {@code null}.
     */
    public UserId getOwner() {
        return owner;
    }

    /**
     * Gets the description of the project described by these details.
     * @return The description as a string.  Not {@code null}.  May be empty.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Determines if this project is in the trash.
     * @return {@code true} if this project is in the trash, otherwise {@code false}.
     */
    public boolean isInTrash() {
        return inTrash;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ProjectDetails)) {
            return false;
        }
        ProjectDetails other = (ProjectDetails) o;
        return this.projectId.equals(other.projectId) && this.displayName.equals(other.displayName) && this.description.equals(other.description) && this.owner.equals(other.owner) && this.inTrash == other.inTrash;
    }

    @Override
    public int hashCode() {
        return "ProjectDetails".hashCode() + projectId.hashCode() + displayName.hashCode() + description.hashCode() + owner.hashCode() + (inTrash ? 7 : 37);
    }

    @Override
    public int compareTo(ProjectDetails o) {
        final int dispNameDiff = displayName.compareToIgnoreCase(o.getDisplayName());
        if(dispNameDiff != 0) {
            return dispNameDiff;
        }
        final int caseSensitiveDiff = displayName.compareTo(o.getDisplayName());
        if(caseSensitiveDiff != 0) {
            return caseSensitiveDiff;
        }
        int ownerDiff = owner.compareTo(o.getOwner());
        if(ownerDiff != 0) {
            return ownerDiff;
        }
        int descriptionDiff = description.compareTo(o.getDescription());
        if(descriptionDiff != 0) {
            return descriptionDiff;
        }
        return projectId.getId().compareTo(o.getProjectId().getId());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ProjectDetails")
                .addValue(projectId)
                .add("displayName", displayName)
                .add("description", description)
                .add("owner", owner)
                .add("inTrash", inTrash).toString();
    }
}
