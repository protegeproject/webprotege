package edu.stanford.bmir.protege.web.server;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2012
 *
 */
public class LocalOWLProjectLocationDescriptor extends LocalProjectLocationDescriptor {

    public static final String OWL_FILE_EXTENSION = ".owl";
    
    private File owlFile;
    
    public LocalOWLProjectLocationDescriptor(String projectName) {
        super(projectName);
        owlFile = new File(getProjectDirectory(), getEscapedProjectName() + OWL_FILE_EXTENSION);
    }
    
    public File getOWLFile() {
        return owlFile;
    }

    /**
     * Sets up the Protege <code>Project</code> with the correct location information.  This consists of a URI which
     * points to a project file, and URIs which point to various source files.
     * @param project The project to be set up.
     */
//    @Override
    public void setupProjectWithSourceFileLocationInformation(Project project) {
        File projectFile = getProjectFile();
        projectFile.getParentFile().mkdirs();
        project.setProjectURI(projectFile.toURI());
        JenaKnowledgeBaseFactory.setOWLFileName(project.getSources(), getOWLFile().toURI().toString());
    }

    @Override
    public Project initialiseEmptyProject() throws OntologyLoadException {
        JenaOWLModel model = ProtegeOWL.createJenaOWLModel();
        Project project = model.getProject();
        setupProjectWithSourceFileLocationInformation(project);
        saveProject(project);
        setupChangeTrackingProject(project);
        return project;
    }

    @Override
    public Project initialiseProjectWithSources(File... sourceFiles) throws OntologyLoadException {
        if(sourceFiles.length != 1) {
            throw new RuntimeException("Expected exactly one source file.");
        }
        String owlFileURIString = sourceFiles[0].toURI().toString();
        JenaOWLModel model = ProtegeOWL.createJenaOWLModelFromURI(owlFileURIString);
        Project project = model.getProject();
        setupProjectWithSourceFileLocationInformation(project);
        saveProject(project);
        setupChangeTrackingProject(project);
        return project;
    }

    private void saveProject(Project project) throws OntologyLoadException {
        ArrayList errors = new ArrayList();
        project.save(errors);
        if(!errors.isEmpty()) {
            // Something went wrong - now we have to find out what and try to pass on the cause - grrrr what a mess!
            for(Object o : errors) {
                if(o instanceof Throwable) {
                    throw new OntologyLoadException((Throwable) o);
                }
            }
            throw new OntologyLoadException(null, errors.get(0).toString());
        }
    }
}

