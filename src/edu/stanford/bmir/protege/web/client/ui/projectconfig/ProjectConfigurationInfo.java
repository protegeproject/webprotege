package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectConfigurationInfo implements Serializable {

    private ProjectId projectId;
    
    private ProjectType projectType;
    
    private String projectDescription;

    // For serialization
    private ProjectConfigurationInfo() {

    }

    public ProjectConfigurationInfo(ProjectId projectId, ProjectType projectType, String projectDescription) {
        this.projectId = projectId;
        this.projectType = projectType;
        this.projectDescription = projectDescription;
    }


    public ProjectId getProjectId() {
        return projectId;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public ProjectType getProjectType() {
        return projectType;
    }
    
    @Override
    public int hashCode() {
        return projectType.hashCode() + projectDescription.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectConfigurationInfo)) {
            return false;
        }
        ProjectConfigurationInfo other = (ProjectConfigurationInfo) obj;
        return this.projectType.equals(other.projectType) && this.projectDescription.equals(other.projectDescription);
    }

    @Override
    public String toString() {
        return "ProjectConfigurationInfo(" + projectId + " Type(" + projectType + ") Description(" + projectDescription + ")";
    }
}
