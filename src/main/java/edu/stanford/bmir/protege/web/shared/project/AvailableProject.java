package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Comparator.comparing;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 *
 * Represents information about a project that is available (viewable) for a given user.
 */
public class AvailableProject implements IsSerializable, Comparable<AvailableProject>, HasProjectId {

    public static final long UNKNOWN = 0;

    private ProjectDetails projectDetails;

    private boolean downloadable;

    private boolean trashable;

    private long lastOpened;

    private static transient Comparator<AvailableProject> comparator = comparing(AvailableProject::getProjectDetails);

    @GwtSerializationConstructor
    private AvailableProject() {
    }

    /**
     * Captures the information about a project that is available for the current
     * user.
     * @param projectDetails  The project details.
     * @param downloadable A flag indicating whether the project is downloadable by the current
     *                     user (in the current session).
     * @param trashable A flag indicating whether the project can be moved to the trash by
     * @param lastOpenedTimestamp A time stamp of when the project was last opened by the current
     *                            user.  A zero or negative value indicates unknown.
     */
    public AvailableProject(@Nonnull ProjectDetails projectDetails,
                            boolean downloadable,
                            boolean trashable,
                            long lastOpenedTimestamp) {
        this.projectDetails = checkNotNull(projectDetails);
        this.downloadable = downloadable;
        this.trashable = trashable;
        this.lastOpened = lastOpenedTimestamp;
    }

    /**
     * Gets the {@link ProjectId}
     * @return The {@link ProjectId}
     */
    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }


    /**
     * Gets the display name of the project.
     * @return A string representing the display name.
     */
    @Nonnull
    public String getDisplayName() {
        return projectDetails.getDisplayName();
    }


    /**
     * Gets the owner of the project.
     * @return The owner of the project represented by a {@link UserId}.
     */
    @Nonnull
    public UserId getOwner() {
        return projectDetails.getOwner();
    }

    /**
     * Gets the project description.
     * @return A possibly empty string representing the project description.
     */
    @Nonnull
    public String getDescription() {
        return projectDetails.getDescription();
    }

    /**
     * Determines whether this project is in the trash or not.
     * @return true if the project is in the trash, otherwise false.
     */
    public boolean isInTrash() {
        return projectDetails.isInTrash();
    }

    /**
     * Gets the timestamp of when the project was created.
     * @return A long representing the timestamp.
     */
    public long getCreatedAt() {
        return projectDetails.getCreatedAt();
    }

    /**
     * Get the user who created the project.
     * @return A {@link UserId} representing the user who created the project.
     */
    @Nonnull
    public UserId getCreatedBy() {
        return projectDetails.getCreatedBy();
    }

    /**
     * Gets the timestamp of when the project was last modified.
     * @return A long representing a timestamp.
     */
    public long getLastModifiedAt() {
        return projectDetails.getLastModifiedAt();
    }

    /**
     * Get the id of the user who last modified the project.
     * @return A {@link UserId} identifying the user who last modified the project.
     */
    @Nonnull
    public UserId getLastModifiedBy() {
        return projectDetails.getLastModifiedBy();
    }

    /**
     * Gets all of the project details as a ProjectDetails object.
     * @return The details as a {@link ProjectDetails} object.
     */
    @Nonnull
    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    /**
     * Determines if this project is downloadable (by the current user).
     * @return true if the project is downloadable, otherwise false.
     */
    public boolean isDownloadable() {
        return downloadable;
    }


    /**
     * Determines if this project is trashable (by the current user).
     * @return true if the project is trashable, otherwise false.
     */
    public boolean isTrashable() {
        return trashable;
    }

    /**
     * Gets the timestamp of when the project was last opened by the current user.
     * @return The timestamp.  A value of 0 or a negative value indicated unknown.
     */
    public long getLastOpenedAt() {
        return lastOpened;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectDetails, downloadable, trashable, lastOpened);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AvailableProject)) {
            return false;
        }
        AvailableProject other = (AvailableProject) obj;
        return this.projectDetails.equals(other.projectDetails)
                && this.downloadable == other.downloadable
                && this.trashable == other.trashable
                && this.lastOpened == other.lastOpened;
    }


    @Override
    public String toString() {
        return toStringHelper("AvailableProject")
                .addValue(projectDetails)
                .add("downloadable", downloadable)
                .add("trashable", trashable)
                .add("lastOpened", lastOpened)
                .toString();
    }

    @Override
    public int compareTo(AvailableProject o) {
        return comparator.compare(this, o);
    }
}
