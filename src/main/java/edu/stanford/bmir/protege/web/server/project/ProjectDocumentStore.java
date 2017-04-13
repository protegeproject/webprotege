package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntologyDocument;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerEx;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 * <p>
 * Essentially manages the location and layout of a project on disk.  There is no commitment to how a project is
 * stored.
 * </p>
 */
@ProjectSingleton
public class ProjectDocumentStore {

    public static final String GENERATED_ONTOLOGY_IRI_PREFIX = "http://webprotege.stanford.edu/project/";

    private static final Logger logger = LoggerFactory.getLogger(ProjectDocumentStore.class);

    private ProjectId projectId;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final File rootOntologyDocument;

    private final Provider<ImportsCacheManager> importsCacheManagerProvider;


    @Inject
    public ProjectDocumentStore(ProjectId projectId,
                                @RootOntologyDocument File rootOntologyDocument,
                                Provider<ImportsCacheManager> importsCacheManagerProvider) {
        this.projectId = projectId;
        this.rootOntologyDocument = rootOntologyDocument;
        this.importsCacheManagerProvider = importsCacheManagerProvider;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void saveOntologyChanges(List<OWLOntologyChange> rawChangeList) {
        // Put changes into a buffer
        try {
            writeLock.lock();
            try {
                ListMultimap<OWLOntology, OWLOntologyChange> changesByOntology = ArrayListMultimap.create();
                for (OWLOntologyChange change : rawChangeList) {
                    changesByOntology.put(change.getOntology(), change);
                }
                for (OWLOntology ontology : changesByOntology.keySet()) {
                    IRI docIRI = ontology.getOWLOntologyManager().getOntologyDocumentIRI(ontology);
                    if (!"file".equalsIgnoreCase(docIRI.toURI().getScheme())) {
                        throw new RuntimeException("Document IRI is not a local file IRI" );
                    }
                    List<OWLOntologyChange> ontologyChangeList = changesByOntology.get(ontology);
                    List<OWLOntologyChangeData> infoList = new ArrayList<OWLOntologyChangeData>();
                    for (OWLOntologyChange change : ontologyChangeList) {
                        OWLOntologyChangeRecord changeRecord = change.getChangeRecord();
                        infoList.add(changeRecord.getData());
                    }
                    File file = new File(docIRI.toURI());
                    BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                    serializer.appendOntologyChanges(file, new OntologyChangeDataList(infoList,
                                                                                      System.currentTimeMillis(),
                                                                                      BinaryOWLMetadata.emptyMetadata
                                                                                              ()));
                }

            } catch (IOException e) {
                logger.error("An error occurred whilst saving ontology changes: {}", e.getMessage(), e);
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLOntology initialiseOntologyManagerWithProject(OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException {
        try {
            writeLock.lock();
            if (rootOntologyDocument.exists()) {
                return loadProjectOntologiesIntoManager(manager);
            }
            else {
                return createFreshProjectOntology(manager);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private OWLOntology loadProjectOntologiesIntoManager(OWLOntologyManager manager) throws OWLOntologyCreationException {
        logger.info("{} Loading project" , projectId);
        long t0 = System.currentTimeMillis();
        OWLOntologyLoaderListener loaderListener = new OWLOntologyLoaderListener() {
            public void startedLoadingOntology(LoadingStartedEvent event) {
                logger.info("{} Ontology loading started: {}",
                            projectId,
                            event.getDocumentIRI());
            }

            public void finishedLoadingOntology(LoadingFinishedEvent event) {
                // Give something else a chance - in case we have LOTS of imports
                Thread.yield();
                if (event.isSuccessful()) {
                    logger.info("{} Ontology loading finished: (Loaded:  {})",
                                projectId, event.getDocumentIRI(),
                                event.getOntologyID());
                }
                else {
                    logger.info("Ontology loading failed: {} (Reason: )", projectId, event.getException().getMessage());
                }
            }
        };
        manager.addOntologyLoaderListener(loaderListener);
        final MissingImportListener missingImportListener = (MissingImportListener) missingImportEvent ->
                logger.info("{} Missing import: {} due to {}", projectId,
                            missingImportEvent.getImportedOntologyURI(),
                            missingImportEvent.getCreationException().getMessage());
        manager.addMissingImportListener(missingImportListener);
        manager.addIRIMapper((OWLOntologyIRIMapper) iri -> {
            logger.info("{} Fetching imported ontology from {}.", projectId, iri.toQuotedString());
            return iri;
        });
        // Important - add last
        ImportsCacheManager importsCacheManager = importsCacheManagerProvider.get();
        OWLOntologyIRIMapper iriMapper = importsCacheManager.getIRIMapper();
        manager.addIRIMapper(iriMapper);


        try {
            OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
            config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
            config = config.setReportStackTraces(true);
            FileDocumentSource documentSource = new FileDocumentSource(rootOntologyDocument);
            logger.info("{} Loading root ontology imports closure.", projectId);
            OWLOntology rootOntology = manager.loadOntologyFromOntologyDocument(documentSource, config);
            importsCacheManager.cacheImports(rootOntology);
            return rootOntology;

        } finally {
            long t1 = System.currentTimeMillis();
            logger.info("{} Ontology loading completed in ms.", projectId, (t1 - t0) );
            MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
            memoryMonitor.monitorMemoryUsage();
            memoryMonitor.logMemoryUsage();
            manager.removeIRIMapper(iriMapper);
            manager.removeOntologyLoaderListener(loaderListener);
            manager.removeMissingImportListener(missingImportListener);
        }
    }

    private OWLOntology createFreshProjectOntology(OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException {
        logger.info("Creating fresh project: %s" , projectId);
        File parentDirectory = rootOntologyDocument.getParentFile();
        parentDirectory.mkdirs();
        IRI ontologyIRI = createUniqueOntologyIRI();
        IRI documentIRI = IRI.create(rootOntologyDocument);
        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
        manager.addIRIMapper(mapper);
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        manager.removeIRIMapper(mapper);
        writeNewProject(manager, ontology);
        return ontology;
    }

    private void writeNewProject(
            OWLOntologyManager rootOntologyManager,
            OWLOntology ontology) throws
            OWLOntologyStorageException {
        rootOntologyDocument.getParentFile().mkdirs();
        rootOntologyManager.saveOntology(ontology, new BinaryOWLOntologyDocumentFormat(),
                                         IRI.create(rootOntologyDocument));

        ImportsCacheManager cacheManager = importsCacheManagerProvider.get();
        cacheManager.cacheImports(ontology);
    }

    private static IRI createUniqueOntologyIRI() {
        String ontologyName = IdUtil.getBase62UUID();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }
}
