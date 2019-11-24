package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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
public class EntityFormRepositoryImpl implements EntityFormRepository {


    private static final String COLLECTION_NAME = "Forms";

    private final ObjectMapper objectMapper;

    private final MongoDatabase database;

    @Inject
    public EntityFormRepositoryImpl(ObjectMapper objectMapper, MongoDatabase database) {
        this.objectMapper = checkNotNull(objectMapper);
        this.database = checkNotNull(database);
    }

    @Override
    public void saveFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormDescriptor formDescriptor) {
        var record = FormDescriptorRecord.get(projectId, formDescriptor);
        var document = objectMapper.convertValue(record, Document.class);
        database.getCollection(COLLECTION_NAME).insertOne(document);
    }

    @Override
    public Stream<FormDescriptor> findFormDescriptors(@Nonnull ProjectId projectId) {
        return StreamSupport.stream(database.getCollection(COLLECTION_NAME)
                                            .find(new Document("projectId", projectId.getId())).spliterator(),
                                        false
                                    )
                .map(doc -> objectMapper.convertValue(doc, FormDescriptorRecord.class))
                            .map(FormDescriptorRecord::getFormDescriptor)
                .collect(toList())
                            .stream();
    }

    @Override
    public void setProjectFormDescriptors(@Nonnull ProjectId projectId, @Nonnull List<FormDescriptor> formDescriptors) {
        var collection = database.getCollection(COLLECTION_NAME);
        collection.deleteMany(new Document("projectId", projectId.getId()));
        var docs = formDescriptors.stream()
                                  .map(formDescriptor -> FormDescriptorRecord.get(projectId, formDescriptor))
                       .map(record -> objectMapper.convertValue(record, Document.class))
                       .collect(toList());
        collection.insertMany(docs);
    }

    @Override
    public Optional<FormDescriptor> findFormDescriptor(@Nonnull ProjectId projectId, @Nonnull FormId formId) {
        var filter = new Document();
        filter.put("projectId", projectId.getId());
//        filter.put("formDescriptor", new Document("formId", formId.getId()));
        var foundFormDocument = database.getCollection(COLLECTION_NAME)
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

    @Override
    public void ensureIndexes() {

    }
}
