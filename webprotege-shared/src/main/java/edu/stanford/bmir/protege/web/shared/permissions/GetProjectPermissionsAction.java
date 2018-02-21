package edu.stanford.bmir.protege.web.shared.permissions;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class GetProjectPermissionsAction implements Action<GetProjectPermissionsResult>, HasProjectId, HasUserId {

    private ProjectId projectId;

    private UserId userId;

    @GwtSerializationConstructor
    private GetProjectPermissionsAction() {
    }

    public GetProjectPermissionsAction(@Nonnull ProjectId projectId,
                                       @Nonnull UserId userId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    @Override
    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectPermissionsAction)) {
            return false;
        }
        GetProjectPermissionsAction other = (GetProjectPermissionsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetPermissionsAction")
                .addValue(projectId)
                .addValue(userId)
                .toString();
    }
}
