package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class FormTriggerRepositoryImpl implements FormTriggerRepository {

    public static final String COLLECTION_NAME = "FormTriggers";

    @Nonnull
    private final MongoDatabase database;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public FormTriggerRepositoryImpl(@Nonnull MongoDatabase database,
                                     @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void save(FormSelector formSelector) {
        var triggerDocument = objectMapper.convertValue(formSelector, Document.class);
        var collection = database.getCollection(COLLECTION_NAME);
        collection.insertOne(triggerDocument);
    }

    @Override
    public Stream<FormSelector> findFormTriggers(@Nonnull ProjectId projectId) {
        var collection = database.getCollection(COLLECTION_NAME);
        var filter = new Document("projectId", projectId.getId());
        var spliterator = collection.find(filter).spliterator();
        return StreamSupport.stream(spliterator, false)
                            .map(doc -> objectMapper.convertValue(doc, FormSelector.class));
    }

    @Override
    public void ensureIndexes() {

    }
}
