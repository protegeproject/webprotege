package edu.stanford.bmir.protege.web.server.permissions;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecordConverter.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public class ProjectPermissionRecordRepository {

    private static final String COLLECTION_NAME = "ProjectPermissionRecords";

    @Nonnull
    private final MongoCollection<Document> collection;

    @Nonnull
    private final ProjectPermissionRecordConverter converter;

    public ProjectPermissionRecordRepository(@Nonnull MongoDatabase database,
                                             @Nonnull ProjectPermissionRecordConverter converter) {
        this.collection = checkNotNull(database).getCollection(COLLECTION_NAME);
        this.converter = checkNotNull(converter);
    }

    public void ensureIndexes() {
        converter.ensureIndexes(collection);
    }

    public boolean hasPermission(@Nonnull ProjectId projectId,
                                 @Nonnull UserId userId,
                                 @Nonnull Permission permission) {
        return collection.find(byProjectIdAndUserIdIfExistsAndPermission(projectId, userId, permission))
                         .limit(1)
                         .projection(new Document())
                         .first() != null;
    }

    @Nonnull
    public List<ProjectPermissionRecord> findByProjectId(@Nonnull ProjectId projectId) {
        List<ProjectPermissionRecord> result = new ArrayList<>();
        collection.find(byProjectId(projectId))
                  .map(d -> converter.fromDocument(d))
                  .into(result);
        return result;
    }

    public List<ProjectPermissionRecord> findByProjectIdAndUserIdIfExists(@Nonnull ProjectId projectId,
                                                                          @Nonnull UserId userId) {
        List<ProjectPermissionRecord> result = new ArrayList<>();
        collection.find(byProjectIdAndUserIdIfExists(projectId, userId))
                  .map(d -> converter.fromDocument(d))
                  .into(result);
        return result;
    }

    public List<ProjectPermissionRecord> findByUserIdAndPermission(@Nonnull UserId userId,
                                                                   @Nonnull Permission permission) {
        List<ProjectPermissionRecord> result = new ArrayList<>();
        collection.find(byUserIdAndPermission(userId, permission))
                  .map(d -> converter.fromDocument(d))
                  .into(result);
        return result;
    }

    public void replace(@Nonnull ProjectId projectId,
                        @Nonnull List<ProjectPermissionRecord> entries) {
        collection.deleteMany(byProjectId(projectId));
        List<Document> documents = entries.stream()
                                          .map(r -> converter.toDocument(r))
                                          .collect(Collectors.toList());
        collection.insertMany(documents);
    }
}
