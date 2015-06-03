package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.crud.ProjectEntityCrudKitHandlerCache;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.ImportsCacheManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectOWLOntologyManager;
import edu.stanford.bmir.protege.web.server.owlapi.ProjectAccessManager;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxOntologyStorer;
import org.coode.owlapi.owlxml.renderer.OWLXMLOntologyStorer;
import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.protege.owlapi.model.ProtegeOWLOntologyManager;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;
import uk.ac.manchester.cs.owl.owlapi.EmptyInMemOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.ParsableOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOntologyStorer;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Singleton
public class RootOntologyProvider implements Provider<OWLOntology> {

    static {
        WebProtegeOWLManager.createOWLOntologyManager();
    }

    private final OWLAPIProjectDocumentStore documentStore;

    private final OWLDataFactory dataFactory;

    private final WebProtegeLogger logger;


    @Inject
    public RootOntologyProvider(OWLAPIProjectDocumentStore documentStore,
                                OWLDataFactory dataFactory, WebProtegeLogger logger) {
        this.documentStore = documentStore;
        this.dataFactory = dataFactory;
        this.logger = logger;
    }

    @Override
    public OWLOntology get() {


        // The delegate - we use the concurrent ontology manager
        OWLOntologyManager delegateManager = new ProtegeOWLOntologyManager(dataFactory);
        // We only support the binary format for speed
        delegateManager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        delegateManager.addOntologyStorer(new RDFXMLOntologyStorer());
        delegateManager.addOntologyStorer(new OWLXMLOntologyStorer());
        delegateManager.addOntologyStorer(new OWLFunctionalSyntaxOntologyStorer());
        delegateManager.addOntologyStorer(new ManchesterOWLSyntaxOntologyStorer());
        // Pass through mapping
        delegateManager.addIRIMapper(new NonMappingOntologyIRIMapper());

        // The wrapper manager
        OWLAPIProjectOWLOntologyManager manager = new OWLAPIProjectOWLOntologyManager();

        // We have to do some twiddling of the factories so that the delegate manager creates ontologies which point
        // to the non-delegate manager.
        EmptyInMemOWLOntologyFactory imMemFactory = new EmptyInMemOWLOntologyFactory();
        delegateManager.addOntologyFactory(imMemFactory);
        imMemFactory.setOWLOntologyManager(manager);


        ParsableOWLOntologyFactory parsingFactory = new ParsableOWLOntologyFactory();
        delegateManager.addOntologyFactory(parsingFactory);
        parsingFactory.setOWLOntologyManager(manager);

        manager.setDelegate(delegateManager);


        try {
            if (!documentStore.exists()) {
                throw new ProjectDocumentNotFoundException(documentStore.getProjectId());
            }
            OWLOntology rootOntology = documentStore.loadRootOntologyIntoManager(manager.getDelegate());
            manager.sealDelegate();
            return rootOntology;
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Failed to load project: " + e.getMessage(), e);
        }



    }
}
