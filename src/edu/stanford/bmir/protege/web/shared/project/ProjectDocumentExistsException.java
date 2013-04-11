package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
public class ProjectDocumentExistsException extends ProjectAlreadyExistsException implements Serializable {

    private ProjectDocumentExistsException() {
    }

    public ProjectDocumentExistsException(ProjectId projectId) {
        super(projectId, "Project document already exists: " + projectId.getId());
    }

}
