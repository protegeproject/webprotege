package edu.stanford.bmir.protege.web.client.rpc.data;

import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 */
public class NotProjectOwnerException extends PermissionDeniedException {

    private ProjectId projectId;

    private NotProjectOwnerException() {
    }

    public NotProjectOwnerException(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
