package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class GetPerspectiveLayoutAction implements ProjectAction<GetPerspectiveLayoutResult>, HasProjectId {

    private ProjectId projectId;

    private UserId userId;

    private PerspectiveId perspectiveId;

    private GetPerspectiveLayoutAction() {
    }

    public GetPerspectiveLayoutAction(ProjectId projectId, UserId userId, PerspectiveId perspectiveId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.perspectiveId = checkNotNull(perspectiveId);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getUserId() {
        return userId;
    }

    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId, perspectiveId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPerspectiveLayoutAction)) {
            return false;
        }
        GetPerspectiveLayoutAction other = (GetPerspectiveLayoutAction) obj;
        return this.perspectiveId.equals(other.perspectiveId)
                && this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetPerspectiveLayoutAction")
                .addValue(projectId)
                .addValue(userId)
                .addValue(perspectiveId)
                .toString();
    }
}
