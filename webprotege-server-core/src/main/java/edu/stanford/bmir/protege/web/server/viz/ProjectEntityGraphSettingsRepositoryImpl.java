package edu.stanford.bmir.protege.web.server.viz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@ApplicationSingleton
public class ProjectEntityGraphSettingsRepositoryImpl implements ProjectEntityGraphSettingsRepository {

    private static final String COLLECTION_NAME = "EntityGraphSettings";

    private final MongoDatabase database;

    private final ObjectMapper objectMapper;

    @Inject
    public ProjectEntityGraphSettingsRepositoryImpl(MongoDatabase database,
                                                    ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void saveSettings(@Nonnull ProjectEntityGraphSettings settings) {
        var document = objectMapper.convertValue(settings, Document.class);
        var filter = getFilter(settings.getProjectId());
        var replaceOptions = new ReplaceOptions().upsert(true);
        getCollection().replaceOne(filter, document, replaceOptions);
    }

    @Nonnull
    @Override
    public ProjectEntityGraphSettings getSettings(@Nonnull ProjectId projectId) {
        var found = getCollection().find(getFilter(projectId));
        var firstDocument = found.first();
        if(firstDocument == null) {
            return ProjectEntityGraphSettings.get(projectId, ImmutableList.of());
        }
        return objectMapper.convertValue(firstDocument, ProjectEntityGraphSettings.class);
    }

    public static Document getFilter(@Nonnull ProjectId projectId) {
        return new Document(ProjectEntityGraphSettings.PROJECT_ID, projectId.getId());
    }

    @Override
    public void ensureIndexes() {

    }

    public static String getCollectionName() {
        return COLLECTION_NAME;
    }

    public MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }
}
