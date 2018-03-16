package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
public class EntityTagsRepository implements Repository {

    @Nonnull
    private final Datastore datastore;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public EntityTagsRepository(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(EntityTags.class);
    }

    public void save(EntityTags tag) {
        try {
            writeLock.lock();
            datastore.delete(tagWithProjectIdAndEntity(tag.getProjectId(), tag.getEntity()));
            datastore.save(tag);
        } finally {
            writeLock.unlock();
        }
    }

    public void addTag(ProjectId projectId, OWLEntity entity, TagId tagId) {
        try {
            writeLock.lock();
            Query<EntityTags> query = tagWithProjectIdAndEntity(projectId, entity);
            UpdateOperations<EntityTags> updateOps = datastore.createUpdateOperations(EntityTags.class);
            updateOps.addToSet("tags", tagId);
            datastore.update(query, updateOps);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeTag(ProjectId projectId, OWLEntity entity, TagId tagId) {
        try {
            writeLock.lock();
            Query<EntityTags> query = tagWithProjectIdAndEntity(projectId, entity);
            UpdateOperations<EntityTags> updateOps = datastore.createUpdateOperations(EntityTags.class);
            updateOps.removeAll("tags", tagId);
            datastore.update(query, updateOps);
        } finally {
            writeLock.unlock();
        }
    }

    private Query<EntityTags> tagWithProjectIdAndEntity(ProjectId projectId, OWLEntity entity) {
         return datastore.createQuery(EntityTags.class)
                     .field("projectId").equal(projectId)
                     .field("entity").equal(entity);
    }

    public List<EntityTags> findByEntity(ProjectId projectId, OWLEntity entity) {
        try {
            readLock.lock();
            return tagWithProjectIdAndEntity(projectId, entity).asList();
        } finally {
            readLock.unlock();
        }
    }

    public List<EntityTags> findByTagId(TagId tagId) {
        try {
            readLock.lock();
            return datastore.find(EntityTags.class)
                            .field("tags")
                            .equal(tagId).asList();
        } finally {
            readLock.unlock();
        }
    }

}
