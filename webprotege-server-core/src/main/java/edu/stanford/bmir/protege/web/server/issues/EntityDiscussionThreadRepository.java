package edu.stanford.bmir.protege.web.server.issues;

import com.mongodb.BasicDBObject;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@ApplicationSingleton
public class EntityDiscussionThreadRepository {

    public static final String MATCHED_COMMENT_PATH = "comments.$";

    @Nonnull
    private final Datastore datastore;

    @Inject
    public EntityDiscussionThreadRepository(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
        this.datastore.ensureIndexes();
    }

    public List<EntityDiscussionThread> findThreads(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity entity) {
        datastore.createQuery(EntityDiscussionThread.class);
        return datastore.find(EntityDiscussionThread.class)
                        .disableValidation()
                        .field(PROJECT_ID).equal(projectId)
                        .field(ENTITY).equal(entity)
                        .order("-comments.0.createdAt")
                        .asList();
    }

    public int getCommentsCount(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity) {
        return findThreads(projectId, entity).stream()
                                             .map(thread -> thread.getComments().size())
                                             .reduce((left, right) -> left + right)
                                             .orElse(0);
    }

    public int getOpenCommentsCount(@Nonnull ProjectId projectId,
                                    @Nonnull OWLEntity entity) {
        return datastore.createQuery(EntityDiscussionThread.class)
                        .disableValidation()
                        .field(PROJECT_ID).equal(projectId)
                        .field(ENTITY).equal(entity)
                        .field(STATUS).equal(Status.OPEN)
                        .asList()
                        .stream().map(thread -> thread.getComments().size())
                        .reduce((left, right) -> left + right)
                        .orElse(0);
    }

    public void saveThread(@Nonnull EntityDiscussionThread thread) {
        datastore.save(thread);
    }

    public void addCommentToThread(@Nonnull ThreadId threadId,
                                   @Nonnull Comment comment) {
        Query<EntityDiscussionThread> query = createQueryForThread(threadId);
        UpdateOperations<EntityDiscussionThread> ops = getUpdateOperations().push(COMMENTS, comment);
        datastore.update(query, ops, false);
    }

    public Optional<EntityDiscussionThread> setThreadStatus(@Nonnull ThreadId threadId,
                                                            @Nonnull Status status) {
        datastore.updateFirst(createQueryForThread(threadId), getUpdateOperations().set(STATUS, status));
        return Optional.ofNullable(datastore.get(EntityDiscussionThread.class, threadId));
    }


    public Optional<EntityDiscussionThread> getThread(@Nonnull ThreadId id) {
        return Optional.ofNullable(datastore.find(EntityDiscussionThread.class)
                                            .field("_id").equal(id)
                                            .get());
    }

    public void replaceEntity(ProjectId projectId, OWLEntity entity, OWLEntity withEntity) {
        Query<EntityDiscussionThread> query = datastore.find(EntityDiscussionThread.class)
                                                       .field(PROJECT_ID).equal(projectId)
                                                       .field(ENTITY).equal(entity);
        UpdateOperations<EntityDiscussionThread> updateOperations = datastore.createUpdateOperations(EntityDiscussionThread.class);
        updateOperations.set("entity", withEntity);
        datastore.update(query, updateOperations);
    }

    private UpdateOperations<EntityDiscussionThread> getUpdateOperations() {
        return datastore.createUpdateOperations(EntityDiscussionThread.class);
    }

    private Query<EntityDiscussionThread> createQueryForThread(ThreadId threadId) {
        return datastore.createQuery(EntityDiscussionThread.class)
                        .field("_id").equal(threadId);
    }

    public void updateComment(ThreadId id, Comment comment) {
        Query<EntityDiscussionThread> query = createQueryForThread(id)
                .field(COMMENTS_ID).equal(comment.getId());
        UpdateOperations<EntityDiscussionThread> update = getUpdateOperations()
                .set(MATCHED_COMMENT_PATH, comment);
        datastore.updateFirst(query, update);
    }

    public Optional<EntityDiscussionThread> findThreadByCommentId(CommentId commentId) {
        Query<EntityDiscussionThread> query = datastore.createQuery(EntityDiscussionThread.class)
                                                       .field(COMMENTS_ID).equal(commentId);
        return Optional.ofNullable(query.get());
    }

    public boolean deleteComment(CommentId commentId) {
        Query<EntityDiscussionThread> query = datastore.createQuery(EntityDiscussionThread.class)
                                                       .field(COMMENTS_ID).equal(commentId);
        UpdateOperations<EntityDiscussionThread> update = getUpdateOperations()
                .removeAll(COMMENTS, new BasicDBObject("_id", commentId.getId()));
        UpdateResults updateResults = datastore.updateFirst(query, update);
        return updateResults.getUpdatedCount() == 1;
    }

    public List<EntityDiscussionThread> getThreadsInProject(ProjectId projectId) {
        return datastore.createQuery(EntityDiscussionThread.class)
                        .field(PROJECT_ID).equal(projectId)
                        .asList();
    }
}
