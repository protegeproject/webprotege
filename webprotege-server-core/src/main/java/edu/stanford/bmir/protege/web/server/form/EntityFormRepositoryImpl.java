package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@ApplicationSingleton
public class EntityFormRepositoryImpl implements EntityFormRepository {

    private static final Logger logger = LoggerFactory.getLogger(EntityFormRepositoryImpl.class);


    private static final String COLLECTION_NAME = "Forms";

    private static final String PROJECT_ID = "projectId";

    private static final String FORM_DESCRIPTOR = "formDescriptor";

    private static final String FORM_ID = "formId";

    public static final String FORM_DESCRIPTOR__FORM_ID = FORM_DESCRIPTOR + "." + FORM_ID;

    private final ObjectMapper objectMapper;

    private final MongoDatabase database;

    @Inject
    public EntityFormRepositoryImpl(ObjectMapper objectMapper, MongoDatabase database) {
        this.objectMapper = checkNotNull(objectMapper);
        this.database = checkNotNull(database);
    }

    @Override
    public void deleteFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormId formId) {
        var query = new Document(PROJECT_ID, projectId.getId()).append(FORM_DESCRIPTOR__FORM_ID, formId.getId());
        getCollection().findOneAndDelete(query);
    }

    @Override
    public void saveFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormDescriptor formDescriptor) {
        var record = FormDescriptorRecord.get(projectId, formDescriptor);
        var document = objectMapper.convertValue(record, Document.class);
        var filter = getProjectIdFormIdFilter(projectId, formDescriptor.getFormId());
        getCollection()
                .findOneAndReplace(filter, document, new FindOneAndReplaceOptions().upsert(true));
    }

    @Override
    public Stream<FormDescriptor> findFormDescriptors(@Nonnull ProjectId projectId) {
        return StreamSupport.stream(getCollection()
                                            .find(new Document(PROJECT_ID, projectId.getId())).spliterator(),
                                    false
                                    )
                .map(doc -> objectMapper.convertValue(doc, FormDescriptorRecord.class))
                            .map(FormDescriptorRecord::getFormDescriptor)
                .collect(toList())
                            .stream();
    }

    @Override
    public void setProjectFormDescriptors(@Nonnull ProjectId projectId, @Nonnull List<FormDescriptor> formDescriptors) {
        var collection = getCollection();
        collection.deleteMany(new Document(PROJECT_ID, projectId.getId()));
        var docs = formDescriptors.stream()
                                  .map(formDescriptor -> FormDescriptorRecord.get(projectId, formDescriptor))
                       .map(record -> objectMapper.convertValue(record, Document.class))
                       .collect(toList());
        collection.insertMany(docs);
    }

    public MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }

    @Override
    public Optional<FormDescriptor> findFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormId formId) {
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
    }

    public static Bson getProjectIdFormIdFilter(@Nonnull ProjectId projectId,
                                         @Nonnull FormId formId) {
        var projectIdFilter = Filters.eq(PROJECT_ID, projectId.getId());
        var formIdFilter = Filters.eq(FORM_DESCRIPTOR__FORM_ID, formId.getId());
        return Filters.and(projectIdFilter, formIdFilter);
    }

    @Override
    public void ensureIndexes() {
        var collection = getCollection();
        var projectIdAsc = Indexes.ascending(PROJECT_ID);
        var formDescriptor_formId_Asc = Indexes.ascending(FORM_DESCRIPTOR__FORM_ID);
        var compoundIndex = Indexes.compoundIndex(projectIdAsc, formDescriptor_formId_Asc);
        var indexOptions = new IndexOptions().unique(true);
        collection.createIndex(compoundIndex, indexOptions);
        normalizeRepo();
    }

    private void normalizeRepo() {
        logger.info("Normalizing forms");
        var collection = getCollection();
        var docs = collection.find();
        List<FormDescriptorRecord> records = new ArrayList<>();
        for (Document formRecord : docs) {
            var record = objectMapper.convertValue(formRecord, FormDescriptorRecord.class);
            records.add(record);
        }
        records.forEach(r -> {
            saveFormDescriptor(r.getProjectId(), r.getFormDescriptor());
        });
        logger.info("Normalized {} forms", records.size());
    }
}
