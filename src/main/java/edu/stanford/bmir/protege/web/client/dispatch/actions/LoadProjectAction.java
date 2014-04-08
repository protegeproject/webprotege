package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectAction implements Action<LoadProjectResult>, HasProjectId {

    private ProjectId projectId;

    /**
     * For serialization purposes onlu
     */
    private LoadProjectAction() {

    }

    public LoadProjectAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
