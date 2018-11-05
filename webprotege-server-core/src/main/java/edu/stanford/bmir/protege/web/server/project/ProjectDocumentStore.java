package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntologyDocument;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final File rootOntologyDocument;

    private final Provider<ImportsCacheManager> importsCacheManagerProvider;

    private ProjectId projectId;


    @Inject
    public ProjectDocumentStore(ProjectId projectId,
                                @RootOntologyDocument File rootOntologyDocument,
                                Provider<ImportsCacheManager> importsCacheManagerProvider) {
        this.projectId = checkNotNull(projectId);
        this.rootOntologyDocument = checkNotNull(rootOntologyDocument);
        this.importsCacheManagerProvider = checkNotNull(importsCacheManagerProvider);
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
                rawChangeList.forEach(change -> changesByOntology.put(change.getOntology(), change));

                for(var ontology : changesByOntology.keySet()) {
                    var docIRI = ontology.getOWLOntologyManager().getOntologyDocumentIRI(ontology);
                    if(!"file".equalsIgnoreCase(docIRI.toURI().getScheme())) {
                        throw new RuntimeException("Document IRI is not a local file IRI");
                    }
                    var changesForOntology = changesByOntology.get(ontology);
                    var changeDataListForOntology = new ArrayList<OWLOntologyChangeData>();
                    for(var changeForOntology : changesForOntology) {
                        var changeRecord = changeForOntology.getChangeRecord();
                        changeDataListForOntology.add(changeRecord.getData());
                    }
                    var file = new File(docIRI.toURI());
                    var serializer = new BinaryOWLOntologyDocumentSerializer();
                    var changeDataList = new OntologyChangeDataList(changeDataListForOntology, System.currentTimeMillis(), BinaryOWLMetadata
                            .emptyMetadata());
                    serializer.appendOntologyChanges(file, changeDataList);
                }

            } catch(IOException e) {
                logger.error("An error occurred whilst saving ontology changes: {}", e.getMessage(), e);
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
            if(rootOntologyDocument.exists()) {
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
        logger.info("{} Loading project", projectId);
        var stopwatch = Stopwatch.createStarted();
        OWLOntologyLoaderListener loaderListener = new OWLOntologyLoaderListener() {
            public void startedLoadingOntology(LoadingStartedEvent event) {
                logger.info("{} Ontology loading started: {}", projectId, event.getDocumentIRI());
            }

            public void finishedLoadingOntology(LoadingFinishedEvent event) {
                if(event.isSuccessful()) {
                    logger.info("{} Ontology loading finished: (Loaded:  {})", projectId, event.getDocumentIRI(), event.getOntologyID());
                    MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
                    memoryMonitor.monitorMemoryUsage();
                }
                else {
                    logger.info("Ontology loading failed: {} (Reason: )", projectId, event.getException().getMessage());
                }
            }
        };
        manager.addOntologyLoaderListener(loaderListener);
        final MissingImportListener missingImportListener = (MissingImportListener) missingImportEvent -> logger.info("{} Missing import: {} due to {}", projectId, missingImportEvent
                .getImportedOntologyURI(), missingImportEvent.getCreationException().getMessage());
        manager.addMissingImportListener(missingImportListener);
        manager.getIRIMappers().add((OWLOntologyIRIMapper) iri -> {
            logger.info("{} Fetching imported ontology from {}.", projectId, iri.toQuotedString());
            return iri;
        });
        // Important - add last
        ImportsCacheManager importsCacheManager = importsCacheManagerProvider.get();
        OWLOntologyIRIMapper iriMapper = importsCacheManager.getIRIMapper();
        manager.getIRIMappers().add(iriMapper);


        try {
            var config = createLoaderConfig();
            logger.info("{} Loading root ontology imports closure.", projectId);
            var projectInputSource = new ProjectInputSource(rootOntologyDocument);
            var rootOntology = manager.loadOntologyFromOntologyDocument(projectInputSource, config);
            importsCacheManager.cacheImports(rootOntology);
            return rootOntology;
        } finally {
            stopwatch.stop();
            logger.info("{} Ontology loading completed in {} ms.", projectId, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            var memoryMonitor = new MemoryMonitor(logger);
            memoryMonitor.monitorMemoryUsage();
            memoryMonitor.logMemoryUsage();
            manager.getIRIMappers().remove(iriMapper);
            manager.removeOntologyLoaderListener(loaderListener);
            manager.removeMissingImportListener(missingImportListener);
        }
    }

    private OWLOntology createFreshProjectOntology(OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException {
        logger.info("Creating a fresh project with an Id of {}", projectId);
        File parentDirectory = rootOntologyDocument.getParentFile();
        parentDirectory.mkdirs();
        IRI ontologyIRI = createUniqueOntologyIRI();
        IRI documentIRI = IRI.create(rootOntologyDocument);
        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
        manager.getIRIMappers().add(mapper);
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        manager.getIRIMappers().remove(mapper);
        writeNewProject(manager, ontology);
        return ontology;
    }

    @Nonnull
    private static OWLOntologyLoaderConfiguration createLoaderConfig() {
        return new OWLOntologyLoaderConfiguration()
                .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT)
                .setReportStackTraces(true)
                // It is safe to turn of illegal punning fixing as we've already parsed
                // (and saved) the ontology using a manager with this turned on.
                .setRepairIllegalPunnings(false);
    }

    private static IRI createUniqueOntologyIRI() {
        String ontologyName = IdUtil.getBase62UUID();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }

    private void writeNewProject(OWLOntologyManager rootOntologyManager,
                                 OWLOntology ontology) throws OWLOntologyStorageException {
        rootOntologyDocument.getParentFile().mkdirs();
        rootOntologyManager.saveOntology(ontology, new BinaryOWLOntologyDocumentFormat(), IRI.create(rootOntologyDocument));

        ImportsCacheManager cacheManager = importsCacheManagerProvider.get();
        cacheManager.cacheImports(ontology);
    }
}
