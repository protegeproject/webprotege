package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.util.DefaultTempFileFactory;
import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.IdUtil;
import edu.stanford.bmir.protege.web.server.ProjectIdFactory;
import edu.stanford.bmir.protege.web.server.filedownload.DownloadFormat;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadConstants;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentExistsException;
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

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
public class OWLAPIProjectDocumentStore {


    public static final String GENERATED_ONTOLOGY_IRI_PREFIX = "http://webprotege.stanford.edu/project/";

    private static final String PROJECT_ATTRIBUTES_FILE_NAME = "project-attributes.binary";

    private static final WebProtegeLogger logger = WebProtegeLoggerManager.get(OWLAPIProjectDocumentStore.class);

    private ProjectId projectId;

    private OWLAPIProjectFileStore projectFileStore;

    private static final String ROOT_ONTOLOGY_DOCUMENT_NAME = "root-ontology.binary";

    private static final String CHANGE_DATA_FILE_NAME = "change-data.binary";


    private static Map<ProjectId, ReadWriteLock> projectLockMap = new WeakHashMap<ProjectId, ReadWriteLock>();

    private static Map<ProjectId, ReadWriteLock> projectDownloadCacheLock = new WeakHashMap<ProjectId, ReadWriteLock>();

    private static Map<ProjectId, ReadWriteLock> projectAttributesCacheLock = new WeakHashMap<ProjectId, ReadWriteLock>();


    private static ReadWriteLock getProjectReadWriteLock(ProjectId projectId) {
        // Synchronized on the class because it should be global over all instances of document store for the
        // specified project.
        synchronized (OWLAPIProjectDocumentStore.class) {
            ReadWriteLock lock = projectLockMap.get(projectId);
            if (lock == null) {
                lock = new ReentrantReadWriteLock();
                projectLockMap.put(projectId, lock);
            }
            return lock;
        }
    }


    private static ReadWriteLock getProjectDownloadCacheLock(ProjectId projectId) {
        synchronized (OWLAPIProjectDocumentStore.class) {
            ReadWriteLock lock = projectDownloadCacheLock.get(projectId);
            if (lock == null) {
                lock = new ReentrantReadWriteLock();
                projectDownloadCacheLock.put(projectId, lock);
            }
            return lock;
        }
    }

    private static ReadWriteLock getProjectAttributesCacheLock(ProjectId projectId) {
        synchronized (OWLAPIProjectDocumentStore.class) {
            ReadWriteLock lock = projectAttributesCacheLock.get(projectId);
            if (lock == null) {
                lock = new ReentrantReadWriteLock();
                projectAttributesCacheLock.put(projectId, lock);
            }
            return lock;
        }
    }




    private OWLAPIProjectDocumentStore(ProjectId projectId) {
        this.projectId = projectId;
        this.projectFileStore = OWLAPIProjectFileStore.getProjectFileStore(projectId);
    }

    private OWLAPIProjectDocumentStore(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, IOException {
        this.projectId = ProjectIdFactory.getFreshProjectId();
        this.projectFileStore = OWLAPIProjectFileStore.getProjectFileStore(projectId);
        if (projectFileStore.getProjectDirectory().exists()) {
            throw new ProjectDocumentExistsException(projectId);
        }
        this.projectFileStore.initDirectories();
        if (newProjectSettings.hasSourceDocument()) {
            createProjectFromSources(newProjectSettings);
        }
        else {
            createEmptyProject(newProjectSettings);
        }
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static OWLAPIProjectDocumentStore getProjectDocumentStore(ProjectId projectId) {
        return new OWLAPIProjectDocumentStore(projectId);
    }


    public static OWLAPIProjectDocumentStore createNewProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, IOException {
        return new OWLAPIProjectDocumentStore(newProjectSettings);
    }



    public void exportProject(OutputStream outputStream, DownloadFormat format) throws IOException, OWLOntologyStorageException {
        // Does it already exist in the download cache?
        createDownloadCacheIfNecessary(format);
        // Feed cached file to caller
        final ReadWriteLock projectDownloadCacheLock = getProjectDownloadCacheLock(projectId);
        try {
            projectDownloadCacheLock.readLock().lock();
            byte[] buffer = new byte[4096];
            File downloadCache = getDownloadCacheFile(format);
            InputStream is = new BufferedInputStream(new FileInputStream(downloadCache));
            int read;
            while ((read = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            is.close();
            outputStream.flush();
        }
        finally {
            projectDownloadCacheLock.readLock().unlock();
        }
    }

    public void exportProjectRevision(RevisionNumber revisionNumber, OutputStream outputStream, DownloadFormat format) throws IOException, OWLOntologyStorageException {
        checkNotNull(revisionNumber);
        checkNotNull(outputStream);
        checkNotNull(format);

        OWLOntologyManager manager = getOntologyManagerForRevision(revisionNumber);
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
        OWLOntologyID rootOntologyId = project.getRootOntology().getOntologyID();
        OWLOntology revisionRootOntology = manager.getOntology(rootOntologyId);
        applyRevisionMetadataAnnotationsToOntology(revisionNumber, revisionRootOntology);
        saveImportsClosureToStream(revisionRootOntology, format, outputStream, revisionNumber);
    }



    public void saveOntologyChanges(List<OWLOntologyChange> changeList) {
        // Put changes into a buffer
        try {
            getProjectReadWriteLock(projectId).writeLock().lock();
            try {
                File file = getBinaryOntologyDocumentFile();
                BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                List<OWLOntologyChangeData> infoList = new ArrayList<OWLOntologyChangeData>();
                for (OWLOntologyChange change : changeList) {
                    OWLOntologyChangeRecord changeRecord = change.getChangeRecord();
                    infoList.add(changeRecord.getData());
                }
                serializer.appendOntologyChanges(file, new OntologyChangeDataList(infoList, System.currentTimeMillis(), BinaryOWLMetadata.emptyMetadata()));
            }
            catch (IOException e) {
                logger.severe(e);
                // NOW WHAT?!?!?!?!?!?!?!?!?!?
                e.printStackTrace();
            }
        }
        finally {
            getProjectReadWriteLock(projectId).writeLock().unlock();
        }
        // Need to delete cache files
        deleteCacheFiles();
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLOntology loadRootOntologyIntoManager(OWLOntologyManager manager) throws OWLOntologyCreationException {
        try {

            getProjectReadWriteLock(projectId).writeLock().lock();

            long t0 = System.currentTimeMillis();
            OWLOntologyLoaderListener loaderListener = new OWLOntologyLoaderListener() {
                public void startedLoadingOntology(LoadingStartedEvent event) {
                    logger.info(projectId, "Ontology loading started: " + event.getDocumentIRI());
                }

                public void finishedLoadingOntology(LoadingFinishedEvent event) {
                    // Give something else a chance - in case we have LOTS of imports
                    Thread.yield();
                    if (event.isSuccessful()) {
                        logger.info(projectId, "Ontology loading finished: " + event.getDocumentIRI() + " (Loaded: " + event.getOntologyID() + ")");
                    }
                    else {
                        logger.info(projectId, "Ontology loading failed: " + event.getDocumentIRI() + " (Reason: " + event.getException().getMessage() + ")");
                    }
                }
            };
            manager.addOntologyLoaderListener(loaderListener);
            final MissingImportListener missingImportListener = new MissingImportListener() {
                @Override
                public void importMissing(MissingImportEvent missingImportEvent) {
                    logger.info(projectId, "Missing import: " + missingImportEvent.getImportedOntologyURI() + " due to " + missingImportEvent.getCreationException().getMessage());
                }
            };
            manager.addMissingImportListener(missingImportListener);
            File ontologyDataDirectory = projectFileStore.getOntologyDataDirectory();
            File rootOntologyDocument = new File(ontologyDataDirectory, ROOT_ONTOLOGY_DOCUMENT_NAME);
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

            }
            finally {
                long t1 = System.currentTimeMillis();
                logger.info(projectId, "Ontology loading completed in " + (t1 - t0) + " ms.");
                manager.removeIRIMapper(iriMapper);
                manager.removeOntologyLoaderListener(loaderListener);
                manager.removeMissingImportListener(missingImportListener);
            }
        }
        finally {
            getProjectReadWriteLock(projectId).writeLock().unlock();
        }



    }

    public File getNotesDataDirectory() {
        return projectFileStore.getNotesDataDirectory();
    }

    public File getChangeDataFile() {
        return new File(projectFileStore.getChangesDataDirectory(), CHANGE_DATA_FILE_NAME);
    }

    public File getConfigurationsDirectory() {
        return projectFileStore.getConfigurationsDirectory();
    }

    public boolean exists() {
        return getBinaryOntologyDocumentFile().exists();
    }

    public void deleteCacheFiles() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    getProjectDownloadCacheLock(projectId).writeLock().lock();
                    File cachedFilesDirectory = projectFileStore.getDownloadCacheDirectory();
                    if (cachedFilesDirectory.exists()) {
                        final File[] files = cachedFilesDirectory.listFiles();
                        if (files != null) {
                            for (File cachedFile : files) {
                                if (!cachedFile.isHidden()) {
                                    cachedFile.delete();
                                }
                            }
                        }
                    }
                }
                finally {
                    getProjectDownloadCacheLock(projectId).writeLock().unlock();
                }
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private void createDownloadCacheIfNecessary(DownloadFormat format) throws IOException, OWLOntologyStorageException {
        ReadWriteLock projectDownloadCacheLock = getProjectDownloadCacheLock(projectId);
        try {
            projectDownloadCacheLock.writeLock().lock();
            File downloadCacheDirectory = projectFileStore.getDownloadCacheDirectory();
            File cachedFile = getDownloadCacheFile(format);
            if (!cachedFile.exists()) {
                downloadCacheDirectory.mkdirs();
                // Create
                OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
                OWLAPIChangeManager changeManager = project.getChangeManager();
                RevisionNumber currentRevisionNumber = changeManager.getCurrentRevision();

                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(cachedFile));
                exportProjectRevision(currentRevisionNumber, outputStream, format);
                outputStream.close();
            }
        }
        finally {
            projectDownloadCacheLock.writeLock().unlock();
        }
    }


    private void applyRevisionMetadataAnnotationsToOntology(RevisionNumber revisionNumber, OWLOntology revisionRootOntology) {
        checkNotNull(revisionNumber, "revisionNumber must not be null");
        checkNotNull(revisionRootOntology, "revisionRootOntology must not be null");
        OWLOntologyManager manager = revisionRootOntology.getOWLOntologyManager();
        RevisionMetadataAnnotater annotater = new RevisionMetadataAnnotater(projectId, revisionNumber, revisionRootOntology);
        manager.applyChanges(annotater.getChanges());
    }

    private OWLOntologyManager getOntologyManagerForRevision(RevisionNumber revision) {
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
        OWLAPIChangeManager changeManager = project.getChangeManager();
        return changeManager.getOntologyManagerForRevision(revision);
    }



    private void saveImportsClosureToStream(OWLOntology rootOntology, DownloadFormat format, OutputStream outputStream, RevisionNumber revisionNumber) throws IOException, OWLOntologyStorageException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String projectDisplayName = OWLAPIProjectMetadataManager.getManager().getDisplayName(projectId);
        String baseFolder = projectDisplayName.replace(" ", "-") + "-ontologies-" + format.getExtension();
        baseFolder = baseFolder.toLowerCase();
        baseFolder = baseFolder + "-REVISION-" + revisionNumber.getValue();
        ZipEntry rootOntologyEntry = new ZipEntry(baseFolder + "/root-ontology." + format.getExtension());
        zipOutputStream.putNextEntry(rootOntologyEntry);
        rootOntology.getOWLOntologyManager().saveOntology(rootOntology, format.getOntologyFormat(), zipOutputStream);
        zipOutputStream.closeEntry();
        int importCount = 0;
        for (OWLOntology ontology : rootOntology.getImports()) {
            importCount++;
            ZipEntry zipEntry = new ZipEntry(baseFolder + "/imported-ontology-" + importCount + "." + format.getExtension());
            zipOutputStream.putNextEntry(zipEntry);
            ontology.getOWLOntologyManager().saveOntology(ontology, format.getOntologyFormat(), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.flush();
    }


    private File getDownloadCacheFile(DownloadFormat format) {
        File downloadCacheDirectory = projectFileStore.getDownloadCacheDirectory();
        return new File(downloadCacheDirectory, "download." + format.getExtension() + ".zip");
    }


    private synchronized void createEmptyProject(NewProjectSettings newProjectSettings) throws IOException {
        try {
            IRI ontologyIRI = createUniqueOntologyIRI();
            OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            OWLOntology ontology = rootOntologyManager.createOntology(ontologyIRI);
            rootOntologyManager.setOntologyFormat(ontology, new BinaryOWLOntologyDocumentFormat());
            saveNewProjectOntologyAndCreateNotesOntologyDocument(rootOntologyManager, ontology);
        }
        catch (OWLOntologyCreationException e) {
            logger.severe(e);
            throw new RuntimeException(e);
        }
        catch (OWLOntologyStorageException e) {
            logger.severe(e);
            throw new RuntimeException(e);
        }
    }

    private synchronized void createProjectFromSources(NewProjectSettings newProjectSettings) throws IOException {
        try {
            File uploadsDirectory = FileUploadConstants.UPLOADS_DIRECTORY;

            DocumentId documentId = newProjectSettings.getSourceDocumentId();
            File uploadedFile = new File(uploadsDirectory, documentId.getDocumentId());
            UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                    new ZipInputStreamChecker(),
                    new ZipArchiveProjectSourcesExtractor(
                            new DefaultTempFileFactory(),
                            new DefaultRootOntologyDocumentMatcher()),
                    new SingleDocumentProjectSourcesExtractor()
            );

            if (uploadedFile.exists()) {
                OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
                RawProjectSources projectSources = extractor.extractProjectSources(uploadedFile);
                OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration()
                        .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
                RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
                OWLOntology ontology = importer.importRawProjectSources(projectSources);

                for(OWLOntology ont : rootOntologyManager.getOntologies()) {
                    rootOntologyManager.setOntologyFormat(ont, new BinaryOWLOntologyDocumentFormat());
                }
                saveNewProjectOntologyAndCreateNotesOntologyDocument(rootOntologyManager, ontology);
                deleteSourceFile(uploadedFile);
            }
            else {
                throw new FileNotFoundException(uploadedFile.getAbsolutePath());
            }

        }
        catch (OWLOntologyCreationException e) {
            logger.severe(e);
            throw new RuntimeException(e);
        }
        catch (OWLOntologyStorageException e) {
            logger.severe(e);
            throw new RuntimeException(e);
        }
    }

    private void deleteSourceFile(File sourceFile) {
        FileUtils.deleteQuietly(sourceFile);
    }

    private void saveNewProjectOntologyAndCreateNotesOntologyDocument(OWLOntologyManager rootOntologyManager, OWLOntology ontology) throws OWLOntologyStorageException {
        File binaryDocumentFile = getBinaryOntologyDocumentFile();
        binaryDocumentFile.getParentFile().mkdirs();
        rootOntologyManager.saveOntology(ontology, new BinaryOWLOntologyDocumentFormat(), IRI.create(binaryDocumentFile));
        ImportsCacheManager cacheManager = new ImportsCacheManager(projectId);
        cacheManager.cacheImports(ontology);
    }

    private static IRI createUniqueOntologyIRI() {
        String ontologyName = IdUtil.getBase62UUID();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    private File getBinaryOntologyDocumentFile() {
        return new File(projectFileStore.getOntologyDataDirectory(), ROOT_ONTOLOGY_DOCUMENT_NAME);
    }
}
