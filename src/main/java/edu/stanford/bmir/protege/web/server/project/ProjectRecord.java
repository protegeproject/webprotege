package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@Document(collection = "ProjectRecords")
public class ProjectRecord {

    @Id
    @Indexed(unique = true)
    private final ProjectId projectId;

    private final UserId owner;

    private final String displayName;

    private final String description;

    private final boolean inTrash;

    public ProjectRecord(ProjectId projectId, UserId owner, String displayName, String description, boolean inTrash) {
        this.projectId = checkNotNull(projectId);
        this.owner = checkNotNull(owner);
        this.displayName = checkNotNull(displayName);
        this.description = checkNotNull(description);
        this.inTrash = inTrash;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getOwner() {
        return owner;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInTrash() {
        return inTrash;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                projectId,
                owner,
                displayName,
                description,
                inTrash
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectRecord)) {
            return false;
        }
        ProjectRecord other = (ProjectRecord) obj;
        return this.projectId.equals(other.projectId)
                && this.owner.equals(other.owner)
                && this.displayName.equals(other.displayName)
                && this.description.equals(other.description)
                && this.inTrash == other.isInTrash();
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectRecord")
                .addValue(projectId)
                .addValue(owner)
                .addValue(displayName)
                .addValue(description)
                .addValue(inTrash)
                .toString();
    }


}
