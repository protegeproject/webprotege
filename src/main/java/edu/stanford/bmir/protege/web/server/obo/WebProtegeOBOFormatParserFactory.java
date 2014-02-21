package edu.stanford.bmir.protege.web.server.obo;

import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/06/2013
 */
public class WebProtegeOBOFormatParserFactory implements OWLParserFactory {

    @Override
    public OWLParser createParser(OWLOntologyManager owlOntologyManager) {
        return new WebProtegeOBOFormatParser();
    }
}
