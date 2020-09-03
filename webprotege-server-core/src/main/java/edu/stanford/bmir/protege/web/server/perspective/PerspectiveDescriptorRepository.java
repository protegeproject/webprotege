package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
public interface PerspectiveDescriptorRepository extends Repository {

    void saveDescriptors(@Nonnull PerspectiveDescriptorsRecord perspectiveDescriptors);


    /**
     * Find the {@link PerspectiveDescriptor}s that are specific to a project and a user.
     * @param projectId The project
     * @param userId The user
     */
    @Nonnull
    Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId,
                                                           @Nonnull UserId userId);

    /**
     * Find the {@link PerspectiveDescriptor}s that are specific to a project.  There are not
     * user specific descriptors.
     * @param projectId The project
     */
    @Nonnull
    Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId);

    /**
     * Find the perspective descriptors that are not project and/or user specific.  These
     * are essentially the descriptors for the default perspectives for new projects.
     */
    @Nonnull
    Optional<PerspectiveDescriptorsRecord> findDescriptors();

    /**
     * Find the perspective descriptors that are not user specific.  This includes project level
     * desecriptors and system level descriptors.
     * @param projectId The project id
     */
    @Nonnull
    Stream<PerspectiveDescriptorsRecord> findProjectAndSystemDescriptors(@Nonnull ProjectId projectId);

    void dropAllDescriptors(@Nonnull ProjectId projectId, @Nonnull UserId userId);
}
