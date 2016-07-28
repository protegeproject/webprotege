package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import org.protege.owlapi.model.ProtegeOWLOntologyManager;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.functional.renderer.FunctionalSyntaxStorerFactory;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterSyntaxStorerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owlxml.renderer.OWLXMLStorer;
import org.semanticweb.owlapi.owlxml.renderer.OWLXMLStorerFactory;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.RDFXMLStorer;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.RDFXMLStorerFactory;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;
import org.semanticweb.owlapi.util.OWLDocumentFormatFactoryImpl;
import org.semanticweb.owlapi.util.OWLStorerFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyFactoryImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Singleton
public class RootOntologyProvider implements Provider<OWLOntology> {

    private final OWLAPIProjectDocumentStore documentStore;

    private OWLOntology rootOntology = null;

    @Inject
    public RootOntologyProvider(OWLAPIProjectDocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @Override
    public synchronized OWLOntology get() {
        if(rootOntology != null) {
            return rootOntology;
        }

        // The delegate - we use the concurrent ontology manager
        OWLOntologyManager delegateManager = WebProtegeOWLManager.createConcurrentOWLOntologyManager();
        // We only support the binary format for speed
        delegateManager.getOntologyStorers().add(new RDFXMLStorerFactory());
        delegateManager.getOntologyStorers().add(new OWLXMLStorerFactory());
        delegateManager.getOntologyStorers().add(new FunctionalSyntaxStorerFactory());
        delegateManager.getOntologyStorers().add(new ManchesterSyntaxStorerFactory());

        delegateManager.getOntologyParsers().add(new BinaryOWLOntologyDocumentParserFactory());


        // The wrapper manager
        OWLAPIProjectOWLOntologyManager manager = new OWLAPIProjectOWLOntologyManager();
        manager.setDelegate(delegateManager);

        try {
            rootOntology = documentStore.initialiseOntologyManagerWithProject(manager.getDelegate());
            manager.sealDelegate();
            return rootOntology;
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            throw new RuntimeException("Failed to load project: " + e.getMessage(), e);
        }
    }


}
