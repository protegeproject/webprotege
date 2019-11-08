package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class EntityFormSelectorRepositoryImpl implements EntityFormSelectorRepository {

    public static final String COLLECTION_NAME = "FormTriggers";

    @Nonnull
    private final MongoDatabase database;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public EntityFormSelectorRepositoryImpl(@Nonnull MongoDatabase database,
                                            @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void ensureIndexes() {

    }

    @Override
    public Stream<EntityFormSelector> findFormTriggers(@Nonnull ProjectId projectId) {
        var collection = database.getCollection(COLLECTION_NAME);
        var filter = new Document("projectId", projectId.getId());
        List<EntityFormSelector> resultList = new ArrayList<>();
        collection.find(filter).forEach((Consumer<Document>) doc -> resultList.add(toEntityFormSelector(doc)));
        return resultList.stream();
    }

    private EntityFormSelector toEntityFormSelector(Document doc) {
        return objectMapper.convertValue(doc, EntityFormSelector.class);
    }

    @Override
    public void save(EntityFormSelector entityFormSelector) {
        var triggerDocument = objectMapper.convertValue(entityFormSelector, Document.class);
        var collection = database.getCollection(COLLECTION_NAME);
        collection.insertOne(triggerDocument);
    }
}
