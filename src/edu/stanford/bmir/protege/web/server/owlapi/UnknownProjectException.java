package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class UnknownProjectException extends RuntimeException implements Serializable {

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    private UnknownProjectException() {
    }

    public UnknownProjectException(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
