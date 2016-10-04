package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.inject.project.ProjectSingleton;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.functional.renderer.FunctionalSyntaxStorerFactory;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterSyntaxStorerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.owlxml.renderer.OWLXMLStorerFactory;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.RDFXMLStorerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@ProjectSingleton
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
