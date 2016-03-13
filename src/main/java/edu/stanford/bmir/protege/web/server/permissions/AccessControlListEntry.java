package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@Document(collection = "AccessControlList")
@TypeAlias("AccessControlListEntry")
public class AccessControlListEntry {

    private final ProjectId projectId;

    private final UserId userId;

    private final Permission permission;

    public AccessControlListEntry(ProjectId projectId, UserId userId, Permission permission) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.permission = checkNotNull(permission);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getUserId() {
        return userId;
    }

    public Permission getPermission() {
        return permission;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId, permission);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AccessControlListEntry)) {
            return false;
        }
        AccessControlListEntry other = (AccessControlListEntry) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.permission.equals(other.permission);
    }


    @Override
    public String toString() {
        return toStringHelper("AccessControlListEntry")
                .addValue(projectId)
                .addValue(userId)
                .addValue(permission)
                .toString();
    }
}
