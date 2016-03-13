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
        for(AccessControlListEntry e : accessControlListRepository.findByProjectIdAndUserId(projectId, userId)) {
            Permission permission = e.getPermission();
            builder.addPermission(permission);
            // TODO: Should this be done here?
            if(permission.isWritePermission()) {
                // Users who can write can also comment
                builder.addPermission(Permission.getCommentPermission());
            }
            else if(permission.isCommentPermission()) {
                builder.addPermission(Permission.getReadPermission());
            }
        }
        ProjectRecord record = projectRecordRepository.findOne(projectId);
        if(record != null) {
            if(record.getOwner().equals(userId)) {
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
        for(AccessControlListEntry e : accessControlListRepository.findByUserId(userId)) {
            ProjectRecord projectRecord = projectRecordRepository.findOne(e.getProjectId());
            result.add(ProjectRecordTranslator.translateToProjectDetails(projectRecord));
        }
        for(ProjectRecord projectRecord : projectRecordRepository.findByOwner(userId)) {
            result.add(ProjectRecordTranslator.translateToProjectDetails(projectRecord));
        }
        return new ArrayList<>(result);
    }
}
