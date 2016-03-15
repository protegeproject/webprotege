package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
@Document(collection = "WorldProjectPermissionRecords")
@TypeAlias("WorldProjectPermissionRecord")
public class WorldProjectPermissionRecord {

    @Indexed(unique = true)
    private final ProjectId projectId;

    private final ImmutableSet<Permission> permissions;

    public WorldProjectPermissionRecord(ProjectId projectId, ImmutableSet<Permission> permissions) {
        this.projectId = checkNotNull(projectId);
        this.permissions = checkNotNull(permissions);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public ImmutableSet<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, permissions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WorldProjectPermissionRecord)) {
            return false;
        }
        WorldProjectPermissionRecord other = (WorldProjectPermissionRecord) obj;
        return this.projectId.equals(other.projectId)
                && this.permissions.equals(other.permissions);
    }


    @Override
    public String toString() {
        return toStringHelper("WorldProjectPermissionRecord")
                .addValue(projectId)
                .addValue(permissions)
                .toString();
    }
}
