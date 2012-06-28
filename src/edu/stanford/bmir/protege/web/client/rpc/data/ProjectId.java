package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 * <p>
 *     A simple object that identifies a project.
 * </p>
 */
public class ProjectId implements Serializable {

    private String projectName = "";


    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectId() {
    }

    /**
     * Constructs a ProjectId.
     * @param projectName The name of the project.  Not <code>null</code>.
     * @throws NullPointerException if the projectName parameter is <code>null</code>.
     */
    public ProjectId(String projectName) {
        if(projectName == null) {
            throw new NullPointerException("The projectName parameter must not be null.");
        }
        this.projectName = projectName;
    }


    public String getProjectName() {
        return projectName;
    }

    @Override
    public String toString() {
        return "Project(" + projectName + ")";
    }

    @Override
    public int hashCode() {
        return projectName == null ? 0 : projectName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectId)) {
            return false;
        }
        ProjectId other = (ProjectId) obj;
        if(this.projectName == null) {
            return other.projectName == null;
        }
        return other.projectName != null && other.projectName.equals(this.projectName);
    }
}
