package edu.stanford.bmir.protege.web.server.owlapi.manager;

import edu.stanford.bmir.protege.web.server.obo.WebProtegeOBOFormatParserFactory;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxParserFactory;
import org.coode.owlapi.owlxmlparser.OWLXMLParserFactory;
import org.coode.owlapi.rdfxml.parser.RDFXMLParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.TurtleOntologyParserFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/06/2013
 */
public class WebProtegeOntologyFactory implements OWLOntologyFactory {

    private List<OWLParserFactory> parserFactories = new ArrayList<OWLParserFactory>();

    private OWLOntologyManager manager;

    public WebProtegeOntologyFactory() {
        parserFactories.add(new BinaryOWLOntologyDocumentParserFactory());
        parserFactories.add(new RDFXMLParserFactory());
        parserFactories.add(new OWLXMLParserFactory());
        parserFactories.add(new TurtleOntologyParserFactory());
        parserFactories.add(new WebProtegeOBOFormatParserFactory());
        parserFactories.add(new ManchesterOWLSyntaxParserFactory());

    }

    @Override
    public void setOWLOntologyManager(OWLOntologyManager owlOntologyManager) {
        this.manager = owlOntologyManager;
    }

    @Override
    public OWLOntologyManager getOWLOntologyManager() {
        return manager;
    }

    @Override
    public OWLOntology createOWLOntology(OWLOntologyID ontologyID, IRI documentIRI, OWLOntologyCreationHandler handler) throws OWLOntologyCreationException {
        final OWLOntologyImpl owlOntology = new OWLOntologyImpl(manager, ontologyID);
        handler.ontologyCreated(owlOntology);
        return owlOntology;
    }

    @Override
    public OWLOntology loadOWLOntology(OWLOntologyDocumentSource documentSource, OWLOntologyCreationHandler handler) throws OWLOntologyCreationException {
        return loadOWLOntology(documentSource, handler, new OWLOntologyLoaderConfiguration());
    }

    @Override
    public OWLOntology loadOWLOntology(OWLOntologyDocumentSource documentSource, OWLOntologyCreationHandler handler, OWLOntologyLoaderConfiguration configuration) throws OWLOntologyCreationException {
        OWLOntology ontology = new OWLOntologyImpl(manager, new OWLOntologyID());
        for(OWLParserFactory parserFactory : parserFactories) {
            OWLParser parser = parserFactory.createParser(manager);
//            OWLOntologyFormat format = parser.parse(documentSource, ontology, new OWLOntologyLoaderConfiguration());
        }

        return ontology;
    }

    @Override
    public boolean canCreateFromDocumentIRI(IRI documentIRI) {
        return false;
    }

    @Override
    public boolean canLoad(OWLOntologyDocumentSource documentSource) {
        return false;
    }
}
