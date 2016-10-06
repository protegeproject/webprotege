package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.server.persistence.OWLEntityConverter;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class EntityDiscussionThreadConverter implements DocumentConverter<EntityDiscussionThread> {

    private static final String PROJECT_ID = "projectId";

    private static final String THREAD_ID = "threadId";

    private static final String ENTITY = "entity";

    private static final String COMMENTS = "comments";

    private static final String STATUS = "status";

    @Nonnull
    private final OWLEntityConverter entityConverter;

    @Nonnull
    private final CommentConverter commentConverter;


    @Inject
    public EntityDiscussionThreadConverter(@Nonnull OWLEntityConverter entityConverter,
                                           @Nonnull CommentConverter commentConverter) {
        this.entityConverter = checkNotNull(entityConverter);
        this.commentConverter = checkNotNull(commentConverter);
    }

    @Override
    public Document toDocument(@Nonnull EntityDiscussionThread thread) {
        Document document = new Document();
        document.append(THREAD_ID,
                        thread.getId()
                              .getId());
        document.append(PROJECT_ID,
                        thread.getProjectId()
                              .getId());
        document.append(ENTITY, entityConverter.toDocument(thread.getTargetEntity()));
        List<Document> commentDocuments = thread.getComments()
                                                .stream()
                                                .map(c -> commentConverter.toDocument(c))
                                                .collect(toList());
        document.append(COMMENTS, commentDocuments);
        document.append(STATUS,
                        thread.getStatus()
                              .name());
        return document;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityDiscussionThread fromDocument(@Nonnull Document document) {
        ProjectId projectId = ProjectId.get(document.getString(PROJECT_ID));
        ThreadId threadId = new ThreadId(document.getString(THREAD_ID));
        OWLEntity entity = entityConverter.fromDocument((Document) document.get(ENTITY));
        List<Comment> comments = ((List<Document>) document.get(COMMENTS)).stream()
                                                                          .map(d -> commentConverter.fromDocument(d))
                                                                          .collect(toList());
        Status status = Status.valueOf(document.getString(STATUS));
        return new EntityDiscussionThread(threadId,
                                          projectId,
                                          entity,
                                          ImmutableList.copyOf(comments),
                                          status);
    }

    public Document byProjectIdAndEntity(ProjectId projectId, OWLEntity entity) {
        Document document = new Document();
        document.append(PROJECT_ID, projectId.getId());
        document.append(ENTITY, entityConverter.toDocument(entity));
        return document;
    }

    public void ensureIndexes(MongoCollection<Document> collection) {
        Document index = new Document();
        index.append(PROJECT_ID, 1)
             .append(ENTITY, 1);

        collection.createIndex(index);
    }

    public Bson byThreadId(ThreadId threadId) {
        return new Document(THREAD_ID, threadId);
    }

    public Bson appendComment(Comment comment) {
        return Updates.addToSet(COMMENTS, comment);
    }
}
