package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.user.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecord;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecordRepository;
import edu.stanford.bmir.protege.web.server.permissions.WorldProjectPermissionRecordRepository;
import edu.stanford.bmir.protege.web.server.permissions.WorldProjectPermissionRecord;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final WebProtegeLogger logger;
    
    private final WorldProjectPermissionRecordRepository worldRepository;

    private final ProjectPermissionRecordRepository repository;

    private final HasGetUserIdByUserIdOrEmail userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(WebProtegeLogger logger, WorldProjectPermissionRecordRepository worldRepository, ProjectPermissionRecordRepository repository, HasGetUserIdByUserIdOrEmail userLookup) {
        this.logger = logger;
        this.worldRepository = worldRepository;
        this.repository = repository;
        this.userLookup = userLookup;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        Multimap<UserId, Permission> userPermissions = HashMultimap.create();
        repository.findByProjectId(projectId)
                .forEach(e -> userPermissions.putAll(e.getUserId(), e.getPermissions()));


        Set<SharingSetting> sharingSettings = new HashSet<>();
        for(UserId userId : userPermissions.keys()) {
            Collection<Permission> permissions = userPermissions.get(userId);
            Optional<SharingPermission> sharingPermission = PermissionMapper.mapToSharingPermission(permissions);
            if(sharingPermission.isPresent()) {
                sharingSettings.add(new SharingSetting(new PersonId(userId.getUserName()), sharingPermission.get()));
            }
        }
        java.util.Optional<WorldProjectPermissionRecord> worldRecord = worldRepository.findOneByProjectId(projectId);
        final Optional<SharingPermission> worldPermission;
        if(worldRecord.isPresent()) {
            Set<Permission> worldPermissions = worldRecord.get().getPermissions();
            worldPermission = PermissionMapper.mapToSharingPermission(worldPermissions);
        }
        else {
            worldPermission = Optional.absent();
        }
        return new ProjectSharingSettings(projectId, worldPermission, new ArrayList<>(sharingSettings));
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        List<ProjectPermissionRecord> entries = new ArrayList<>();
        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                .collect(toMap(SharingSetting::getPersonId, s -> s));
        for(SharingSetting setting : map.values()) {
            PersonId personId = setting.getPersonId();
            Optional<UserId> userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            if(userId.isPresent()) {
                ImmutableSet<Permission> permissions = getPermission(setting.getSharingPermission());
                ProjectPermissionRecord e = new ProjectPermissionRecord(
                        projectId,
                        userId.get(),
                        permissions);
                entries.add(e);
            }
            else {
                logger.info(projectId, "User in sharing setting not found.  An email invitation needs to be sent");
                // TODO
                // We need to send the user an email invitation
            }
        }
        repository.deleteByProjectId(projectId);
        if(!entries.isEmpty()) {
            repository.save(entries);
        }
        worldRepository.deleteAllByProjectId(projectId);
        if(settings.getLinkSharingPermission().isPresent()) {
            ImmutableSet<Permission> permissions = getPermission(settings.getLinkSharingPermission().get());
             worldRepository.save(new WorldProjectPermissionRecord(projectId, permissions));
        }
    }

    /**
     * Applies the default sharing setting to a project.  The default sharing settings are that the project is private,
     * but the signed in user is an editor.
     * @param projectId The project id that identifies the project to apply sharing settings to.
     */
    @Override
    public void applyDefaultSharingSettings(ProjectId projectId, UserId userId) {
        List<SharingSetting> userSharingSettings = new ArrayList<>();
        if (!userId.isGuest()) {
            userSharingSettings.add(new SharingSetting(new PersonId(userId.getUserName()), SharingPermission.EDIT));
        }
        ProjectSharingSettings sharingSettings = new ProjectSharingSettings(projectId, Optional.absent(), userSharingSettings);
        setProjectSharingSettings(sharingSettings);
    }

    private ImmutableSet<Permission> getPermission(SharingPermission sharingPermission) {
        ImmutableSet.Builder<Permission> permissions = new ImmutableSet.Builder<Permission>();
        switch (sharingPermission) {
            case EDIT:
                permissions.add(Permission.getWritePermission());
                permissions.add(Permission.getCommentPermission());
                permissions.add(Permission.getReadPermission());
                break;
            case COMMENT:
                permissions.add(Permission.getCommentPermission());
                permissions.add(Permission.getReadPermission());
                break;
            case VIEW:
                permissions.add(Permission.getReadPermission());
        }
        return permissions.build();
    }
}
