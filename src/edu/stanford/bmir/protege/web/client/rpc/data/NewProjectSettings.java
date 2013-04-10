package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public class NewProjectSettings implements Serializable {

    private UserId projectOwner;

    private String projectName;

    private String projectDescription;

    private ProjectType projectType;

    private DocumentId sourceDocumentId = null;


    /**
     * Serialization
     */
    private NewProjectSettings() {
    }

    /**
     * Creates a NewProjectSettings object that describes the basic settings for a new project.
     * @param projectOwner The desired owner of the project.  Not null.
     * @param projectName The desired project name for the new project.  Not null.
     * @param projectDescription The desired project description for the new project.  Not null.
     * @param projectType The type of projectType. Not null.
     * @throws NullPointerException if either projectOwner, projectName or projectDescription are null.
     */
    public NewProjectSettings(UserId projectOwner, String projectName, String projectDescription, ProjectType projectType) {
        if (projectName == null) {
            throw new NullPointerException("projectName must not be null.");
        }
        if (projectDescription == null) {
            throw new NullPointerException("projectDescription must not be null.");
        }
        if (projectType == null) {
            throw new NullPointerException("projectType must not be null");
        }
        this.projectOwner = projectOwner;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectType = projectType;
    }



    /**
     * Creates a NewProjectSettings object that describes the basic settings for a new project and also specifies a
     * set of source documents (via a set of {@link DocumentId} objects) from which to create the project.
     * @param projectOwner The desired owner of the project.  Not null.
     * @param projectName The desired project name for the new project.  Not null.
     * @param projectDescription The desired project description for the new project.  Not null.
     * @param projectType The type of projectType. Not null.
     * @param sourceDocumentId A {@link DocumentId} object that should be used to identify the source document with
     * which to initialise a project.  May be null.
     * @throws NullPointerException if either projectOwner, projectName, projectDescription or sourceDocumentId are
     *                              null.
     */
    public NewProjectSettings(UserId projectOwner, String projectName, String projectDescription, ProjectType projectType, DocumentId sourceDocumentId) {
        this(projectOwner, projectName, projectDescription, projectType);
        this.sourceDocumentId = sourceDocumentId;
    }

    /**
     * Gets the desired owner of the project.
     * @return The {@link UserId} representing the desired ownner of the project.  Not null.
     */
    public UserId getProjectOwner() {
        return projectOwner;
    }

    /**
     * Gets the desired name of the project.
     * @return A string representing the project name.  Not null.
     */
    public String getProjectName() {
        return projectName;
    }


    /**
     * Gets the desired project description.
     * @return A string representing the project description.  Not null.
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * Gets the desired type of the project.
     * @return The project type.
     */
    public ProjectType getProjectType() {
        return projectType;
    }

    /**
     * Determines whether of not this new project settings object has a source document associated with it.
     * @return <code>true</code> if there is a source documents associated with this {@link NewProjectSettings} object,
     * otherwise <code>false</code>.
     */
    public boolean hasSourceDocument() {
        return sourceDocumentId != null;
    }

    /**
     * Gets a set of {@link DocumentId}s that identify source documents that should be used to create a new project.
     * @return A {@link DocumentId} object identifying a source document.  May be null, which indicates there is no
     * source document associated with this project.
     */
    public DocumentId getSourceDocumentId() {
        return sourceDocumentId;
    }


    @Override
    public int hashCode() {
        return projectOwner.hashCode() + projectName.hashCode() * 37 + projectDescription.hashCode() + sourceDocumentId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NewProjectSettings)) {
            return false;
        }
        NewProjectSettings other = (NewProjectSettings) obj;
        return other.projectOwner.equals(projectOwner) &&
                other.projectName.equals(this.projectName) &&
                other.projectDescription.equals(this.projectDescription) &&
                other.sourceDocumentId.equals(sourceDocumentId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NewProjectSettings");
        sb.append("(");
        sb.append(projectOwner);
        sb.append(" ");
        sb.append(" ProjectName(");
        sb.append(projectName);
        sb.append(") ProjectDescription(");
        sb.append(projectDescription);
        sb.append("))");
        return sb.toString();
    }
}
