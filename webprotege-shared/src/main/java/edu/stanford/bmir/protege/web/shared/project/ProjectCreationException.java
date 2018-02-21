package edu.stanford.bmir.protege.web.shared.project;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 */
public class ProjectCreationException extends RuntimeException implements Serializable {

    private ProjectId projectId;

    protected ProjectCreationException() {
    }

    public ProjectCreationException(ProjectId projectId, String message) {
        super(message);
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
