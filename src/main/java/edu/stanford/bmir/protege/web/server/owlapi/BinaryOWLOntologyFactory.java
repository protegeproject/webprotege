package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParser;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/06/15
 */
public class BinaryOWLOntologyFactory implements OWLOntologyFactory {

    private OWLOntologyManager manager;

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
        OWLOntologyImpl ontology = new OWLOntologyImpl(manager, ontologyID);
//        manager.setOntologyFormat(ontology, new BinaryOWLOntologyDocumentFormat());
        handler.ontologyCreated(ontology);
        return ontology;
    }

    @Override
    public OWLOntology loadOWLOntology(OWLOntologyDocumentSource documentSource, OWLOntologyCreationHandler handler) throws OWLOntologyCreationException {
        return this.loadOWLOntology(documentSource, handler, new OWLOntologyLoaderConfiguration());
    }

    @Override
    public OWLOntology loadOWLOntology(OWLOntologyDocumentSource documentSource, OWLOntologyCreationHandler handler, OWLOntologyLoaderConfiguration configuration) throws OWLOntologyCreationException {
        try {
            BinaryOWLOntologyDocumentParser parser = new BinaryOWLOntologyDocumentParser();
            OWLOntology ontology = new OWLOntologyImpl(manager, new OWLOntologyID(documentSource.getDocumentIRI()));
            OWLOntologyFormat format = parser.parse(documentSource, ontology, configuration);
//            manager.setOntologyFormat(ontology, format);
            handler.ontologyCreated(ontology);
            return ontology;
        } catch (IOException e) {
            throw new OWLOntologyCreationException(e);
        }
    }

    @Override
    public boolean canCreateFromDocumentIRI(IRI documentIRI) {
        return true;
    }

    @Override
    public boolean canLoad(OWLOntologyDocumentSource documentSource) {
        return true;
    }
}
