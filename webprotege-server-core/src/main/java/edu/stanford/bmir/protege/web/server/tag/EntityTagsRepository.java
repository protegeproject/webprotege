package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 *
 * Represents the entity tags for a given project.
 */
@ProjectSingleton
public interface EntityTagsRepository {

    void save(@Nonnull EntityTags tag);

    void addTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId);

    void removeTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId);

    void removeTag(@Nonnull TagId tagId);

    @Nonnull
    Optional<EntityTags> findByEntity(@Nonnull OWLEntity entity);

    @Nonnull
    Collection<EntityTags> findByTagId(@Nonnull TagId tagId);
}
