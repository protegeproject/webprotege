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
import java.util.List;

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
                .append(PERSPECTIVE_ID, 1);
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

        String perspectiveId = record.getPerspectiveId().getId();
        document.append(PERSPECTIVE_ID, perspectiveId);
        return document;
    }

    @Override
    public void saveDescriptors(@Nonnull List<PerspectiveDescriptorsRecord> perspectiveDescriptors) {
        var collection = getCollection();
        var bulkWriteModels = new ArrayList<WriteModel<Document>>();
        for(var record : perspectiveDescriptors) {
            var query = getQuery(record);
            var replacementDocument = objectMapper.convertValue(record, Document.class);
            var replaceOneModel = new ReplaceOneModel<>(query, replacementDocument, new ReplaceOptions().upsert(true));
            bulkWriteModels.add(replaceOneModel);
        }
        collection.bulkWrite(bulkWriteModels);
    }

    @Nonnull
    @Override
    public ImmutableList<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId,
                                                                       @Nonnull UserId userId) {
        var query = new Document(PROJECT_ID, projectId.getId())
                .append(USER_ID, userId.getUserName());
        return getPerspectiveDescriptorRecords(query);
    }

    @Nonnull
    @Override
    public ImmutableList<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId) {
        var query = new Document(PROJECT_ID, projectId.getId())
                .append(USER_ID, null);
        return getPerspectiveDescriptorRecords(query);
    }

    @Nonnull
    @Override
    public ImmutableList<PerspectiveDescriptorsRecord> findDescriptors() {
        var query = new Document(PROJECT_ID, null)
                .append(USER_ID, null);
        return getPerspectiveDescriptorRecords(query);
    }

    private ImmutableList<PerspectiveDescriptorsRecord> getPerspectiveDescriptorRecords(Document query) {
        var resultBuilder = ImmutableList.<PerspectiveDescriptorsRecord>builder();
        try(var cursor = getCollection()
                .find(query).iterator()) {
            while (cursor.hasNext()) {
                var document = cursor.next();
                var rec = objectMapper.convertValue(document, PerspectiveDescriptorsRecord.class);
                resultBuilder.add(rec);
            }
        }
        return resultBuilder.build();
    }
}
