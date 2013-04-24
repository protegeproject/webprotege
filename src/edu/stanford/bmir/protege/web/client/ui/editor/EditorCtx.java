package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public abstract class EditorCtx {

    private ProjectId projectId;

    protected EditorCtx(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
