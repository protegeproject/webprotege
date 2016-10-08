package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class EntityDiscussionThreadRepository {

    @Nonnull
    private final Datastore datastore;

    @Inject
    public EntityDiscussionThreadRepository(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    public List<EntityDiscussionThread> findThreads(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        datastore.createQuery(EntityDiscussionThread.class);
        return datastore.find(EntityDiscussionThread.class)
//                        .field("projectId").equal(projectId)
                        .field("entity").equal(entity)
                        .asList();
    }

    public void saveThread(EntityDiscussionThread thread) {
        datastore.save(thread);
    }

    public void addCommentToThread(ThreadId threadId, Comment comment) {
        Query<EntityDiscussionThread> query = createQueryForThread(threadId);
        UpdateOperations<EntityDiscussionThread> ops = getUpdateOperations().add("comments", comment);
        datastore.update(query, ops, false);
    }

    public void setThreadStatus(ThreadId threadId, Status status) {
        datastore.updateFirst(createQueryForThread(threadId), getUpdateOperations().set("status", status));
    }


    public Optional<EntityDiscussionThread> getThread(ThreadId id) {
        return Optional.ofNullable(datastore.find(EntityDiscussionThread.class)
                                            .field("_id").equal(id)
                                            .get());
    }


    private UpdateOperations<EntityDiscussionThread> getUpdateOperations() {
        return datastore.createUpdateOperations(EntityDiscussionThread.class);
    }

    private Query<EntityDiscussionThread> createQueryForThread(ThreadId threadId) {
        return datastore.createQuery(EntityDiscussionThread.class)
                        .field("_id").equal(threadId);
    }
}
