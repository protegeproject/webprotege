package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public interface PermissionChecker {

    boolean hasWritePermissionForProject(UserId userId, ProjectId projectId);

    boolean hasReadPermissionForProject(UserId userId, ProjectId projectId);
}
