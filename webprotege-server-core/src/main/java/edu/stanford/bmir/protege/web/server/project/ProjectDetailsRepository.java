package edu.stanford.bmir.protege.web.server.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import edu.stanford.bmir.protege.web.server.api.TimestampSerializer;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.project.ProjectDetails.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@ApplicationSingleton
public class ProjectDetailsRepository implements Repository {

    public static final String COLLECTION_NAME = "ProjectDetails";

    @Nonnull
    private final ObjectMapper objectMapper;

    private final MongoCollection<Document> collection;

    private final LoadingCache<ProjectId, ProjectDetails> cache;

    private final LoadingCache<ProjectId, ImmutableList<DictionaryLanguage>> displayLanguagesCache;

    @Inject
    public ProjectDetailsRepository(@Nonnull MongoDatabase database,
                                    @Nonnull ObjectMapper objectMapper) {
        this.collection = database.getCollection(COLLECTION_NAME);
        this.objectMapper = checkNotNull(objectMapper);
        this.cache = Caffeine.newBuilder()
                             .expireAfterAccess(Duration.ofMinutes(2))
                             .build(projectId -> findOneFromDb(projectId).orElse(null));
        this.displayLanguagesCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(2))
                .build(projectId -> findOne(projectId).map(settings -> settings.getDefaultDisplayNameSettings().getPrimaryDisplayNameLanguages().stream()
                                                                               .map(DictionaryLanguageData::getDictionaryLanguage)
                                                                               .collect(toImmutableList()))
                                                      .orElse(ImmutableList.of()));
    }

    private static Document withProjectId(@Nonnull ProjectId projectId) {
        return new Document(PROJECT_ID, projectId.getId());
    }

    private static Document withOwner(@Nonnull UserId owner) {
        return new Document(OWNER, owner.getUserName());
    }

    private static Document withProjectIdAndWithOwner(@Nonnull ProjectId projectId,
                                                      @Nonnull UserId owner) {
        return new Document(PROJECT_ID, projectId.getId())
                .append(OWNER, owner.getUserName());
    }

    public static Bson updateInTrash(boolean inTrash) {
        return Updates.set(IN_TRASH, inTrash);
    }

    public static Bson updateModified(UserId userId, long timestamp) {
        return Updates.combine(
                Updates.set(MODIFIED_AT, TimestampSerializer.toIsoDateTime(timestamp)),
                Updates.set(MODIFIED_BY, userId.getUserName())
        );
    }

    @Override
    public void ensureIndexes() {
        collection.createIndex(new Document(PROJECT_ID, 1).append(DISPLAY_NAME, 1));
    }

    public boolean containsProject(@Nonnull ProjectId projectId) {
        return collection
                .find(withProjectId(projectId))
                .projection(new Document())
                .limit(1)
                .first() != null;
    }

    public boolean containsProjectWithOwner(@Nonnull ProjectId projectId, @Nonnull UserId owner) {
        return collection
                .find(withProjectIdAndWithOwner(projectId, owner))
                .projection(new Document())
                .limit(1)
                .first() != null;
    }

    public void setInTrash(ProjectId projectId, boolean inTrash) {
        collection.updateOne(withProjectId(projectId), updateInTrash(inTrash));
        cache.invalidate(projectId);
        // No need to invalidate display languages cache
    }

    public void setModified(ProjectId projectId, long modifiedAt, UserId modifiedBy) {
        collection.updateOne(withProjectId(projectId), updateModified(modifiedBy, modifiedAt));
        cache.invalidate(projectId);
        // No need to invalidate display languages cache
    }

    public Optional<ProjectDetails> findOne(@Nonnull ProjectId projectId) {
        return Optional.ofNullable(cache.get(projectId));
    }

    private Optional<ProjectDetails> findOneFromDb(@Nonnull ProjectId projectId) {
        return Optional.ofNullable(
                collection
                        .find(withProjectId(projectId))
                        .limit(1)
                        .first())
                       .map(d -> objectMapper.convertValue(d, ProjectDetails.class));
    }

    public List<ProjectDetails> findByOwner(UserId owner) {
        ArrayList<ProjectDetails> result = new ArrayList<>();
        collection.find(withOwner(owner))
                  .map(d -> objectMapper.convertValue(d, ProjectDetails.class))
                  .into(result);
        return result;
    }

    public void save(@Nonnull ProjectDetails projectRecord) {
        Document document = objectMapper.convertValue(projectRecord, Document.class);
        collection.replaceOne(withProjectId(projectRecord.getProjectId()),
                              document,
                              new UpdateOptions().upsert(true));
        cache.invalidate(projectRecord.getProjectId());
        displayLanguagesCache.invalidate(projectRecord.getProjectId());
    }

    public void delete(@Nonnull ProjectId projectId) {
        collection.deleteOne(withProjectId(projectId));
        cache.invalidate(projectId);
        displayLanguagesCache.invalidate(projectId);
    }

    public ImmutableList<DictionaryLanguage> getDisplayNameLanguages(@Nonnull ProjectId projectId) {
        return displayLanguagesCache.get(projectId);
    }
}
