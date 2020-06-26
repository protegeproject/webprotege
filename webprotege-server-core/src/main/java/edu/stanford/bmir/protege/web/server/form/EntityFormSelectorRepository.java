package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public interface EntityFormSelectorRepository extends Repository {

    void save(EntityFormSelector entityFormSelector);

    Stream<EntityFormSelector> findFormSelectors(@Nonnull ProjectId projectId);
}
