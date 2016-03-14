package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectPermissionsManager {

    boolean isUserAdmin(UserId userId);

    List<ProjectDetails> getListableReadableProjects(UserId userId);

    PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId);
}
