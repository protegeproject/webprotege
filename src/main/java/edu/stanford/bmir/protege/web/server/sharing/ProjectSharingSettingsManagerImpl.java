package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.metaproject.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.server.permissions.AccessControlListEntry;
import edu.stanford.bmir.protege.web.server.permissions.AccessControlListRepository;
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

    private final AccessControlListRepository repository;

    private final HasGetUserIdByUserIdOrEmail userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(WebProtegeLogger logger, WorldProjectPermissionRecordRepository worldRepository, AccessControlListRepository repository, HasGetUserIdByUserIdOrEmail userLookup) {
        this.logger = logger;
        this.worldRepository = worldRepository;
        this.repository = repository;
        this.userLookup = userLookup;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        Multimap<UserId, Permission> userPermissions = HashMultimap.create();
        repository.findByProjectId(projectId)
                .forEach(e -> userPermissions.put(e.getUserId(), e.getPermission()));


        Set<SharingSetting> sharingSettings = new HashSet<>();
        for(UserId userId : userPermissions.keys()) {
            Collection<Permission> permissions = userPermissions.get(userId);
            Optional<SharingPermission> sharingPermission = PermissionMapper.mapToSharingPermission(permissions);
            if(sharingPermission.isPresent()) {
                sharingSettings.add(new SharingSetting(new PersonId(userId.getUserName()), sharingPermission.get()));
            }
        }
        Set<Permission> worldPermissions = worldRepository.findAllByProjectId(projectId)
                .map(WorldProjectPermissionRecord::getPermission)
                .collect(toSet());
        final Optional<SharingPermission> worldPermission = PermissionMapper.mapToSharingPermission(worldPermissions);
        return new ProjectSharingSettings(projectId, worldPermission, new ArrayList<>(sharingSettings));
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        List<AccessControlListEntry> entries = new ArrayList<>();
        for(SharingSetting setting : settings.getSharingSettings()) {
            PersonId personId = setting.getPersonId();
            Optional<UserId> userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            if(userId.isPresent()) {
                Collection<Permission> permissions = getPermission(setting.getSharingPermission());
                for(Permission permission : permissions) {
                    AccessControlListEntry e = new AccessControlListEntry(
                            projectId,
                            userId.get(),
                            permission);
                    entries.add(e);
                }
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
            for(Permission permission : getPermission(settings.getLinkSharingPermission().get())) {
                worldRepository.save(new WorldProjectPermissionRecord(projectId, permission));
            }
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

    private Collection<Permission> getPermission(SharingPermission sharingPermission) {
        List<Permission> permissions = new ArrayList<>();
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
        return permissions;
    }
}
