package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public interface PermissionChecker {


    /**
     * Determines if the specified user has the specified permission for the specified project.
     * @param projectId The project.  Not {@code null}.
     * @param userId The user.  Not {@code null}.
     * @param permission The permission. Not {@code null}.
     * @return {@code true} if the user has the permission, otherwise, {@code false}.
     */
    boolean hasPermission(ProjectId projectId, UserId userId, Permission permission);

    /**
     * Determines if the specified user has Write permission for the specified project.
     * @param projectId The project.  Not {@code null}.
     * @param userId The user.  Not {@code null}.
     * @return {@code true} if the user has Write permission, otherwise, {@code false}.
     */
    default boolean hasWritePermission(ProjectId projectId, UserId userId) {
        return hasPermission(projectId, userId, Permission.getWritePermission());
    }

    /**
     * Determines if the specified user has Comment permission for the specified project.
     * @param projectId The project.  Not {@code null}.
     * @param userId The user.  Not {@code null}.
     * @return {@code true} if the user has Comment permission, otherwise, {@code false}.
     */
    default boolean hasCommentPermission(ProjectId projectId, UserId userId) {
        return hasPermission(projectId, userId, Permission.getCommentPermission());
    }

    /**
     * Determines if the specified user has Read permission for the specified project.
     * @param projectId The project.  Not {@code null}.
     * @param userId The user.  Not {@code null}.
     * @return {@code true} if the user has Read permission, otherwise, {@code false}.
     */
    default boolean hasReadPermission(ProjectId projectId, UserId userId) {
        return hasPermission(projectId, userId, Permission.getReadPermission());
    }
}
