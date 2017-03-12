package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/11/2012
 */
public class WebProtegeOWLManager {

    public static OWLOntologyManager createConcurrentOWLOntologyManager() {
        OWLOntologyManager manager = OWLManager.createConcurrentOWLOntologyManager();
        augmentManager(manager);
        return manager;
    }

    public static OWLOntologyManager createOWLOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        augmentManager(manager);
        return manager;
    }

    private static void augmentManager(OWLOntologyManager manager) {
        manager.getOntologyParsers().add(new BinaryOWLOntologyDocumentParserFactory());
        manager.getOntologyStorers().add(new BinaryOWLStorerFactory());

    }
}
