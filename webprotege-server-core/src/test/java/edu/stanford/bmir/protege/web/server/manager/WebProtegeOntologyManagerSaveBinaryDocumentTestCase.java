package edu.stanford.bmir.protege.web.server.manager;

import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class WebProtegeOntologyManagerSaveBinaryDocumentTestCase {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File ontologyDocumentFile;

    @Before
    public void setUp() throws IOException {
        ontologyDocumentFile = temporaryFolder.newFile();
    }

    /**
     * Ensures that an ontology can be saved in the binary ontology format.  This doesn't test the format serialization,
     * it tests that the {@link WebProtegeOWLManager} is configured
     * with the ability to save an ontology in that format.
     */
    @Test
    public void shouldSaveOntologyToBinaryDocumentFile()
            throws OWLOntologyCreationException, OWLOntologyStorageException{
        OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology();
        BinaryOWLOntologyDocumentFormat format = new BinaryOWLOntologyDocumentFormat();
        manager.saveOntology(ontology, format, IRI.create(ontologyDocumentFile));
    }

}
