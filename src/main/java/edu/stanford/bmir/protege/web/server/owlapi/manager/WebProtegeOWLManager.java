package edu.stanford.bmir.protege.web.server.owlapi.manager;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        OWLParserFactoryRegistry parserFactoryRegistry = OWLParserFactoryRegistry.getInstance();
        List<OWLParserFactory> parserFactoryList = new ArrayList<OWLParserFactory>(parserFactoryRegistry.getParserFactories());
        Collections.reverse(parserFactoryList);
        parserFactoryRegistry.clearParserFactories();
        for(OWLParserFactory parserFactory : parserFactoryList) {
                parserFactoryRegistry.registerParserFactory(parserFactory);
        }
        parserFactoryRegistry.registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
    }

    public static OWLOntologyManager createOWLOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        return manager;
    }

}
