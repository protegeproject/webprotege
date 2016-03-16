package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public interface LoggedInUserProjectPermissionChecker {

    void hasWritePermission(DispatchServiceCallback<Boolean> callback);

    void hasReadPermission(DispatchServiceCallback<Boolean> callback);

    void hasCommentPermission(DispatchServiceCallback<Boolean> callback);
}
