package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import org.semanticweb.owlapi.functional.renderer.FunctionalSyntaxStorerFactory;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterSyntaxStorerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.owlxml.renderer.OWLXMLStorerFactory;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.RDFXMLStorerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
public class RootOntologyLoader {

    @Nonnull
    private final ProjectOntologyManagerLoader projectOntologyManagerLoader;

    @Inject
    public RootOntologyLoader(@Nonnull ProjectOntologyManagerLoader projectOntologyManagerLoader) {
        this.projectOntologyManagerLoader = projectOntologyManagerLoader;
    }

    public OWLOntology loadRootOntology() {
        // The delegate - we use the concurrent ontology manager
        OWLOntologyManager delegateManager = WebProtegeOWLManager.createConcurrentOWLOntologyManager();
        delegateManager.getOntologyStorers().add(new RDFXMLStorerFactory());
        delegateManager.getOntologyStorers().add(new OWLXMLStorerFactory());
        delegateManager.getOntologyStorers().add(new FunctionalSyntaxStorerFactory());
        delegateManager.getOntologyStorers().add(new ManchesterSyntaxStorerFactory());

        // The wrapper manager
        ProjectOWLOntologyManager manager = new ProjectOWLOntologyManager();
        manager.setDelegate(delegateManager);
        try {
            OWLOntology rootOntology = projectOntologyManagerLoader.createProjectOntologiesInManager(manager.getDelegate());
            manager.sealDelegate();
            return rootOntology;
        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Failed to load project: " + e.getMessage(), e);
        }
    }

}
