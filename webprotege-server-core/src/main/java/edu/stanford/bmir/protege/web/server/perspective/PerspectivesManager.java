package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
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
}