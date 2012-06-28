package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/05/2012
 */
public class ExplodedOWLAPIProjectSource {

    public static final String PROJECT_DIRECTORY_NAME = "project";

    public static final String ONTOLOGIES_DIRECTORY_NAME = "ontologies";

    public static final String IMPORTS_DIRECTORY_NAME = "imports";

    public static final String ROOT_ONTOLOGY_FILE_NAME = "root-ontology.owl";
    
    private File projectDirectory;

    /**
     * Constructs an ExplodedOWLAPIProjectSource over the specified project directory.  This project directory must
     * have a name equal to (case sensitive) the value specified by the {@link #PROJECT_DIRECTORY_NAME} constant.
     * @param projectDirectory The project directory
     * @throws RuntimeException if the name of the projectDirectory param is not equal to the value of the
     * {@link #PROJECT_DIRECTORY_NAME} constant.
     */
    public ExplodedOWLAPIProjectSource(File projectDirectory) {
        if(!projectDirectory.getName().equals(PROJECT_DIRECTORY_NAME)) {
            throw new RuntimeException(projectDirectory + " must be named " + PROJECT_DIRECTORY_NAME);
        }
        this.projectDirectory = projectDirectory;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }
    
    
    public File getOntologiesDirectory() {
        return new File(projectDirectory, ONTOLOGIES_DIRECTORY_NAME);   
    }
    
    public File getImportsDirectory() {
        return new File(getOntologiesDirectory(), IMPORTS_DIRECTORY_NAME);
    }
    
    public File getRootOntologyFile() {
        File ontologiesDirectory = getOntologiesDirectory();
        return new File(ontologiesDirectory, ROOT_ONTOLOGY_FILE_NAME);
    }

    public OWLOntologyIRIMapper getIRIMapper() {
        return new AutoIRIMapper(getOntologiesDirectory(), true);
    }

    public OWLOntology loadRootOntologyImportsClosure(OWLOntologyManager manager) throws IOException, OWLOntologyCreationException {
        File rootOntologyFile = getRootOntologyFile();
        if(!rootOntologyFile.exists()) {
            throw new FileNotFoundException("Root ontology document file does not exist: " + rootOntologyFile.getAbsolutePath());
        }
        OWLOntologyIRIMapper mapper = getIRIMapper();
        try {
            manager.addIRIMapper(mapper);
            return manager.loadOntologyFromOntologyDocument(rootOntologyFile);
        }
        finally {
            manager.removeIRIMapper(mapper);
        }
    }
    
    
    public boolean isWellFormed() {
        return getProjectDirectory().exists() && getOntologiesDirectory().exists() && getRootOntologyFile().exists();
    }


}
