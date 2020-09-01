package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveCoordinates;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-31
 */
public interface PerspectiveDescriptorRepository extends Repository {

    void saveDescriptors(@Nonnull List<PerspectiveDescriptorRecord> perspectiveDescriptors);


    /**
     * Find the {@link PerspectiveDescriptor}s that are specific to a project and a user.
     * @param projectId The project
     * @param userId The user
     */
    @Nonnull
    ImmutableList<PerspectiveDescriptorRecord> findDescriptors(@Nonnull ProjectId projectId,
                                                         @Nonnull UserId userId);

    /**
     * Find the {@link PerspectiveDescriptor}s that are specific to a project.  There are not
     * user specific descriptors.
     * @param projectId The project
     */
    @Nonnull
    ImmutableList<PerspectiveDescriptorRecord> findDescriptors(@Nonnull ProjectId projectId);

    /**
     * Find the perspective descriptors that are not project and/or user specific.  These
     * are essentially the descriptors for the default perspectives for new projects.
     */
    @Nonnull
    ImmutableList<PerspectiveDescriptorRecord> findDescriptors();
}
