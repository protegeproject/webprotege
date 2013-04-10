package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public interface ProjectPermissionsManager {

    boolean hasReadAccess(UserId userId);

    boolean hasWriteAccess(UserId userId);

    boolean hasCommentAccess(UserId userId);
}
