package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 */
public class EntityTagsRepositoryCachingImpl implements EntityTagsRepository, Repository {

    @Nonnull
    private final EntityTagsRepositoryImpl delegate;

    public EntityTagsRepositoryCachingImpl(@Nonnull EntityTagsRepositoryImpl delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public void ensureIndexes() {
        delegate.ensureIndexes();
    }

    @Override
    public void save(@Nonnull EntityTags tag) {
        delegate.save(tag);
    }

    @Override
    public void addTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId) {
        delegate.addTag(entity, tagId);
    }

    @Override
    public void removeTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId) {
        delegate.removeTag(entity, tagId);
    }

    @Override
    public void removeTag(@Nonnull TagId tagId) {
        delegate.removeTag(tagId);
    }

    @Nonnull
    @Override
    public Map<OWLEntity, EntityTags> findAll() {
        return delegate.findAll();
    }

    @Nonnull
    @Override
    public Optional<EntityTags> findByEntity(@Nonnull OWLEntity entity) {
        return delegate.findByEntity(entity);
    }

    @Nonnull
    @Override
    public Collection<EntityTags> findByTagId(@Nonnull TagId tagId) {
        return delegate.findByTagId(tagId);
    }
}
