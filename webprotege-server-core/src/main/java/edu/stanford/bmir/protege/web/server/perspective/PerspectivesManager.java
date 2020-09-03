package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public interface PerspectivesManager {

    @Nonnull
    ImmutableList<PerspectiveDescriptor> getPerspectives(@Nonnull ProjectId projectId, @Nonnull UserId userId);

    void setPerspectives(@Nonnull ImmutableList<PerspectiveDescriptor> perspectives);

    void setPerspectives(@Nonnull ProjectId projectId, @Nonnull ImmutableList<PerspectiveDescriptor> perspectives);

    void setPerspectives(@Nonnull ProjectId projectId,
                         @Nonnull UserId userId,
                         @Nonnull ImmutableList<PerspectiveDescriptor> perspectives);

    @Nonnull
    PerspectiveLayout getPerspectiveLayout(@Nonnull ProjectId projectId,
                                           @Nonnull UserId userId,
                                           @Nonnull PerspectiveId perspectiveId);

    void savePerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveLayout layout);

    void savePerspectiveLayout(@Nonnull ProjectId projectId, @Nonnull PerspectiveLayout layout);

    void savePerspectiveLayout(@Nonnull PerspectiveLayout layout);

    void resetPerspectiveLayout(@Nonnull ProjectId projectId,
                                @Nonnull UserId userId,
                                @Nonnull PerspectiveId perspectiveId);

    @Nonnull
    ImmutableSet<PerspectiveId> getResettablePerspectiveIds(@Nonnull ProjectId projectId,
                                                            @Nonnull UserId userId);

    /**
     * Saves the specified list of perspectives as the project defaults, copying
     * the layouts for the perspectives.
     * @param projectId The project
     * @param perspectiveDescriptors the descriptors of the perspectives
     * @param userId The user id from which to copy the perspective layouts
     */
    void savePerspectivesAsProjectDefault(@Nonnull ProjectId projectId,
                                          @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveDescriptors,
                                          UserId userId);

    @Nonnull
    ImmutableList<PerspectiveDetails> getPerspectiveDetails(@Nonnull ProjectId projectId,
                                                            @Nonnull UserId userId);

    void resetPerspectives(@Nonnull ProjectId projectId, @Nonnull UserId userId);
}