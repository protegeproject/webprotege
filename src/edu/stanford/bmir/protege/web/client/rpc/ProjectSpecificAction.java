package edu.stanford.bmir.protege.web.client.rpc;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/12/2012
 */
public abstract class ProjectSpecificAction<R extends Response> implements Action<R> {

    private ProjectId projectId;

    protected ProjectSpecificAction() {

    }

    protected ProjectSpecificAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
