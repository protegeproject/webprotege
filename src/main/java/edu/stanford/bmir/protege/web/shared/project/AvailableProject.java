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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 */
public class AvailableProject implements IsSerializable, Comparable<AvailableProject>, HasProjectId {

    private ProjectDetails projectDetails;

    private boolean downloadable;

    private boolean trashable;

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
     *                  the current user.
     */
    public AvailableProject(@Nonnull ProjectDetails projectDetails,
                            boolean downloadable,
                            boolean trashable) {
        this.projectDetails = checkNotNull(projectDetails);
        this.downloadable = downloadable;
        this.trashable = trashable;
    }

    @Override
    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }

    public UserId getOwner() {
        return projectDetails.getOwner();
    }

    public String getDescription() {
        return projectDetails.getDescription();
    }

    public boolean isInTrash() {
        return projectDetails.isInTrash();
    }

    public long getCreatedAt() {
        return projectDetails.getCreatedAt();
    }

    public UserId getCreatedBy() {
        return projectDetails.getCreatedBy();
    }

    public long getLastModifiedAt() {
        return projectDetails.getLastModifiedAt();
    }

    public UserId getLastModifiedBy() {
        return projectDetails.getLastModifiedBy();
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public boolean isTrashable() {
        return trashable;
    }

    public String getDisplayName() {
        return projectDetails.getDisplayName();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectDetails, downloadable, trashable);
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
                && this.trashable == other.trashable;
    }


    @Override
    public String toString() {
        return toStringHelper("AvailableProject")
                .addValue(projectDetails)
                .add("downloadable", downloadable)
                .add("trashable", trashable)
                .toString();
    }

    @Override
    public int compareTo(AvailableProject o) {
        return this.projectDetails.compareTo(o.getProjectDetails());
    }
}
