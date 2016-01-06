package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public interface PermissionChecker {

    void hasWritePermissionForProject(UserId userId, ProjectId projectId, DispatchServiceCallback<Boolean> callback);

    void hasReadPermissionForProject(UserId userId, ProjectId projectId, DispatchServiceCallback<Boolean> callback);

    void hasCommentPermissionForProject(UserId userId, ProjectId projectId, DispatchServiceCallback<Boolean> callback);
}
