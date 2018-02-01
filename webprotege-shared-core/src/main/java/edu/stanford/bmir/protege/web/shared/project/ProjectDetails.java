package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
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

    private long createdAt;

    private UserId createdBy;

    private long lastModifiedAt;

    private UserId lastModifiedBy;

    @GwtSerializationConstructor
    private ProjectDetails() {

    }

    /**
     * Constructs a {@link ProjectDetails} object.
     * @param projectId The {@link ProjectId} that identifies the project which these details describe.
     * @param displayName The human readable name for the project.  Not {@code null}.
     * @param owner The owner of the project. Not {@code null}.
     * @param description A description of the project.  Not {@code null}. May be empty.
     * @param inTrash A flag that specifies whether the project is in the trash.
     * @param createdAt A timestamp that specifies when the project was created.  A zero value stands for unknown.
     * @param createdBy A {@link UserId} that identifies the user that created the project.
     * @param lastModifiedAt A timestamp that specifies when the project was last modified.  A zero value indicates
     *                       unknown.
     * @param lastModifiedBy A {@link UserId} that identifies the user that last modified the project.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public ProjectDetails(@Nonnull ProjectId projectId,
                          @Nonnull String displayName,
                          @Nonnull String description,
                          @Nonnull UserId owner,
                          boolean inTrash,
                          long createdAt,
                          @Nonnull UserId createdBy,
                          long lastModifiedAt,
                          @Nonnull UserId lastModifiedBy) {
        this.projectId = checkNotNull(projectId);
        this.displayName = checkNotNull(displayName);
        this.owner = checkNotNull(owner);
        this.description = checkNotNull(description);
        this.inTrash = inTrash;
        this.createdAt = createdAt;
        this.createdBy = checkNotNull(createdBy);
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = checkNotNull(lastModifiedBy);
    }

    /**
     * Gets the {@link ProjectId} of the project that these details describe.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the human readable name for the project.
     * @return The human readable name.  Not {@code null}.
     */
    @Nonnull
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the {@link UserId} that identifies the owner of the project.
     * @return The {@link UserId} for the project described by these details.  Not {@code null}.
     */
    @Nonnull
    public UserId getOwner() {
        return owner;
    }

    /**
     * Gets the description of the project described by these details.
     * @return The description as a string.  Not {@code null}.  May be empty.
     */
    @Nonnull
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

    /**
     * Gets the timestamp of when the project was created.
     * @return A timestamp.  A value of zero denotes unknown.
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the user who created the project.
     * @return A {@link UserId} of the user who created the project.
     */
    @Nonnull
    public UserId getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets the timestamp of when the project was modified.
     * @return A timestamp.  A value of zero denotes unknown.
     */
    public long getLastModifiedAt() {
        return lastModifiedAt;
    }

    /**
     * Gets the user who last modified the project.
     * @return A {@link UserId} of the user who last modified the project.
     */
    @Nonnull
    public UserId getLastModifiedBy() {
        return lastModifiedBy;
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
        return this.projectId.equals(other.projectId)
                && this.displayName.equals(other.displayName)
                && this.description.equals(other.description)
                && this.owner.equals(other.owner)
                && this.inTrash == other.inTrash
                && this.lastModifiedAt == other.lastModifiedAt
                && this.lastModifiedBy.equals(other.lastModifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                projectId,
                displayName,
                description,
                owner,
                inTrash,
                lastModifiedAt,
                lastModifiedBy
        );
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
        return toStringHelper("ProjectDetails")
                          .addValue(projectId)
                          .add("displayName", displayName)
                          .add("description", description)
                          .add("owner", owner)
                          .add("inTrash", inTrash)
                          .add("lastModifiedAt", lastModifiedAt)
                          .add("lastModifiedBy", lastModifiedBy)
                          .toString();
    }


    public static Builder builder(ProjectId projectId, UserId owner, String displayName, String description) {
        return new Builder(projectId, owner, displayName, description);
    }

    public Builder builder() {
        return new Builder(projectId,
                           owner,
                           displayName,
                           description,
                           inTrash,
                           createdAt,
                           createdBy,
                           lastModifiedAt,
                           lastModifiedBy);
    }


    public static class Builder {

        private ProjectId projectId;

        private UserId owner;

        private String displayName;

        private String description;

        private boolean inTrash;

        private long createdAt;

        private UserId createdBy;

        private long lastModifiedAt;

        private UserId lastModifiedBy;

        public Builder(ProjectId projectId, UserId owner, String displayName, String description) {
            this.projectId = projectId;
            this.owner = owner;
            this.displayName = displayName;
            this.description = description;
            this.lastModifiedAt = 0;
            this.lastModifiedBy = owner;
        }

        public Builder(ProjectId projectId,
                       UserId owner,
                       String displayName,
                       String description,
                       boolean inTrash,
                       long createdAt,
                       UserId createdBy,
                       long lastModifiedAt,
                       UserId lastModifiedBy) {
            this.projectId = projectId;
            this.owner = owner;
            this.displayName = displayName;
            this.description = description;
            this.inTrash = inTrash;
            this.createdAt = createdAt;
            this.createdBy = createdBy;
            this.lastModifiedAt = lastModifiedAt;
            this.lastModifiedBy = lastModifiedBy;
        }

        public UserId getOwner() {
            return owner;
        }

        public Builder setOwner(UserId owner) {
            this.owner = owner;
            return this;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public boolean isInTrash() {
            return inTrash;
        }

        public Builder setInTrash(boolean inTrash) {
            this.inTrash = inTrash;
            return this;
        }

        public Builder setLastModifiedAt(long lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return this;
        }

        public Builder setLastModifiedBy(UserId lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return this;
        }

        public Builder setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setCreatedBy(UserId createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ProjectDetails build() {
            return new ProjectDetails(projectId, displayName, description, owner, inTrash,
                                      createdAt, createdBy, lastModifiedAt, lastModifiedBy);
        }
    }
}
