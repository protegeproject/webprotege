package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public interface FormTriggerRepository extends Repository {

    void save(FormSelector formSelector);

    Stream<FormSelector> findFormTriggers(@Nonnull ProjectId projectId);
}
