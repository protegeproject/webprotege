package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.project.ProjectExistsFilter;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private final ProjectPermissionRecordRepository projectPermissionRecordRepository;

    private final WorldProjectPermissionRecordRepository worldProjectPermissionRecordRepository;

    private final ProjectDetailsRepository projectDetailsRepository;

    private final ProjectExistsFilter projectExistsFilter;

    @Inject
    public ProjectPermissionsManagerImpl(ProjectPermissionRecordRepository projectPermissionRecordRepository, WorldProjectPermissionRecordRepository worldProjectPermissionRecordRepository, ProjectDetailsRepository projectDetailsRepository, ProjectExistsFilter projectExistsFilter) {
        this.projectPermissionRecordRepository = projectPermissionRecordRepository;
        this.worldProjectPermissionRecordRepository = worldProjectPermissionRecordRepository;
        this.projectDetailsRepository = projectDetailsRepository;
        this.projectExistsFilter = projectExistsFilter;
    }

    @Override
    public PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId) {
        PermissionsSet.Builder builder = PermissionsSet.builder();
        projectPermissionRecordRepository.findByProjectIdAndUserId(projectId, userId)
                .forEach(r -> {
                    r.getPermissions().forEach(permission -> {
                        builder.addPermission(permission);
                        if (permission.isWritePermission()) {
                            // Users who can write can also comment
                            builder.addPermission(Permission.getCommentPermission());
                        }
                        else if (permission.isCommentPermission()) {
                            // Users who can comment can also read
                            builder.addPermission(Permission.getReadPermission());
                        }
                    });
                });
        // Users can always write, comment and read the projects they own.  They are also admins.
        if (projectDetailsRepository.containsProjectWithOwner(projectId, userId)) {
            builder.addPermission(Permission.getAdminPermission());
            builder.addPermission(Permission.getWritePermission());
            builder.addPermission(Permission.getCommentPermission());
            builder.addPermission(Permission.getReadPermission());
        }
        // World permissions
        Optional<WorldProjectPermissionRecord> worldRecord = worldProjectPermissionRecordRepository.findOneByProjectId(
                projectId);
        if (worldRecord.isPresent()) {
            worldRecord.get().getPermissions().forEach(builder::addPermission);
        }
        return builder.build();
    }

    @Override
    public List<ProjectDetails> getReadableProjects(UserId userId) {
        Set<ProjectDetails> result = new HashSet<>();
        projectPermissionRecordRepository.findByUserId(userId)
                .forEach(r -> {
                    Optional<ProjectDetails> record = projectDetailsRepository.findOne(r.getProjectId());
                    if (record.isPresent() && projectExistsFilter.isProjectPresent(record.get().getProjectId())) {
                        result.add(record.get());
                    }
                });
        result.addAll(projectDetailsRepository.findByOwner(userId));

        // We don't show projects for which the user can access due to world permissions
        return new ArrayList<>(result);
    }

    @Override
    public boolean hasPermission(ProjectId projectId, UserId userId, Permission permission) {
        int count = projectPermissionRecordRepository.countByProjectIdAndUserIdAndPermissions(projectId,
                                                                                              userId,
                                                                                              permission);
        if (count > 0) {
            return true;
        }
        int worldCount = worldProjectPermissionRecordRepository.countByProjectIdAndPermissions(projectId, permission);
        return worldCount > 0;
    }
}
