package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormRepositoryImpl implements EntityFormRepository {


    private static final String COLLECTION_NAME = "Forms";

    private static final String PROJECT_ID = "projectId";

    private static final String FORM_DESCRIPTOR = "formDescriptor";

    private static final String FORM_ID = "formId";

    public static final String FORM_DESCRIPTOR__FORM_ID = FORM_DESCRIPTOR + "." + FORM_ID;

    private final ObjectMapper objectMapper;

    private final MongoDatabase database;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public EntityFormRepositoryImpl(ObjectMapper objectMapper, MongoDatabase database) {
        this.objectMapper = checkNotNull(objectMapper);
        this.database = checkNotNull(database);
    }

    @Override
    public void deleteFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormId formId) {
        try {
            writeLock.lock();
            var query = new Document(PROJECT_ID, projectId.getId()).append(FORM_DESCRIPTOR__FORM_ID, formId.getId());
            getCollection().findOneAndDelete(query);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void saveFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormDescriptor formDescriptor) {
        try {
            writeLock.lock();
            try (var cursor = getCollection().find(getProjectIdFormIdFilter(projectId, formDescriptor.getFormId()))
                                             .limit(1)
                                             .iterator()) {
                Integer ordinal = null;
                if (cursor.hasNext()) {
                    var document = cursor.next();
                    var currentOrdinal = document.getInteger(FormDescriptorRecord.ORDINAL);
                    if (currentOrdinal != null) {
                        ordinal = currentOrdinal;
                    }
                }
                if (ordinal == null){
                    ordinal = (int) getCollection().countDocuments(getProjectIdFormIdFilter(projectId,
                                                                                            formDescriptor.getFormId()));
                }
                var record = FormDescriptorRecord.get(projectId, formDescriptor, ordinal);
                var document = objectMapper.convertValue(record, Document.class);
                var filter = getProjectIdFormIdFilter(projectId, formDescriptor.getFormId());
                getCollection()
                        .findOneAndReplace(filter, document, new FindOneAndReplaceOptions().upsert(true));
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Stream<FormDescriptor> findFormDescriptors(@Nonnull ProjectId projectId) {
        try {
            readLock.lock();
            return StreamSupport.stream(getCollection()
                                                .find(new Document(PROJECT_ID, projectId.getId())).spliterator(),
                                        false
                                        )
                    .map(doc -> objectMapper.convertValue(doc, FormDescriptorRecord.class))
                                .collect(toList())
                                .stream()
                    .sorted()
                    .map(FormDescriptorRecord::getFormDescriptor);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<FormDescriptor> findFormDescriptors(@Nonnull ImmutableSet<FormId> formIds,
                                                      @Nonnull ProjectId projectId) {
        return findFormDescriptors(projectId)
                .filter(formDescriptor -> formIds.contains(formDescriptor.getFormId()));
    }

    @Override
    public void setProjectFormDescriptors(@Nonnull ProjectId projectId, @Nonnull List<FormDescriptor> formDescriptors) {
        try {
            writeLock.lock();
            var collection = getCollection();
            collection.deleteMany(new Document(PROJECT_ID, projectId.getId()));
            if (!formDescriptors.isEmpty()) {
                var docs = new ArrayList<Document>();
                for (int ordinal = 0; ordinal < formDescriptors.size(); ordinal++) {
                    var formDescriptor = formDescriptors.get(ordinal);
                    var record = FormDescriptorRecord.get(projectId, formDescriptor, ordinal);
                    var recordDocument = objectMapper.convertValue(record, Document.class);
                    docs.add(recordDocument);
                }
                collection.insertMany(docs);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }

    @Override
    public Optional<FormDescriptor> findFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormId formId) {
        try {
            readLock.lock();
            Bson filter = getProjectIdFormIdFilter(projectId, formId);
            var foundFormDocument = getCollection()
                    .find(filter)
                    .first();
            if(foundFormDocument == null) {
                return Optional.empty();
            }
            else {
                var formRecord = objectMapper.convertValue(foundFormDocument, FormDescriptorRecord.class);
                return Optional.of(formRecord.getFormDescriptor());
            }
        } finally {
            readLock.unlock();
        }
    }

    public static Bson getProjectIdFormIdFilter(@Nonnull ProjectId projectId,
                                         @Nonnull FormId formId) {
        var projectIdFilter = Filters.eq(PROJECT_ID, projectId.getId());
        var formIdFilter = Filters.eq(FORM_DESCRIPTOR__FORM_ID, formId.getId());
        return Filters.and(projectIdFilter, formIdFilter);
    }

    @Override
    public void ensureIndexes() {
        try {
            writeLock.lock();
            var collection = getCollection();
            var projectIdAsc = Indexes.ascending(PROJECT_ID);
            var formDescriptor_formId_Asc = Indexes.ascending(FORM_DESCRIPTOR__FORM_ID);
            var compoundIndex = Indexes.compoundIndex(projectIdAsc, formDescriptor_formId_Asc);
            var indexOptions = new IndexOptions().unique(true);
            collection.createIndex(compoundIndex, indexOptions);
        } finally {
            writeLock.unlock();
        }
    }
}
