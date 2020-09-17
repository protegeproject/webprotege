package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public interface EntityFormRepository extends Repository {

    void deleteFormDescriptor(@Nonnull ProjectId projectId,
                              @Nonnull FormId formId);

    /**
     * Saves the specified form descriptor.  If a form descriptor with the {@link FormId}
     * already exists then it will be updated.
     * @param projectId The project for which the form descriptor should be set.
     * @param formDescriptor The form descriptor.
     */
    void saveFormDescriptor(@Nonnull ProjectId projectId,
                            @Nonnull FormDescriptor formDescriptor);

    void setProjectFormDescriptors(@Nonnull ProjectId projectId,
                                   @Nonnull List<FormDescriptor> formDescriptors);

    Stream<FormDescriptor> findFormDescriptors(@Nonnull ProjectId projectId);

    Stream<FormDescriptor> findFormDescriptors(@Nonnull ImmutableSet<FormId> formIds,
                                               @Nonnull ProjectId projectId);

    Optional<FormDescriptor> findFormDescriptor(@Nonnull ProjectId projectId,
                                                @Nonnull FormId formId);

}
