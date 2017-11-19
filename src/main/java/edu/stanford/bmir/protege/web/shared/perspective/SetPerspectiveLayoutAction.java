package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class SetPerspectiveLayoutAction implements ProjectAction<SetPerspectiveLayoutResult> {

    private ProjectId projectId;

    private UserId userId;

    private PerspectiveLayout layout;

    /**
     * For serialization only
     */
    private SetPerspectiveLayoutAction() {
    }

    public SetPerspectiveLayoutAction(ProjectId projectId, UserId userId, PerspectiveLayout layout) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.layout = checkNotNull(layout);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getUserId() {
        return userId;
    }

    public PerspectiveLayout getLayout() {
        return layout;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId, layout);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetPerspectiveLayoutAction)) {
            return false;
        }
        SetPerspectiveLayoutAction other = (SetPerspectiveLayoutAction) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.layout.equals(other.layout);
    }


    @Override
    public String toString() {
        return toStringHelper("SetPerspectiveLayoutAction")
                .addValue(projectId)
                .addValue(userId)
                .addValue(layout)
                .toString();
    }
}
