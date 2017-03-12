package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.user.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;

import static edu.stanford.bmir.protege.web.server.access.Subject.forAnySignedInUser;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final WebProtegeLogger logger;

    private final AccessManager accessManager;

    private final HasGetUserIdByUserIdOrEmail userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(WebProtegeLogger logger,
                                             AccessManager accessManager,
                                             HasGetUserIdByUserIdOrEmail userLookup) {
        this.logger = logger;
        this.accessManager = accessManager;
        this.userLookup = userLookup;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        List<SharingSetting> sharingSettings = new ArrayList<>();
        ProjectResource projectResource = new ProjectResource(projectId);
        Collection<Subject> subjects = accessManager.getSubjectsWithAccessToResource(projectResource);
        subjects.stream()
                .filter(s -> !s.isGuest())
                .filter(s -> s.getUserName().isPresent())
                .map(s -> UserId.getUserId(s.getUserName().get()))
                .forEach(u -> {
                    Collection<RoleId> roles = accessManager.getAssignedRoles(Subject.forUser(u), projectResource);
                    Roles.toSharingPermission(roles).ifPresent(
                            p -> sharingSettings.add(new SharingSetting(PersonId.of(u), p)));

                });
        Collection<RoleId> signedInUserRoles = accessManager.getAssignedRoles(forAnySignedInUser(), projectResource);
        Optional<SharingPermission> linkSharing = Roles.toSharingPermission(signedInUserRoles);
        return new ProjectSharingSettings(projectId, linkSharing, sharingSettings);
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        ProjectResource projectResource = new ProjectResource(projectId);

        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                                                    .collect(toMap(s -> s.getPersonId(), s -> s, (s1, s2) -> s1));
        Optional<SharingPermission> linkSharingPermission = settings.getLinkSharingPermission();
        linkSharingPermission.ifPresent(permission -> {
            Collection<RoleId> roleId = Roles.fromSharingPermission(permission);
            accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, roleId);
        });
        if(!linkSharingPermission.isPresent()) {
            accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, emptySet());
        }
        for (SharingSetting setting : map.values()) {
            PersonId personId = setting.getPersonId();
            Optional<UserId> userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            if (userId.isPresent()) {
                ImmutableSet<RoleId> roles = Roles.fromSharingPermission(setting.getSharingPermission());
                accessManager.setAssignedRoles(forUser(userId.get()),
                                               projectResource,
                                               roles);
            }
            else {
                logger.info(projectId, "User in sharing setting not found.  An email invitation needs to be sent");
                // TODO
                // We need to send the user an email invitation
            }
        }
    }
}
