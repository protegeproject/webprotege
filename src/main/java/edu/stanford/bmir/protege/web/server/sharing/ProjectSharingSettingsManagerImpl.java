package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecord;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecordRepository;
import edu.stanford.bmir.protege.web.server.user.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.sharing.Permissions.toSharingPermission;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final WebProtegeLogger logger;
    
    private final ProjectPermissionRecordRepository projectPermissionRepository;

    private final HasGetUserIdByUserIdOrEmail userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(WebProtegeLogger logger, ProjectPermissionRecordRepository repository, HasGetUserIdByUserIdOrEmail userLookup) {
        this.logger = logger;
        this.projectPermissionRepository = repository;
        this.userLookup = userLookup;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        List<ProjectPermissionRecord> records = projectPermissionRepository.findByProjectId(projectId);
        List<SharingSetting> sharingSettings = new ArrayList<>();
        Optional<SharingPermission> linkSharing = Optional.empty();
        for(ProjectPermissionRecord record : records) {
            Optional<UserId> userId = record.getUserId();
            if(userId.isPresent()) {
                Optional<SharingPermission> sharingPermission = toSharingPermission(record.getPermissions());
                SharingSetting sharingSetting = new SharingSetting(
                        PersonId.of(userId.get()),
                        sharingPermission.get()
                );
                sharingSettings.add(sharingSetting);
            }
            else {
                linkSharing = toSharingPermission(record.getPermissions());
            }
        }
        return new ProjectSharingSettings(projectId, linkSharing, sharingSettings);
    }



    @Override
    public void setProjectSharingSettings(ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        List<ProjectPermissionRecord> entries = new ArrayList<>();
        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                .collect(toMap(s -> s.getPersonId(), s -> s, (s1, s2) -> s1));
        for(SharingSetting setting : map.values()) {
            PersonId personId = setting.getPersonId();
            Optional<UserId> userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            if(userId.isPresent()) {
                ImmutableSet<Permission> permissions = Permissions.fromSharingPermission(setting.getSharingPermission());
                ProjectPermissionRecord e = new ProjectPermissionRecord(
                        projectId,
                        userId,
                        permissions);
                entries.add(e);
            }
            else {
                logger.info(projectId, "User in sharing setting not found.  An email invitation needs to be sent");
                // TODO
                // We need to send the user an email invitation
            }
        }
        projectPermissionRepository.replace(projectId, entries);
//        if(!entries.isEmpty()) {
//            projectPermissionRepository.save(entries);
//        }
    }
}
