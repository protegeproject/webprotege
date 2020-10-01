package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
public interface PerspectiveLayoutRepository extends Repository {

    @Nonnull
    Optional<PerspectiveLayoutRecord> findLayout(@Nonnull ProjectId projectId,
                                                 @Nonnull UserId userId,
                                                 @Nonnull PerspectiveId perspectiveId);

    @Nonnull
    Optional<PerspectiveLayoutRecord> findLayout(@Nonnull ProjectId projectId,
                                                 @Nonnull PerspectiveId perspectiveId);

    @Nonnull
    Optional<PerspectiveLayoutRecord> findLayout(@Nonnull PerspectiveId perspectiveId);

    void saveLayout(PerspectiveLayoutRecord record);

    void saveLayouts(@Nonnull List<PerspectiveLayoutRecord> layout);

    void dropLayout(@Nonnull ProjectId projectId,
                    @Nonnull UserId userId,
                    @Nonnull PerspectiveId perspectiveId);

    void dropAllLayouts(@Nonnull ProjectId projectId, @Nonnull UserId userId);
}
