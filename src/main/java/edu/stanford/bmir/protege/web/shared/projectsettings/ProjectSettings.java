package edu.stanford.bmir.protege.web.shared.projectsettings;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectSettings implements Serializable {

    private ProjectId projectId;
    
    private ProjectType projectType;
    
    private String projectDescription;


    /**
     * For serialization purposes only
     */
    private ProjectSettings() {}

    /**
     * Constructs a ProjectSettingsData object.
     * @param projectId The projectId.  Not {@code null}.
     * @param projectType The ProjectType.  Not {@code null}.
     * @param projectDescription The project description. Not {@code null}.
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    public ProjectSettings(ProjectId projectId, ProjectType projectType, String projectDescription) {
        this.projectId = checkNotNull(projectId);
        this.projectType = checkNotNull(projectType);
        this.projectDescription = checkNotNull(projectDescription);
    }

    /**
     * Gets the projectId.
     * @return The projectId.  Not {@code null}.
     */
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the project description.
     * @return The project description as a string.  May be empty. Not {@code null}.
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * Gets the project type.
     * @return The project type.  Not {@code null}.
     */
    public ProjectType getProjectType() {
        return projectType;
    }


    @Override
    public int hashCode() {
        return projectType.hashCode() + projectDescription.hashCode() + projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectSettings)) {
            return false;
        }
        ProjectSettings other = (ProjectSettings) obj;
        return this.projectType.equals(other.projectType) && this.projectDescription.equals(other.projectDescription) && this.projectId.equals(other.projectId);
    }

    @Override
    public String toString() {
        return "ProjectSettingsData(" + projectId + " Type(" + projectType + ") Description(" + projectDescription + ")";
    }
}
