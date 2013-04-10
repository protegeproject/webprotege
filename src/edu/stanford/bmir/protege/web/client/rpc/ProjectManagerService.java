package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.ui.projectconfig.ProjectConfigurationInfo;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;
import java.util.Set;

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
     * Gets a list of projects which are registered, which have project documents associated with them and which can
     * be listed by the caller.
     * @return A list of all projects.
     */
    List<ProjectData> getProjects();

    /**
     * Gets a list of projects which are registered, which have project documents associated with them, that are
     * owned by a particular user, and that can be listed by the caller.
     * @param userId The user id.
     * @return A list of projects that are owned by the user with the specified Id.
     */
    List<ProjectData> getProjects(UserId userId);

    ProjectData getProjectData(ProjectId projectId) throws ProjectNotRegisteredException;

    /**
     * Gets a list of ALL project names in the system whether or not the caller can list them.
     * @return A list of ALL project names in the system.
     */
    List<String> getProjectNames();

    /**
     * Gets a list of project names for which the caller corresponds to the owner.
     * @return A list of project names for which the signed in user is the owner.  This list will be empty if the user
     *         is not signed in.
     */
    List<String> getOwnedProjectNames();

    /**
     * Determines if there is already a registered project with a particular name.
     *
     * @param projectId The project id to test for.  Not null.
     * @return <code>true</code> if a project with projectName exists, or <code>false</code> if there is
     *         no project with a name corresponding to projectName.
     * @throws NullPointerException if projectName is null.
     */
    boolean isRegisteredProject(ProjectId projectId);

    /**
     * Creates a new project.  The project is initialised based on the {@link NewProjectSettings} parameter. The call
     * will fail if the project is already registered or if the project is not registered but a (legacy) project document
     * exists.  By default, the project will be a private project (does not appear in the project list) with the owner
     * as a writer.
     * @param newProjectSettings A {@link NewProjectSettings} object that specifies the intended owner of the project,
     * the project name, a description for the project, sources etc. etc.
     * @throws NotSignedInException if the caller is not signed in.
     * @throws ProjectAlreadyRegisteredException If the project is already registered and the caller is not the owner.
     * @throws ProjectDocumentExistsException If the project is not registered but the project document already exists.
     */
    ProjectDetails createNewProject(NewProjectSettings newProjectSettings) throws NotSignedInException, ProjectAlreadyRegisteredException, ProjectDocumentExistsException;

//    /**
//     * Replaces the project document for an EXISTING registered project.
//     * @param newProjectSettings The settings that describe both a currently registered project and how to overwrite the
//     * current project document.
//     * @throws NotSignedInException If the caller is not signed in.
//     * @throws ProjectNotRegisteredException If the NewProjectSettings object does not refer to a project that is already
//     * registered.  The {@link edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings#getProjectName()} must
//     * match a the name of a registered project.
//     * @throws NotProjectOwnerException If the caller is not the owner of the existing registered project.  Note that
//     * there is no requirement for the caller to be the owner of the final overwritten project (i.e. this method supports
//     * a change in ownership).
//     */
//    void replaceProjectDocument(NewProjectSettings newProjectSettings) throws NotSignedInException, ProjectNotRegisteredException, NotProjectOwnerException;


    /**
     * Moves a set of projects to the trash.
     * @param projectIds The projects to move to the trash.  Not null.
     * @throws NullPointerException if projectIds is null.
     */
    void moveProjectsToTrash(Set<ProjectId> projectIds) throws NotSignedInException, NotProjectOwnerException;

    /**
     * Removes a set of projects from the trash.
     * @param projectIds The projects to be removed.  Not null.
     * @throws NullPointerException is projectIds is null.
     */
    void removeProjectsFromTrash(Set<ProjectId> projectIds) throws NotSignedInException, NotProjectOwnerException;

    long getLastAccessTime(ProjectId projectId);

    /**
     * Gets the list of available project types.
     * @see ProjectType
     * @return A list of project types.  Not null.
     */
    List<ProjectType> getAvailableProjectTypes();


    ProjectType getProjectType(ProjectId projectId) throws ProjectNotRegisteredException;

    void setProjectType(ProjectId projectId, ProjectType projectType) throws NotProjectOwnerException, ProjectNotRegisteredException;
    
    
    ProjectConfigurationInfo getProjectConfiguration(ProjectId projectId) throws ProjectNotRegisteredException;

    void setProjectConfiguration(ProjectConfigurationInfo configuration) throws ProjectNotRegisteredException, NotProjectOwnerException;
}
