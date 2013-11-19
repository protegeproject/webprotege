package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 * <p>
 *     Describes the situation where an operation could not be completed because the operation relied on a particular
 *     project being registered.
 * </p>
 */
public class ProjectNotRegisteredException extends RuntimeException implements Serializable {

    private ProjectId projectId;

    private ProjectNotRegisteredException() {
    }

    public ProjectNotRegisteredException(ProjectId projectId) {
        super("Project Not Registered (" + projectId + ")");
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
