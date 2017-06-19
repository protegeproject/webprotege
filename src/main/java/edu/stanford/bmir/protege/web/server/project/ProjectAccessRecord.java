package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Mar 2017
 */
@Entity(value = "ProjectAccess", noClassnameStored = true)
@Indexes(
        {
                @Index(fields = {@Field("projectId"), @Field("userId")},
                       options = @IndexOptions(unique = true))
        }
)
public class ProjectAccessRecord {

    private final ProjectId projectId;

    private final UserId userId;

    private final Date timestamp;

    public ProjectAccessRecord(@Nonnull ProjectId projectId,
                               @Nonnull UserId userId,
                               long timestamp) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.timestamp = new Date(timestamp);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp.getTime();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectAccessRecord)) {
            return false;
        }
        ProjectAccessRecord other = (ProjectAccessRecord) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.timestamp.equals(other.timestamp);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectAccessRecord")
                .addValue(projectId)
                .addValue(userId)
                .add("ts", timestamp.getTime())
                .toString();
    }
}
