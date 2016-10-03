package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.collect.ImmutableSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Oct 2016
 */
public class ProjectPermissionRecordConverter implements DocumentConverter<ProjectPermissionRecord> {

    private static final String PROJECT_ID = "projectId";

    private static final String USER_ID = "userId";

    private static final String PERMISSIONS = "permissions";

    @Inject
    public ProjectPermissionRecordConverter() {
    }

    @Override
    public Document toDocument(@Nonnull ProjectPermissionRecord object) {
        Document document = new Document();
        document.append(PROJECT_ID, object.getProjectId().getId());
        object.getUserId().ifPresent(userId -> document.append(USER_ID, userId.getUserName()));
        List<String> permissions = object.getPermissions().stream()
                .map(permission -> permission.getPermissionName())
                .collect(toList());
        document.append(PERMISSIONS, permissions);
        return document;
    }

    @SuppressWarnings("unchecked" )
    @Override
    public ProjectPermissionRecord fromDocument(@Nonnull Document document) {
        ProjectId projectId = ProjectId.get(document.getString(PROJECT_ID));
        Optional<UserId> userId = Optional.ofNullable(document.getString(USER_ID))
                .map(s -> UserId.getUserId(s));
        ImmutableSet.Builder<Permission> builder = ImmutableSet.builder();
        ((List<String>) document.get(PERMISSIONS)).stream()
                .map(s -> Permission.getPermission(s))
                .forEach(p -> builder.add(p));
        return new ProjectPermissionRecord(projectId, userId, builder.build());
    }

    public static Bson byProjectIdAndUserIdIfExistsAndPermission(ProjectId projectId, UserId userId, Permission permission) {
        return and(
                eq(PROJECT_ID, projectId.getId()),
                or(
                        not(exists(USER_ID)),
                        eq(USER_ID, userId.getUserName())),
                eq(PERMISSIONS, permission.getPermissionName()));
    }

    public static Bson byProjectId(ProjectId projectId) {
        return eq(PROJECT_ID, projectId.getId());
    }

    public static Bson byProjectIdAndUserIdIfExists(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        return and(eq(PROJECT_ID, projectId.getId()),
                   or(not(exists(USER_ID)), eq(USER_ID, userId.getUserName())));
    }

    public static Bson byUserIdAndPermission(@Nonnull UserId userId, @Nonnull Permission permission) {
        return and(eq(USER_ID, userId.getUserName()), eq(PERMISSIONS, permission.getPermissionName()));
    }

    public void ensureIndexes(@Nonnull MongoCollection<Document> collection) {
        Document document = new Document();
        document.append(PROJECT_ID, 1);
        document.append(USER_ID, 1);
        collection.createIndex(document, new IndexOptions().unique(true));
    }
}
