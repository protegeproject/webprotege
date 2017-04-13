package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntologyDocument;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.apache.commons.io.FileUtils;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

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

    private final WebProtegeLogger logger;

    private ProjectId projectId;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final File rootOntologyDocument;

    private final Provider<ImportsCacheManager> importsCacheManagerProvider;


    @Inject
    public ProjectDocumentStore(ProjectId projectId,
                                @RootOntologyDocument File rootOntologyDocument,
                                WebProtegeLogger logger,
                                Provider<ImportsCacheManager> importsCacheManagerProvider) {
        this.logger = logger;
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
                    if (docIRI == null) {
                        // Generate new ontology
                        throw new RuntimeException("No ontology document specified for ontology");
                    }
                    if (!"file".equalsIgnoreCase(docIRI.toURI().getScheme())) {
                        throw new RuntimeException("Document IRI is not a local file IRI");
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
                logger.error(e);
                // NOW WHAT?!?!?!?!?!?!?!?!?!?
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }
        // Need to delete cache files
        deleteCacheFiles();
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
        logger.info("Loading %s", projectId);
        long t0 = System.currentTimeMillis();
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
                }
                else {
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
        ImportsCacheManager importsCacheManager = importsCacheManagerProvider.get();
        OWLOntologyIRIMapper iriMapper = importsCacheManager.getIRIMapper();
        manager.addIRIMapper(iriMapper);


        try {
            OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
            config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
            config = config.setReportStackTraces(true);
            FileDocumentSource documentSource = new FileDocumentSource(rootOntologyDocument);
            logger.info(projectId, "Loading root ontology imports closure.");
            OWLOntology rootOntology = manager.loadOntologyFromOntologyDocument(documentSource, config);
            importsCacheManager.cacheImports(rootOntology);
            return rootOntology;

        } finally {
            long t1 = System.currentTimeMillis();
            logger.info(projectId, "Ontology loading completed in " + (t1 - t0) + " ms.");
            manager.removeIRIMapper(iriMapper);
            manager.removeOntologyLoaderListener(loaderListener);
            manager.removeMissingImportListener(missingImportListener);
        }
    }

    private OWLOntology createFreshProjectOntology(OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException {
        logger.info("Creating fresh project: %s", projectId);
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

    public void deleteCacheFiles() {
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    downloadCacheWriteLock.lock();
//                    File cachedFilesDirectory = projectFileStore.getDownloadCacheDirectory();
//                    if (cachedFilesDirectory.exists()) {
//                        final File[] files = cachedFilesDirectory.listFiles();
//                        if (files != null) {
//                            for (File cachedFile : files) {
//                                if (!cachedFile.isHidden()) {
//                                    cachedFile.delete();
//                                }
//                            }
//                        }
//                    }
//                } finally {
//                    downloadCacheWriteLock.unlock();
//                }
//            }
//        });
//        t.setPriority(Thread.MIN_PRIORITY);
//        t.start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//    private void createDownloadCacheIfNecessary(String projectDisplayName, DownloadFormat format) throws IOException, OWLOntologyStorageException {
//        ReadWriteLock projectDownloadCacheLock = getProjectDownloadCacheLock(projectId);
//        try {
//            projectDownloadCacheLock.writeLock().lock();
//            File downloadCacheDirectory = projectFileStore.getDownloadCacheDirectory();
//            File cachedFile = getDownloadCacheFile(format);
//            if (!cachedFile.exists()) {
//                downloadCacheDirectory.mkdirs();
//                // Create
//                OWLAPIProject project = projectManager.getProject(projectId);
//                RevisionManager changeManager = project.getChangeManager();
//                RevisionNumber currentRevisionNumber = changeManager.getCurrentRevision();
//
//                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(cachedFile));
//                exportProjectRevision(projectDisplayName, currentRevisionNumber, outputStream, format);
//                outputStream.close();
//            }
//        } finally {
//            projectDownloadCacheLock.writeLock().unlock();
//        }
//    }



//    private File getDownloadCacheFile(DownloadFormat format) {
//        File downloadCacheDirectory = projectFileStore.getDownloadCacheDirectory();
//        return new File(downloadCacheDirectory, "download." + format.getExtension() + ".zip");
//    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void deleteSourceFile(File sourceFile) {
        FileUtils.deleteQuietly(sourceFile);
    }

    private void writeNewProject(
            OWLOntologyManager rootOntologyManager,
            OWLOntology ontology) throws
            OWLOntologyStorageException {
//        File binaryDocumentFile = getBinaryOntologyDocumentFile();
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
