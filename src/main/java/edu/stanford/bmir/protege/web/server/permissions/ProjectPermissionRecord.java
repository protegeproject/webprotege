package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public class ProjectPermissionRecord {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Optional<UserId> userId;

    @Nonnull
    private final ImmutableSet<Permission> permissions;

    public ProjectPermissionRecord(@Nonnull ProjectId projectId,
                                   @Nonnull Optional<UserId> userId,
                                   @Nonnull ImmutableSet<Permission> permissions) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.permissions = checkNotNull(ImmutableSet.copyOf(permissions));
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public Optional<UserId> getUserId() {
        return userId;
    }

    public ImmutableSet<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId, permissions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectPermissionRecord)) {
            return false;
        }
        ProjectPermissionRecord other = (ProjectPermissionRecord) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.permissions.equals(other.permissions);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectPermissionRecord")
                .addValue(projectId)
                .addValue(userId)
                .addValue(permissions)
                .toString();
    }
}
