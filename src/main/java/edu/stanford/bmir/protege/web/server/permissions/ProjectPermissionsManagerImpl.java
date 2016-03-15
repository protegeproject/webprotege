package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.project.ProjectRecord;
import edu.stanford.bmir.protege.web.server.project.ProjectRecordRepository;
import edu.stanford.bmir.protege.web.server.project.ProjectRecordTranslator;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;

import static edu.stanford.bmir.protege.web.server.project.ProjectRecordTranslator.translateToProjectDetails;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private final ProjectPermissionRecordRepository projectPermissionRecordRepository;

    private final WorldProjectPermissionRecordRepository worldProjectPermissionRecordRepository;

    private final ProjectRecordRepository projectRecordRepository;

    @Inject
    public ProjectPermissionsManagerImpl(ProjectPermissionRecordRepository projectPermissionRecordRepository, WorldProjectPermissionRecordRepository worldProjectPermissionRecordRepository, ProjectRecordRepository projectRecordRepository) {
        this.projectPermissionRecordRepository = projectPermissionRecordRepository;
        this.worldProjectPermissionRecordRepository = worldProjectPermissionRecordRepository;
        this.projectRecordRepository = projectRecordRepository;
    }

    @Override
    public PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId) {
        PermissionsSet.Builder builder = PermissionsSet.builder();
        projectPermissionRecordRepository.findByProjectIdAndUserId(projectId, userId)
                .forEach(r -> {
                    r.getPermissions().forEach(permission -> {
                        builder.addPermission(permission);
                        if(permission.isWritePermission()) {
                            // Users who can write can also comment
                            builder.addPermission(Permission.getCommentPermission());
                        }
                        else if(permission.isCommentPermission()) {
                            // Users who can comment can also read
                            builder.addPermission(Permission.getReadPermission());
                        }
                    });
                });
        // Users can always write, comment and read the projects they own.  They are also admins.
        Optional<ProjectRecord> record = projectRecordRepository.findOne(projectId);
        if(record.isPresent()) {
            if(record.get().getOwner().equals(userId)) {
                builder.addPermission(Permission.getAdminPermission());
                builder.addPermission(Permission.getWritePermission());
                builder.addPermission(Permission.getCommentPermission());
                builder.addPermission(Permission.getReadPermission());
            }
        }
        // World permissions
        worldProjectPermissionRecordRepository.findAllByProjectId(projectId)
                .forEach(r -> builder.addPermission(r.getPermission()));
        return builder.build();
    }

    @Override
    public List<ProjectDetails> getReadableProjects(UserId userId) {
        Set<ProjectDetails> result = new HashSet<>();
        projectPermissionRecordRepository.findByUserId(userId)
                .forEach(r -> {
                    Optional<ProjectRecord> record = projectRecordRepository.findOne(r.getProjectId());
                    result.add(translateToProjectDetails(record.get()));
                });
        projectRecordRepository.findByOwner(userId)
                .map(ProjectRecordTranslator::translateToProjectDetails)
                .forEach(result::add);

        // We don't show projects for which the user can access due to world permissions
        return new ArrayList<>(result);
    }

    @Override
    public boolean hasPermission(ProjectId projectId, UserId userId, Permission permission) {
        int count = projectPermissionRecordRepository.countByProjectIdAndUserIdAndPermissions(projectId, userId, permission);
        if(count > 0) {
            return true;
        }
        int worldCount = worldProjectPermissionRecordRepository.countByProjectIdAndPermission(projectId, permission);
        return worldCount > 0;
    }
}
