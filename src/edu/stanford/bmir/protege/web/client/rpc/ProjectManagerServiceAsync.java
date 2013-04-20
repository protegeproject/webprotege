package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.projectconfig.ProjectConfigurationInfo;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;
import java.util.Set;

public interface ProjectManagerServiceAsync {

    /**
     * Creates a new project.  The project is initialised based on the {@link edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings}
     * parameter.
     * @param newProjectSettings A {@link edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings} object that
     * specifies the intended owner of the project,
     * the project name, a description for the project, sources etc. etc.
     * @throws edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException
     *          if the project already exists on the server.
     */
    void createNewProject(NewProjectSettings newProjectSettings, AsyncCallback<ProjectDetails> async);

    /**
     * Gets a list of all project names.
     * @return A list of strings representing the names of project.
     */
    void getProjectNames(AsyncCallback<List<String>> async);

    /**
     * Gets a list of project names for which the signed in user is the owner.
     * @return A list of project names for which the signed in user is the owner.  This list will be empty if the user
     *         is not signed in.
     */
    void getOwnedProjectNames(AsyncCallback<List<String>> async);

    void getLastAccessTime(ProjectId projectId, AsyncCallback<Long> async);


    /**
     * Gets the list of available project types.
     * @return A list of project types.  Not null.
     * @see edu.stanford.bmir.protege.web.client.rpc.data.ProjectType
     */
    void getAvailableProjectTypes(AsyncCallback<List<ProjectType>> async);

    void getProjectType(ProjectId projectId, AsyncCallback<ProjectType> async);

    void setProjectType(ProjectId projectId, ProjectType projectType, AsyncCallback<Void> async);

    void getProjectConfiguration(ProjectId projectId, AsyncCallback<ProjectConfigurationInfo> async);

    void setProjectConfiguration(ProjectConfigurationInfo configuration, AsyncCallback<Void> async);
}
