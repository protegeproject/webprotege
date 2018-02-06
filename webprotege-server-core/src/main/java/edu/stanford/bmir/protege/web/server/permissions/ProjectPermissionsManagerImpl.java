package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.Resource;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private final AccessManager accessManager;

    private final ProjectDetailsRepository projectDetailsRepository;


    @Inject
    public ProjectPermissionsManagerImpl(@Nonnull AccessManager accessManager,
                                         @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        this.accessManager = accessManager;
        this.projectDetailsRepository = projectDetailsRepository;
    }

    @Override
    public List<ProjectDetails> getReadableProjects(UserId userId) {
        Set<ProjectDetails> result = new HashSet<>();
        accessManager.getResourcesAccessibleToSubject(forUser(userId), VIEW_PROJECT.getActionId())
                     .stream()
                     .filter(Resource::isProject)
                     .forEach(
                             resource ->
                                     resource.getProjectId().ifPresent(
                                             projectId -> projectDetailsRepository
                                                     .findOne(projectId)
                                                     .ifPresent(result::add)));
        // Always add owned in case permissions are screwed up - yes?
        // It will be obvious that the permissions are screwed up because the
        // user won't be able to open their own project.
        result.addAll(projectDetailsRepository.findByOwner(userId));
        return new ArrayList<>(result);
    }
}
