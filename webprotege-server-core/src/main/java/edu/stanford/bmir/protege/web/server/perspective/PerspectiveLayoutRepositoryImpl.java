package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayoutRecord.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveLayoutRepositoryImpl implements PerspectiveLayoutRepository {

    public static final String PERSPECTIVE_LAYOUTS = "PerspectiveLayouts";

    @Nonnull
    private final MongoDatabase database;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public PerspectiveLayoutRepositoryImpl(@Nonnull MongoDatabase database, @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Nonnull
    private MongoCollection<Document> getCollection() {
        return database.getCollection(PERSPECTIVE_LAYOUTS);
    }

    private Document getQuery(@Nullable String projectId,
                              @Nullable String userId,
                              @Nonnull PerspectiveId perspectiveId) {
        return new Document(PROJECT_ID, projectId)
                .append(USER_ID, userId)
                .append(PERSPECTIVE_ID, perspectiveId.getId());
    }

    @Nonnull
    @Override
    public Optional<PerspectiveLayoutRecord> findLayout(@Nonnull ProjectId projectId,
                                                        @Nonnull UserId userId,
                                                        @Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(projectId.getId(), userId.getUserName(), perspectiveId);
        return findLayout(query);
    }

    private Optional<PerspectiveLayoutRecord> findLayout(Document query) {
        var document = getCollection().find(query)
                .first();
        if(document == null) {
            return Optional.empty();
        }
        else {
            var record = objectMapper.convertValue(document, PerspectiveLayoutRecord.class);
            return Optional.of(record);
        }
    }

    @Nonnull
    @Override
    public Optional<PerspectiveLayoutRecord> findLayout(@Nonnull ProjectId projectId,
                                                        @Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(projectId.getId(), null, perspectiveId);
        return findLayout(query);
    }

    @Nonnull
    @Override
    public Optional<PerspectiveLayoutRecord> findLayout(@Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(null, null, perspectiveId);
        return findLayout(query);
    }

    @Override
    public void saveLayout(PerspectiveLayoutRecord record) {
        saveLayouts(ImmutableList.of(record));
    }

    private Document getQuery(PerspectiveLayoutRecord record) {
        return getQuery(Optional.ofNullable(record.getProjectId()).map(ProjectId::getId).orElse(null),
                        Optional.ofNullable(record.getUserId()).map(UserId::getUserName).orElse(null),
                        record.getPerspectiveId());
    }

    @Override
    public void saveLayouts(@Nonnull List<PerspectiveLayoutRecord> records) {
        var writes = new ArrayList<WriteModel<Document>>(records.size());
        for(var record : records) {
            var query = getQuery(record);
            var nextDocument = objectMapper.convertValue(record, Document.class);
            var write = new ReplaceOneModel<>(query, nextDocument, new ReplaceOptions().upsert(true));
            writes.add(write);
        }
        getCollection().bulkWrite(writes);
    }

    @Override
    public void dropLayout(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(projectId.getId(), userId.getUserName(), perspectiveId);
        getCollection().deleteOne(query);
    }

    @Override
    public void dropAllLayouts(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        var query = new Document(PROJECT_ID, projectId.getId())
                .append(USER_ID, userId.getUserName());
        getCollection().deleteMany(query);
    }

    @Override
    public void ensureIndexes() {
        var indexKeys = new Document(PROJECT_ID, 1)
                .append(USER_ID, 1)
                .append(PERSPECTIVE_ID, 1);
        var indexOptions = new IndexOptions().unique(true);
        getCollection()
                .createIndex(indexKeys, indexOptions);
    }
}
