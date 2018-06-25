package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
    public void save(EntityTags tag) {
        delegate.save(tag);
    }

    @Override
    public void addTag(ProjectId projectId, OWLEntity entity, TagId tagId) {
        delegate.addTag(projectId, entity, tagId);
    }

    @Override
    public void removeTag(ProjectId projectId, OWLEntity entity, TagId tagId) {
        delegate.removeTag(projectId, entity, tagId);
    }

    @Override
    public void removeTag(ProjectId projectId, TagId tagId) {
        delegate.removeTag(projectId, tagId);
    }

    @Override
    public Map<OWLEntity, EntityTags> findByProject(ProjectId projectId) {
        return delegate.findByProject(projectId);
    }

    @Override
    public Optional<EntityTags> findByEntity(ProjectId projectId, OWLEntity entity) {
        return delegate.findByEntity(projectId, entity);
    }

    @Override
    public Collection<EntityTags> findByTagId(TagId tagId) {
        return delegate.findByTagId(tagId);
    }
}
