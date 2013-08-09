package edu.stanford.bmir.protege.web.server.owlapi.manager;

import edu.stanford.bmir.protege.web.server.obo.WebProtegeOBOFormatParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/11/2012
 */
public class WebProtegeOWLManager {

    static {
        // Some HORRIBLE HORRIBLE staticness requires this
        OWLManager.getOWLDataFactory();
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new WebProtegeOBOFormatParserFactory());
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
    }

    public static OWLOntologyManager createOWLOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        return manager;
    }

}
