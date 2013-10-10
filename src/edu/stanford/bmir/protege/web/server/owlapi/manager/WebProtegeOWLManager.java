package edu.stanford.bmir.protege.web.server.owlapi.manager;

import edu.stanford.bmir.protege.web.server.obo.WebProtegeOBOFormatParserFactory;
import org.coode.owlapi.obo.parser.OBOParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.ArrayList;
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

        // Set up parser factories.  We want to replace the OBO one with the reference OBO parser.  This OBO parser
        // must be chosen last (i.e. be added first here) - see https://github.com/protegeproject/webprotege/issues/62
        OWLParserFactoryRegistry parserFactoryRegistry = OWLParserFactoryRegistry.getInstance();
        List<OWLParserFactory> parserFactoryList = new ArrayList<OWLParserFactory>(parserFactoryRegistry.getParserFactories());
        parserFactoryRegistry.clearParserFactories();
        parserFactoryRegistry.registerParserFactory(new WebProtegeOBOFormatParserFactory());
        for(OWLParserFactory parserFactory : parserFactoryList) {
            if (!(parserFactory instanceof OBOParserFactory)) {
                // Replace the OBO one with the one above
                parserFactoryRegistry.registerParserFactory(parserFactory);
            }
        }
        parserFactoryRegistry.registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
    }

    public static OWLOntologyManager createOWLOntologyManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        return manager;
    }

}
