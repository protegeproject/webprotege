package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.NotProjectOwnerException;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.shared.project.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 * <p>
 * The ProjectManagerService provides an interface to common project management tasks.  It allows projects
 * to be queried, created, trashed etc.
 * </p>
 */
@RemoteServiceRelativePath("projectmanager")
public interface ProjectManagerService extends RemoteService {

    /**
     * Creates a new project.  The project is initialised based on the {@link NewProjectSettings} parameter. The call
     * will fail if the project is already registered or if the project is not registered but a (legacy) project document
     * exists.  By default, the project will be a private project (does not appear in the project list) with the owner
     * as a writer.
     * @param newProjectSettings A {@link NewProjectSettings} object that specifies the intended owner of the project,
     * the project name, a description for the project, sources etc. etc.
     * @throws NotSignedInException if the caller is not signed in.
     * @throws edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyRegisteredException If the project is already registered and the caller is not the owner.
     * @throws edu.stanford.bmir.protege.web.shared.project.ProjectDocumentExistsException If the project is not registered but the project document already exists.
     */
    ProjectDetails createNewProject(NewProjectSettings newProjectSettings) throws NotSignedInException, ProjectAlreadyRegisteredException, ProjectDocumentExistsException;
}
