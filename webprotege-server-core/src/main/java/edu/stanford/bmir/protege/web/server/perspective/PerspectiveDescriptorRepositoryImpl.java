package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.perspective.PerspectiveDescriptorsRecord.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptorRepositoryImpl implements PerspectiveDescriptorRepository {

    public static final String PERSPECTIVE_DESCRIPTORS = "PerspectiveDescriptors";

    @Nonnull
    private final MongoDatabase database;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public PerspectiveDescriptorRepositoryImpl(@Nonnull MongoDatabase database, @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Nonnull
    private MongoCollection<Document> getCollection() {
        return database.getCollection(PERSPECTIVE_DESCRIPTORS);
    }

    @Override
    public void ensureIndexes() {
        var indexKeys = new Document(PROJECT_ID, 1)
                .append(USER_ID, 1)
                .append(PERSPECTIVES, 1);
        var indexOptions = new IndexOptions().unique(true);
        getCollection().createIndex(indexKeys, indexOptions);
    }

    private Document getQuery(@Nonnull PerspectiveDescriptorsRecord record) {
        var document = new Document();
        String projectId;
        if(record.getProjectId() == null) {
            projectId = null;
        }
        else {
            projectId = record.getProjectId().getId();
        }
        document.append(PROJECT_ID, projectId);

        String userId;
        if(record.getUserId() == null) {
            userId = null;
        }
        else {
            userId = record.getUserId().getUserName();
        }
        document.append(USER_ID, userId);
        return document;
    }

    @Override
    public void saveDescriptors(@Nonnull PerspectiveDescriptorsRecord perspectiveDescriptors) {
        var collection = getCollection();
        var query = getQuery(perspectiveDescriptors);
        var replacementDocument = objectMapper.convertValue(perspectiveDescriptors, Document.class);
        collection.replaceOne(query, replacementDocument, new ReplaceOptions().upsert(true));
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId,
                                                                  @Nonnull UserId userId) {
        var query = new Document(PROJECT_ID, projectId.getId())
                .append(USER_ID, userId.getUserName());
        return getPerspectiveDescriptorRecord(query);
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId) {
        var query = new Document(PROJECT_ID, projectId.getId())
                .append(USER_ID, null);
        return getPerspectiveDescriptorRecord(query);
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDescriptorsRecord> findDescriptors() {
        var query = new Document(PROJECT_ID, null)
                .append(USER_ID, null);
        return getPerspectiveDescriptorRecord(query);
    }

    @Nonnull
    @Override
    public Stream<PerspectiveDescriptorsRecord> findProjectAndSystemDescriptors(@Nonnull ProjectId projectId) {
        var projectIdQueries = Arrays.asList(
                new Document(PROJECT_ID, null),
                new Document(PROJECT_ID, projectId.getId()));
        var query = new Document(new Document("$or", projectIdQueries))
                .append(USER_ID, null);
        var resultBuilder = Stream.<PerspectiveDescriptorsRecord>builder();
        try(var cursor = getCollection().find(query)
                       .cursor()) {
            while(cursor.hasNext()) {
                var doc = cursor.next();
                var record = objectMapper.convertValue(doc, PerspectiveDescriptorsRecord.class);
                resultBuilder.add(record);
            }
        }
        return resultBuilder.build();
    }

    @Override
    public void dropAllDescriptors(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        var query = new Document(PROJECT_ID, projectId.getId())
                .append(USER_ID, userId.getUserName());
        getCollection().deleteMany(query);
    }

    private Optional<PerspectiveDescriptorsRecord> getPerspectiveDescriptorRecord(Document query) {
        var document = getCollection()
                .find(query)
                .first();
        return Optional.ofNullable(document)
                .map(doc -> objectMapper.convertValue(doc, PerspectiveDescriptorsRecord.class));
    }
}
