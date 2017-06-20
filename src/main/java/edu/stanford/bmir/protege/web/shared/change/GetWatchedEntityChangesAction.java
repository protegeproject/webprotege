package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class GetWatchedEntityChangesAction implements ProjectAction<GetWatchedEntityChangesResult>, HasProjectId {

    private ProjectId projectId;

    private UserId userId;

    private GetWatchedEntityChangesAction() {
    }

    public GetWatchedEntityChangesAction(ProjectId projectId, UserId userId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

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
        if (!(obj instanceof GetWatchedEntityChangesAction)) {
            return false;
        }
        GetWatchedEntityChangesAction other = (GetWatchedEntityChangesAction) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetWatchedEntityChangesAction")
                .addValue(projectId)
                .addValue(userId)
                .toString();
    }
}
