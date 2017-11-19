package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
public class ResetPerspectiveLayoutAction implements ProjectAction<ResetPerspectiveLayoutResult> {

    private ProjectId projectId;

    private PerspectiveId perspectiveId;

    @GwtSerializationConstructor
    private ResetPerspectiveLayoutAction() {
    }

    /**
     * Requests that the perspective identified by the specified id in the specified project is reset to
     * the default for the current user.
     * @param projectId The project id.
     * @param perspectiveId The perspective id.
     */
    public ResetPerspectiveLayoutAction(@Nonnull ProjectId projectId,
                                        @Nonnull PerspectiveId perspectiveId) {
        this.projectId = checkNotNull(projectId);
        this.perspectiveId = checkNotNull(perspectiveId);
    }

    public static ResetPerspectiveLayoutAction resetPerspective(@Nonnull ProjectId projectId,
                                                                @Nonnull PerspectiveId perspectiveId) {
        return new ResetPerspectiveLayoutAction(projectId, perspectiveId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ResetPerspectiveLayoutAction)) {
            return false;
        }
        ResetPerspectiveLayoutAction other = (ResetPerspectiveLayoutAction) obj;
        return this.projectId.equals(other.projectId)
                && this.perspectiveId.equals(other.perspectiveId);
    }


    @Override
    public String toString() {
        return toStringHelper("ResetPerspectiveLayoutAction" )
                .addValue(projectId)
                .toString();
    }
}
