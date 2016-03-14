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
import java.util.stream.Stream;

import static edu.stanford.bmir.protege.web.server.project.ProjectRecordTranslator.translateToProjectDetails;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private final AccessControlListRepository accessControlListRepository;

    private final WorldProjectPermissionRecordRepository worldProjectPermissionRecordRepository;

    private final ProjectRecordRepository projectRecordRepository;

    @Inject
    public ProjectPermissionsManagerImpl(AccessControlListRepository accessControlListRepository, WorldProjectPermissionRecordRepository worldProjectPermissionRecordRepository, ProjectRecordRepository projectRecordRepository) {
        this.accessControlListRepository = accessControlListRepository;
        this.worldProjectPermissionRecordRepository = worldProjectPermissionRecordRepository;
        this.projectRecordRepository = projectRecordRepository;
    }

    @Override
    public PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId) {
        PermissionsSet.Builder builder = PermissionsSet.builder();
        accessControlListRepository.findByProjectIdAndUserId(projectId, userId)
                .forEach(r -> {
                    Permission permission = r.getPermission();
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
        // Users can always write, comment and read the projects they own
        Optional<ProjectRecord> record = projectRecordRepository.findOne(projectId);
        if(record.isPresent()) {
            if(record.get().getOwner().equals(userId)) {
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
    public List<ProjectDetails> getListableReadableProjects(UserId userId) {
        Set<ProjectDetails> result = new HashSet<>();
        accessControlListRepository.findByUserId(userId)
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
}
