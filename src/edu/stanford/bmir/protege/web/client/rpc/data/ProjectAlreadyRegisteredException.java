package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 * <p>
 *     An exception which describes the situation where a client attempted to create a project which was already
 *     registered with the system.  This does not imply that the project can be retrieved, loaded, or that project
 *     source documents exist - just that the the project cannot be re-registered.
 * </p>
 */
public class ProjectAlreadyRegisteredException extends ProjectAlreadyExistsException implements Serializable {

    private ProjectAlreadyRegisteredException() {
        super();
    }

    public ProjectAlreadyRegisteredException(ProjectId projectId) {
        super(projectId, "Project already registered (" + projectId.getProjectName() + ")");
    }
}
