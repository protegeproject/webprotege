package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.permissions.AccessControlListEntry;
import edu.stanford.bmir.protege.web.server.permissions.AccessControlListRepository;
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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final WebProtegeLogger logger;

    private final AccessControlListRepository repository;

    private final HasGetUserIdByUserIdOrEmail userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(AccessControlListRepository repository, HasGetUserIdByUserIdOrEmail userLookup, WebProtegeLogger logger) {
        this.repository = repository;
        this.userLookup = userLookup;
        this.logger = logger;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        List<SharingSetting> sharingSettings = new ArrayList<>();
        for(AccessControlListEntry e : repository.findByProjectId(projectId)) {
            SharingSetting sharingSetting = new SharingSetting(new PersonId(e.getProjectId().getId()), getSharingPermission(e.getPermission()));
            sharingSettings.add(sharingSetting);
        }
        return new ProjectSharingSettings(projectId, SharingPermission.VIEW, sharingSettings);
    }

    private SharingPermission getSharingPermission(Permission permission) {
        if(permission.isReadPermission()) {
            return SharingPermission.VIEW;
        }
        else if(permission.isCommentPermission()) {
            return SharingPermission.COMMENT;
        }
        else if(permission.isWritePermission()) {
            return SharingPermission.EDIT;
        }
        else {
            return SharingPermission.NONE;
        }
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings) {
        ProjectId projectId = projectSharingSettings.getProjectId();
        List<AccessControlListEntry> entries = new ArrayList<>();
        for(SharingSetting settings : projectSharingSettings.getSharingSettings()) {
            PersonId personId = settings.getPersonId();
            Optional<UserId> userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            if(userId.isPresent()) {
                Collection<Permission> permissions = getPermission(settings.getSharingPermission());
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
        ProjectSharingSettings sharingSettings = new ProjectSharingSettings(projectId, SharingPermission.NONE, userSharingSettings);
        this.setProjectSharingSettings(sharingSettings);
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
            case VIEW:
                permissions.add(Permission.getReadPermission());
        }
        return permissions;
    }
}
