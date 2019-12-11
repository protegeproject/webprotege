package edu.stanford.bmir.protege.web.server.viz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.NegatedEdgeCriteria;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings.PROJECT_ID;
import static edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings.USER_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@ApplicationSingleton
public class EntityGraphSettingsRepositoryImpl implements EntityGraphSettingsRepository {

    private static final String COLLECTION_NAME = "EntityGraphSettings";

    private final MongoDatabase database;

    private final ObjectMapper objectMapper;

    @Inject
    public EntityGraphSettingsRepositoryImpl(MongoDatabase database,
                                             ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void saveSettings(@Nonnull EntityGraphSettings settings) {
        var document = objectMapper.convertValue(settings, Document.class);
        var filter = getFilter(settings.getProjectId(), settings.getUserId().orElse(null));
        var replaceOptions = new ReplaceOptions().upsert(true);
        getCollection().replaceOne(filter, document, replaceOptions);
    }

    @Nonnull
    @Override
    public EntityGraphSettings getProjectDefaultSettings(@Nonnull ProjectId projectId) {
        var filter = getFilter(projectId, null);
        return getProjectEntityGraphSettings(projectId, filter);
    }

    private EntityGraphSettings getProjectEntityGraphSettings(@Nonnull ProjectId projectId,
                                                              Document filter) {
        var found = getCollection().find(filter);
        var firstDocument = found.first();
        if(firstDocument == null) {
            return EntityGraphSettings.get(projectId, null, CompositeEdgeCriteria.get(
                    ImmutableList.of(
                            CompositeEdgeCriteria.get(ImmutableList.of(AnyEdgeCriteria.get()), MultiMatchType.ANY),
                            NegatedEdgeCriteria.get(CompositeEdgeCriteria.get(ImmutableList.of(), MultiMatchType.ANY))),
                    MultiMatchType.ALL
            ), 1.0);
        }
        return objectMapper.convertValue(firstDocument, EntityGraphSettings.class);
    }

    @Nonnull
    @Override
    public EntityGraphSettings getSettingsForUserOrProjectDefault(@Nonnull ProjectId projectId,
                                                                  @Nullable UserId userId) {
        var filter = getFilter(projectId, userId);
        return getProjectEntityGraphSettings(projectId, filter);
    }

    public static Document getFilter(@Nonnull ProjectId projectId,
                                     @Nullable UserId userId) {
        var doc = new Document(PROJECT_ID, projectId.getId());
        if(userId == null) {
            doc.append(USER_ID, null);
        }
        else {
            doc.append(USER_ID, userId.getUserName());
        }
        return doc;
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
