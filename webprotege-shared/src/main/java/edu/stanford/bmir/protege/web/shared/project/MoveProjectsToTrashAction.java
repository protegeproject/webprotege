package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class MoveProjectsToTrashAction implements Action<MoveProjectsToTrashResult> {

    private ProjectId projectId;

    private MoveProjectsToTrashAction() {
    }

    public MoveProjectsToTrashAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
