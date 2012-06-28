package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protegex.chao.ChAOKbManager;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2012
 * <p>
 * This class tries to encapsulate information about project files and project sources as best it can.  I'm not entirely
 * sure what the sources property list in a protege project points to.  The list is just full of strings and it's not
 * clear whether these are path names, escaped path names, absolute path names, URIs (relative or otherwise!).  I've
 * assumed absolute URIs for now.
 */
public abstract class LocalProjectLocationDescriptor {

    /**
     * The place where projects are stored.  Each project is stored within its own subdirectory in this directory.
     */
    public static final String PROJECTS_DIRECTORY_NAME = "projects";

    /**
     * The project file extension
     */
    public static final String PPRJ_EXTENSION = ".pprj";

    public static final String ANNOTATION_PROJECT_SUFFIX = "-annotation";

    private String projectName;

    private String projectFileName;
    
    private File projectFile;
    
    private File annotationProjectFile;
 
    
    public LocalProjectLocationDescriptor(String projectName) {
        this.projectName = projectName;
        this.projectFileName = escapeProjectName(projectName);
        File projectDirectory = getProjectDirectory();
        projectFile = new File(projectDirectory, projectFileName + PPRJ_EXTENSION);
        annotationProjectFile = new File(projectDirectory, projectFileName + ANNOTATION_PROJECT_SUFFIX + PPRJ_EXTENSION);
    }

    /**
     * Get project name
     * @return The name of the project.
     */
    public String getProjectName() {
        return projectName;
    }
    
    public String getAnnotationProjectName() {
        return projectName + ANNOTATION_PROJECT_SUFFIX;
    }

    /**
     * Protege 3 doesn't seem to be able to handle URIs with escaped spaces in them :(  Just replace with hyphens.
     * @param projectName The project name to be escaped.
     * @return The project name with spaces replaced with hyphens.
     */
    private String escapeProjectName(String projectName) {
        return projectName.replace(" ", "-");
    }

    /**
     * Gets the directory which contains project directories.
     * @return A file representing the projects directory.  This file has an absolute path in the local file system.
     */
    public File getProjectsDirectory() {
        File webProtegeDirectory = new File(ApplicationProperties.getWeprotegeDirectory());
        return new File(webProtegeDirectory, PROJECTS_DIRECTORY_NAME);
    }

    
    /**
     * Gets the name of the project.
     * @return A string representing the name of the project.
     */
    public String getEscapedProjectName() {
        return projectFileName;
    }
    
    /**
     * Gets the project directory that contains the project (pprj) file and project source file (e.g. OWL file, pont
     * and pins files etc.)
     * @return A file representing the project directory. This file has an absolute path in the local file system.
     */
    public File getProjectDirectory() {
        return new File(getProjectsDirectory(), projectFileName);
    }

    /**
     * Gets the project file i.e. the pprj file.
     * @return A file representing the pprj file.  This file has an absolute path in the local file system.
     */
    public File getProjectFile() {
        return projectFile;
    }

    /**
     * Gets the file that stores the annotation project file
     * @return The file.
     */
    public File getAnnotationProjectFile() {
        return annotationProjectFile;
    }

    protected void setupChangeTrackingProject(Project project) {
        KnowledgeBase changesKB = ChAOKbManager.createRDFFileChAOKb(project.getKnowledgeBase(), annotationProjectFile.toURI());
        if (changesKB != null) {
            Project changesProject = changesKB.getProject();
            changesProject.save(new ArrayList());
        }
    }
    

    /**
     * Creates an initialises an empty project.
     * @return The Project that was created.  The project files will exist on disk at the location pointed to by the
     * project URI.
     * @throws OntologyLoadException If there was a problem creating the ontology.
     */
    public abstract Project initialiseEmptyProject() throws OntologyLoadException;

    /**
     * Creates a project and initialises it with the specified source files.
     * @param sourceFiles The source files that the project should be initialised with.
     * @return The Project that was created.  The project files will exist on disk at the location pointed to by the
     * project URI.
     * @throws OntologyLoadException If there was a problem creating the ontology.
     */
    public abstract Project initialiseProjectWithSources(File ... sourceFiles) throws OntologyLoadException;
}
