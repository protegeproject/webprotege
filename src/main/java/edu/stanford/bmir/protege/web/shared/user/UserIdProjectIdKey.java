package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/01/16
 */
public class UserIdProjectIdKey {

    private final UserId userId;

    private final ProjectId projectId;

    public UserIdProjectIdKey(UserId userId, ProjectId projectId) {
        this.userId = checkNotNull(userId);
        this.projectId = checkNotNull(projectId);
    }

    public UserId getUserId() {
        return userId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserIdProjectIdKey)) {
            return false;
        }
        UserIdProjectIdKey other = (UserIdProjectIdKey) obj;
        return this.userId.equals(other.userId) && this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("UserIdProjectIdKey")
                .addValue(userId)
                .addValue(projectId)
                .toString();
    }
}
