package edu.stanford.bmir.protege.web.client.ui.ontology.home;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 */
public class NewProjectInfo {

    private String projectName;
    
    private String projectDescription;

    /**
     * Constructs a {@link NewProjectInfo} that holds a project name and a project description.
     * @param projectName The project name.  Not null.
     * @param projectDescription The project description.  Not null.
     * @throws NullPointerException if projectName, projectDescription are null.
     */
    public NewProjectInfo(String projectName, String projectDescription) {
        if(projectName == null) {
            throw new NullPointerException("projectName must not be null");
        }
        if(projectDescription == null) {
            throw new NullPointerException("projectDescription must not be null");
        }
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    /**
     * Gets the project name
     * @return A string representing the project name.  Not null.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the project description.
     * @return A string representing the project description.  Not null.
     */
    public String getProjectDescription() {
        return projectDescription;
    }

}
