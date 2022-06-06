package edu.stanford.bmir.protege.web.server.sharing;

import static edu.stanford.bmir.protege.web.server.access.Subject.forAnySignedInUser;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.mongodb.DuplicateKeyException;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.user.HasGetUserIdByUserIdOrEmail;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final Logger logger = LoggerFactory.getLogger(ProjectSharingSettingsManagerImpl.class);

    private final AccessManager accessManager;

    private final HasGetUserIdByUserIdOrEmail userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(AccessManager accessManager,
                                             HasGetUserIdByUserIdOrEmail userLookup) {
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

	// Overwrite existing assignments with blank assignment
	for (Subject subject : accessManager.getSubjectsWithAccessToResource(projectResource)) {
	    try {
		logger.info("Subject: {}, resource: {}", subject, projectResource);
		accessManager.setAssignedRoles(subject, projectResource, Collections.emptySet());
	    } catch (DuplicateKeyException e) {
		// TODO: fix for intermittent 'E11000 duplicate key error collection:
		// webprotege.RoleAssignments
		// index: userName_1_projectId_1
		// dup key: { userName: null, projectId: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
		// }'
		String msg = String.format("Subject: %s, resource: %s", subject, projectResource);
		logger.warn(msg, e);
	    }
	}

        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                                                    .collect(toMap(SharingSetting::getPersonId, s -> s, (s1, s2) -> s1));
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
                logger.warn("User in sharing setting not found.  An email invitation needs to be sent");
                // TODO
                // We need to send the user an email invitation
            }
        }
    }
}
