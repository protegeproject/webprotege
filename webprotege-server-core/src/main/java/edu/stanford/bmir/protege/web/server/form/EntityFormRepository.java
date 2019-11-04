package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public interface EntityFormRepository extends Repository {

    void saveFormDescriptor(@Nonnull ProjectId projectId,
                            @Nonnull FormDescriptor formDescriptor);

    Stream<FormDescriptor> findFormDescriptors(@Nonnull ProjectId projectId);

    Optional<FormDescriptor> findFormDescriptor(@Nonnull ProjectId projectId,
                                                @Nonnull FormId formId);

}
