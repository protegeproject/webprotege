package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.crud.ProjectEntityCrudKitHandlerCache;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.ImportsCacheManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectOWLOntologyManager;
import edu.stanford.bmir.protege.web.server.owlapi.ProjectAccessManager;
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


    private ProjectId projectId;

    private File rootOntologyDocument;

    private OWLDataFactory dataFactory;

    private WebProtegeLogger logger;

    public RootOntologyProvider(ProjectId projectId, File rootOntologyDocument, OWLDataFactory dataFactory, WebProtegeLogger logger) {
        this.projectId = projectId;
        this.rootOntologyDocument = rootOntologyDocument;
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
            OWLOntology rootOntology = loadRootOntologyIntoManager(delegateManager);
//            delegateManager.addOntologyChangeListener(new OWLOntologyChangeListener() {
//                public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
//                    handleOntologiesChanged(changes);
//                }
//            });
            manager.sealDelegate();
            return rootOntology;
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Failed to load project: " + e.getMessage(), e);
        }

    }

    public OWLOntology loadRootOntologyIntoManager(OWLOntologyManager manager) throws OWLOntologyCreationException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        OWLOntologyLoaderListener loaderListener = new OWLOntologyLoaderListener() {
            public void startedLoadingOntology(LoadingStartedEvent event) {
                logger.info(projectId, "Ontology loading started: " + event.getDocumentIRI());
            }

            public void finishedLoadingOntology(LoadingFinishedEvent event) {
                // Give something else a chance - in case we have LOTS of imports
                Thread.yield();
                if (event.isSuccessful()) {
                    logger.info(projectId, "Ontology loading finished: " + event.getDocumentIRI() + " (Loaded: "
                            + event.getOntologyID() + ")");
                } else {
                    logger.info(projectId, "Ontology loading failed: " + event.getDocumentIRI() + " (Reason: " +
                            event.getException().getMessage() + ")");
                }
            }
        };
        manager.addOntologyLoaderListener(loaderListener);
        final MissingImportListener missingImportListener = new MissingImportListener() {
            @Override
            public void importMissing(MissingImportEvent missingImportEvent) {
                logger.info(projectId, "Missing import: " + missingImportEvent.getImportedOntologyURI() + " due " +
                        "to " + missingImportEvent.getCreationException().getMessage());
            }
        };
        manager.addMissingImportListener(missingImportListener);
        manager.addIRIMapper(new OWLOntologyIRIMapper() {
            @Override
            public IRI getDocumentIRI(IRI iri) {
                logger.info(projectId, "Fetching imported ontology from %s.", iri.toQuotedString());
                return iri;
            }
        });
        // Important - add last
        ImportsCacheManager importsCacheManager = new ImportsCacheManager(projectId);
        OWLOntologyIRIMapper iriMapper = importsCacheManager.getIRIMapper();
        manager.addIRIMapper(iriMapper);


        try {
            OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
            config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
            FileDocumentSource documentSource = new FileDocumentSource(rootOntologyDocument);
            logger.info(projectId, "Loading root ontology imports closure.");
            OWLOntology rootOntology = manager.loadOntologyFromOntologyDocument(documentSource, config);
            importsCacheManager.cacheImports(rootOntology);
            return rootOntology;

        } finally {
            stopwatch.stop();
            logger.info(projectId, "Ontology loading completed in " + (stopwatch.elapsed(TimeUnit.MILLISECONDS)) + " ms.");
            manager.removeIRIMapper(iriMapper);
            manager.removeOntologyLoaderListener(loaderListener);
            manager.removeMissingImportListener(missingImportListener);
        }

    }
}
