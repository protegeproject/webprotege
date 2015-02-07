package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectDetailsManager {
    /**
     * Creates a new project description inside the meta project (with the default access policy etc.)
     * @param newProjectSettings The {@link edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings} that describes the new project.  Not <code>null</code>.
     */
    void registerProject(ProjectId projectId, NewProjectSettings newProjectSettings);

    ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException;

    int getProjectCount();

    boolean isExistingProject(ProjectId projectId);

    boolean isProjectOwner(UserId userId, ProjectId projectId);

    boolean isInTrash(ProjectId projectId);

    void setInTrash(ProjectId projectId, boolean b);

    OWLAPIProjectType getType(ProjectId projectId);

    void setType(ProjectId projectId, OWLAPIProjectType projectType);

    ProjectSettings getProjectSettings(ProjectId projectId);

    void setProjectSettings(ProjectSettings projectSettings);


}
