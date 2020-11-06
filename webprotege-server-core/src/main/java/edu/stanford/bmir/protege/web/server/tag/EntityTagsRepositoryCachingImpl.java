package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 */
@ProjectSingleton
public class EntityTagsRepositoryCachingImpl implements EntityTagsRepository, Repository {

    @Nonnull
    private final Map<OWLEntity, EntityTags> cache = new HashMap<>();

    @Nonnull
    private final EntityTagsRepositoryImpl delegate;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private boolean empty = false;

    public EntityTagsRepositoryCachingImpl(@Nonnull EntityTagsRepositoryImpl delegate) {
        this.delegate = checkNotNull(delegate);
    }

    public void preloadCache() {
        writeLock.lock();
        try {
            cache.clear();
            delegate.findAll().forEach(cache::put);
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void ensureIndexes() {
        delegate.ensureIndexes();
        empty = delegate.countTaggedEntities() == 0;
    }

    @Override
    public void save(@Nonnull EntityTags tag) {
        writeLock.lock();
        try {
            delegate.save(tag);
            cache.put(tag.getEntity(), tag);
            empty = false;
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void addTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId) {
        writeLock.lock();
        try {
            delegate.addTag(entity, tagId);
            cache.remove(entity);
            delegate.findByEntity(entity).ifPresent(tag -> cache.put(entity, tag));
            empty = false;
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void removeTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId) {
        writeLock.lock();
        try {
            delegate.removeTag(entity, tagId);
            cache.remove(entity);
            delegate.findByEntity(entity).ifPresent(tag -> cache.put(entity, tag));
            empty = delegate.countTaggedEntities() != 0;
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void removeTag(@Nonnull TagId tagId) {
        writeLock.lock();
        try {
            delegate.removeTag(tagId);
            delegate.findByTagId(tagId)
                    .forEach(tag -> cache.put(tag.getEntity(), tag));
            empty = delegate.countTaggedEntities() != 0;
        } finally {
            writeLock.unlock();
        }

    }

    @Nonnull
    @Override
    public Optional<EntityTags> findByEntity(@Nonnull OWLEntity entity) {
        readLock.lock();
        try {
            if(empty) {
                return Optional.empty();
            }
            return Optional.ofNullable(cache.get(entity));
        } finally {
            readLock.unlock();
        }

    }

    @Nonnull
    @Override
    public Collection<EntityTags> findByTagId(@Nonnull TagId tagId) {
        readLock.lock();
        try {
            if(empty) {
                return Collections.emptySet();
            }
            return delegate.findByTagId(tagId);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public long countTaggedEntities() {
        try {
            readLock.lock();
            return delegate.countTaggedEntities();
        } finally {
            writeLock.lock();
        }
    }
}
