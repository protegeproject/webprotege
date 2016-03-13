package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.server.permissions.AccessControlListEntry;
import edu.stanford.bmir.protege.web.server.permissions.AccessControlListRepository;
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
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private final AccessControlListRepository accessControlListRepository;

    private final ProjectRecordRepository projectRecordRepository;


    @Inject
    public ProjectPermissionsManagerImpl(AccessControlListRepository accessControlListRepository, ProjectRecordRepository projectRecordRepository) {
        this.accessControlListRepository = accessControlListRepository;
        this.projectRecordRepository = projectRecordRepository;
    }

    @Override
    public boolean isUserAdmin(UserId userId) {
        return false;
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
        Optional<ProjectRecord> record = projectRecordRepository.findOne(projectId);
        if(record.isPresent()) {
            if(record.get().getOwner().equals(userId)) {
                builder.addPermission(Permission.getWritePermission());
                builder.addPermission(Permission.getCommentPermission());
                builder.addPermission(Permission.getReadPermission());
            }
        }
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
                .map(r -> translateToProjectDetails(r))
                .forEach(d -> result.add(d));

        return new ArrayList<>(result);
    }
}
