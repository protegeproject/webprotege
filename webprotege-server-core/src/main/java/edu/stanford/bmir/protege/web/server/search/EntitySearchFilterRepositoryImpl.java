package edu.stanford.bmir.protege.web.server.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public class EntitySearchFilterRepositoryImpl implements EntitySearchFilterRepository {

    private static final String COLLECTION_NAME = "EntitySearchFilters";

    private static final String ID = "_id";

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Nonnull
    private final MongoDatabase db;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public EntitySearchFilterRepositoryImpl(@Nonnull MongoDatabase mongoDatabase, @Nonnull ObjectMapper objectMapper) {
        this.db = checkNotNull(mongoDatabase);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void ensureIndexes() {
        getCollection().createIndex(new Document(EntitySearchFilter.PROJECT_ID, 1));
    }

    @Nonnull
    private MongoCollection<Document> getCollection() {
        return db.getCollection(COLLECTION_NAME);
    }

    @Nonnull
    private Document toMongoDocument(@Nonnull EntitySearchFilter filter) {
        return objectMapper.convertValue(filter, Document.class);
    }

    @Nonnull
    private EntitySearchFilter toEntitySearchFilter(@Nonnull Document mongoDocument) {
        return objectMapper.convertValue(mongoDocument, EntitySearchFilter.class);
    }

    @Nonnull
    @Override
    public ImmutableList<EntitySearchFilter> getSearchFilters(@Nonnull ProjectId projectId) {
        try {
            readLock.lock();
            var filtersByProjectId = new Document(EntitySearchFilter.PROJECT_ID, projectId.getId());
            try (var iterator = getCollection().find(filtersByProjectId).iterator()) {
                var resultBuilder = ImmutableList.<EntitySearchFilter>builder();
                while (iterator.hasNext()) {
                    var matchedDocument = iterator.next();
                    var searchFilter = toEntitySearchFilter(matchedDocument);
                    resultBuilder.add(searchFilter);
                }
                return resultBuilder.build();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void saveSearchFilters(@Nonnull ImmutableList<EntitySearchFilter> filters) {
        try {
            writeLock.lock();
            var writes = filters.stream()
                                .map(this::toMongoDocument)
                                .map(this::toReplaceOne)
                                .collect(toImmutableList());
            getCollection().bulkWrite(writes);
        } finally {
            writeLock.unlock();
        }
    }

    private ReplaceOneModel<Document> toReplaceOne(Document doc) {
        return new ReplaceOneModel<>(new Document(ID, doc.get(ID)),
                                     doc,
                                     new ReplaceOptions().upsert(true));
    }
}
