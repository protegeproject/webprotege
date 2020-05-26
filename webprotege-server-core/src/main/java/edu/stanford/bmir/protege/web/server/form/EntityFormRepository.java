package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
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
@ApplicationSingleton
public interface EntityFormRepository extends Repository {

    void deleteFormDescriptor(@Nonnull ProjectId projectId,
                              @Nonnull FormId formId);

    void saveFormDescriptor(@Nonnull ProjectId projectId,
                            @Nonnull FormDescriptor formDescriptor);

    void setProjectFormDescriptors(@Nonnull ProjectId projectId,
                                   @Nonnull List<FormDescriptor> formDescriptors);

    Stream<FormDescriptor> findFormDescriptors(@Nonnull ProjectId projectId);

    Optional<FormDescriptor> findFormDescriptor(@Nonnull ProjectId projectId,
                                                @Nonnull FormId formId);

}
