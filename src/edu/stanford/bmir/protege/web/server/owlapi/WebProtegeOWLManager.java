package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/11/2012
 */
public class WebProtegeOWLManager {

    public static OWLOntologyManager createOWLOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        return manager;
    }
}
