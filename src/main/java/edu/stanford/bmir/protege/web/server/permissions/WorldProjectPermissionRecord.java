package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.annotation.Id;
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
public class WorldProjectPermissionRecord {

    @Indexed
    private final ProjectId projectId;

    private final Permission permission;

    public WorldProjectPermissionRecord(ProjectId projectId, Permission permission) {
        this.projectId = checkNotNull(projectId);
        this.permission = checkNotNull(permission);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public Permission getPermission() {
        return permission;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, permission);
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
                && this.permission.equals(other.permission);
    }


    @Override
    public String toString() {
        return toStringHelper("WorldProjectPermissionRecord")
                .addValue(projectId)
                .addValue(permission)
                .toString();
    }
}
