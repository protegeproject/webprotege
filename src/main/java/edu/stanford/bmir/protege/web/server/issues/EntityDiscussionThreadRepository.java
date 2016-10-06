package edu.stanford.bmir.protege.web.server.issues;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class EntityDiscussionThreadRepository implements Repository {

    private static final String COLLECTION_NAME = "EntityComments";

    @Nonnull
    private final MongoCollection<Document> collection;

    @Nonnull
    private final EntityDiscussionThreadConverter converter;


    @Inject
    public EntityDiscussionThreadRepository(@Nonnull MongoDatabase database,
                                            @Nonnull EntityDiscussionThreadConverter converter) {
        this.collection = database.getCollection(COLLECTION_NAME);
        this.converter = converter;
    }

    public List<EntityDiscussionThread> findThreads(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        List<Document> matches = new ArrayList<>();
        collection.find(byProjectIdAndEntity(projectId, entity)).into(matches);
        return matches.stream()
                      .map(d -> converter.fromDocument(d))
                      .collect(Collectors.toList());
    }

    public void saveThread(EntityDiscussionThread thread) {
        collection.insertOne(converter.toDocument(thread));
    }

    public void addCommentToThread(ThreadId threadId, Comment comment) {
        collection.updateOne(converter.byThreadId(threadId), converter.appendComment(comment));
    }

    private Document byProjectIdAndEntity(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        return converter.byProjectIdAndEntity(projectId, entity);
    }

    @Override
    public void ensureIndexes() {
        converter.ensureIndexes(collection);
    }
}
