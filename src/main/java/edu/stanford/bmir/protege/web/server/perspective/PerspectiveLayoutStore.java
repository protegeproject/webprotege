package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public interface PerspectiveLayoutStore {

    /**
     * Gets the perspective layout for the specified project, user and perspective.
     * @param projectId The id of the project.
     * @param userId The id of the user.
     * @param perspectiveId The id of the perspective.
     * @return A perspective layout for the specified perspective for the specified user
     * and project.  If the user has not customised a layout for the specified perspective
     * in the specified project then the layout returned by {@link #getPerspectiveLayout(ProjectId, PerspectiveId)}
     * will be returned.
     */
    @Nonnull
    PerspectiveLayout getPerspectiveLayout(@Nonnull ProjectId projectId,
                                           @Nonnull UserId userId,
                                           @Nonnull PerspectiveId perspectiveId);

    @Nonnull
    PerspectiveLayout getPerspectiveLayout(@Nonnull ProjectId projectId,
                                           @Nonnull PerspectiveId perspectiveId);

    void setPerspectiveLayout(@Nonnull ProjectId projectId,
                              @Nonnull UserId userId,
                              @Nonnull PerspectiveLayout layout);

    void clearPerspectiveLayout(@Nonnull ProjectId projectId,
                                @Nonnull UserId userId,
                                @Nonnull PerspectiveId perspectiveId);

}
