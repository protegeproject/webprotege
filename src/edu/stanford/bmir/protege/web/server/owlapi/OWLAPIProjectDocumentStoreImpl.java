package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadConstants;
import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.coode.owlapi.obo.parser.OBOPrefix;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.binaryowl.BinaryOWLMetadata;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.owlapi.binaryowl.change.OntologyChangeRecordInfoList;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecordInfo;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
public class OWLAPIProjectDocumentStoreImpl extends OWLAPIProjectDocumentStore {


    public static final String GENERATED_ONTOLOGY_IRI_PREFIX = "http://webprotege.stanford.edu/ontologies/";

    public static final String PROJECT_ATTRIBUTES_FILE_NAME = "project-attributes.binary";

    private ProjectId projectId;

    private OWLAPIProjectFileStore projectFileStore;

    private static final String ROOT_ONTOLOGY_DOCUMENT_NAME = "root-ontology.binary";

    private static final String CHANGE_DATA_FILE_NAME = "change-data.binary";


    private static Map<ProjectId, ReadWriteLock> projectLockMap = new WeakHashMap<ProjectId, ReadWriteLock>();

    private static Map<ProjectId, ReadWriteLock> projectDownloadCacheLock = new WeakHashMap<ProjectId, ReadWriteLock>();


    private static ReadWriteLock getProjectReadWriteLock(ProjectId projectId) {
        // Synchronized on the class because it should be global over all instances of document store for the
        // specified project.
        synchronized (OWLAPIProjectDocumentStoreImpl.class) {
            ReadWriteLock lock = projectLockMap.get(projectId);
            if (lock == null) {
                lock = new ReentrantReadWriteLock();
                projectLockMap.put(projectId, lock);
            }
            return lock;
        }
    }


    private static ReadWriteLock getProjectDownloadCacheLock(ProjectId projectId) {
        synchronized (OWLAPIProjectDocumentStoreImpl.class) {
            ReadWriteLock lock = projectDownloadCacheLock.get(projectId);
            if (lock == null) {
                lock = new ReentrantReadWriteLock();
                projectDownloadCacheLock.put(projectId, lock);
            }
            return lock;
        }
    }


    public static OWLAPIProjectDocumentStoreImpl getProjectDocumentStore(ProjectId projectId) {
        return new OWLAPIProjectDocumentStoreImpl(projectId);
    }


    public static OWLAPIProjectDocumentStoreImpl createNewProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, IOException {
        return new OWLAPIProjectDocumentStoreImpl(newProjectSettings);
    }


    private OWLAPIProjectDocumentStoreImpl(ProjectId projectId) {
        this.projectId = projectId;
        this.projectFileStore = OWLAPIProjectFileStore.getProjectFileStore(projectId);
    }

    private OWLAPIProjectDocumentStoreImpl(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, IOException {
        this.projectId = new ProjectId(newProjectSettings.getProjectName());
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

    public void exportProject(OutputStream outputStream, OWLOntologyFormat format) throws IOException, OWLOntologyStorageException {
        // Does it already exist in the download cache?
        createDownloadCacheIfNecessary(format);
        // Feed cached file to caller
        try {
            getProjectDownloadCacheLock(projectId).readLock().lock();
            byte[] buffer = new byte[4096];
            File downloadCache = getDownloadCacheFile(format.toString());
            InputStream is = new BufferedInputStream(new FileInputStream(downloadCache));
            int read;
            while ((read = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            is.close();
            outputStream.flush();
        }
        finally {
            getProjectDownloadCacheLock(projectId).readLock().unlock();
        }
    }

    private void createDownloadCacheIfNecessary(OWLOntologyFormat format) throws IOException, OWLOntologyStorageException {
        try {
            getProjectDownloadCacheLock(projectId).writeLock().lock();
            File downloadCacheDirectory = projectFileStore.getDownloadCacheDirectory();
            String formatName = format.toString();
            File cachedFile = getDownloadCacheFile(formatName);
            if (!cachedFile.exists()) {
                downloadCacheDirectory.mkdirs();
                // Create
                OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
                OWLOntology rootOntology = project.getRootOntology();
                String projectName = project.getProjectId().getProjectName();
                ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(cachedFile)));
                String baseFolder = projectName.replace(" ", "-") + "-ontologies";
                // Revision might not match exactly with what is downloaded.  Could be a problem!
                long revision = project.getRevision();
                baseFolder = baseFolder.toLowerCase();
                baseFolder = baseFolder + "-REVISION-" + revision;
                ZipEntry rootOntologyEntry = new ZipEntry(baseFolder + "/root-ontology.owl");
                zipOutputStream.putNextEntry(rootOntologyEntry);
                rootOntology.getOWLOntologyManager().saveOntology(rootOntology, format, zipOutputStream);
                zipOutputStream.closeEntry();
                int importCount = 0;
                for (OWLOntology ontology : project.getRootOntology().getImports()) {
                    importCount++;
                    ZipEntry zipEntry = new ZipEntry(baseFolder + "/imported-ontology-" + importCount + ".owl");
                    zipOutputStream.putNextEntry(zipEntry);
                    ontology.getOWLOntologyManager().saveOntology(ontology, format, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.finish();
                zipOutputStream.close();
            }
        }
        finally {
            getProjectDownloadCacheLock(projectId).writeLock().unlock();
        }
    }

    private File getDownloadCacheFile(String formatName) {
        File downloadCacheDirectory = projectFileStore.getDownloadCacheDirectory();
        String escapedFileName = formatName.replaceAll("\\/|\\\\", "-");
        return new File(downloadCacheDirectory, "download-" + escapedFileName + ".zip");
    }


    public ProjectId getProjectId() {
        return projectId;
    }


    public synchronized OWLAPIProjectAttributes getProjectAttributes() throws IOException {
        File projectAttributesFile = getProjectAttributesFile();
        if (projectAttributesFile.exists()) {
            DataInput dataInput = new DataInputStream(new BufferedInputStream(new FileInputStream(projectAttributesFile)));
            return new OWLAPIProjectAttributes(dataInput);
        }
        else {
            OWLAPIProjectAttributes freshProjectAttributes = new OWLAPIProjectAttributes();
            saveProjectAttributes(freshProjectAttributes);
            return freshProjectAttributes;
        }
    }

    public void saveProjectAttributes(OWLAPIProjectAttributes projectAttributes) throws IOException {
        File metadataFile = getProjectAttributesFile();
        metadataFile.getParentFile().mkdirs();
        DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(metadataFile)));
        projectAttributes.write(dataOutput);
        dataOutput.close();
    }


    private File getProjectAttributesFile() {
        OWLAPIProjectFileStore projectFileStore = OWLAPIProjectFileStore.getProjectFileStore(projectId);
        return new File(projectFileStore.getProjectDirectory(), PROJECT_ATTRIBUTES_FILE_NAME);
    }

    public OWLOntology loadRootOntologyIntoManager(OWLOntologyManager manager) throws OWLOntologyCreationException {
        try {

            getProjectReadWriteLock(projectId).readLock().lock();

            long t0 = System.currentTimeMillis();
            OWLOntologyLoaderListener loaderListener = new OWLOntologyLoaderListener() {
                public void startedLoadingOntology(LoadingStartedEvent event) {
                    System.out.println("Loading started: " + event.getDocumentIRI());
                }

                public void finishedLoadingOntology(LoadingFinishedEvent event) {
                    if (event.isSuccessful()) {
                        System.out.println("Loading finished: " + event.getDocumentIRI() + " (Loaded: " + event.getOntologyID() + ")");
                    }
                    else {
                        System.out.println("Loading failed: " + event.getDocumentIRI() + " (Reason: " + event.getException().getMessage() + ")");
                    }
                }
            };
            manager.addOntologyLoaderListener(loaderListener);
            File ontologyDataDirectory = projectFileStore.getOntologyDataDirectory();
            File rootOntologyDocument = new File(ontologyDataDirectory, ROOT_ONTOLOGY_DOCUMENT_NAME);
            AutoIRIMapper iriMapper = new AutoIRIMapper(ontologyDataDirectory, true);
            manager.addIRIMapper(iriMapper);
            try {
                OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
                config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
                FileDocumentSource documentSource = new FileDocumentSource(rootOntologyDocument);
                return manager.loadOntologyFromOntologyDocument(documentSource, config);
            }
            finally {
                long t1 = System.currentTimeMillis();
                System.out.println("Loaded project (" + projectId + "): " + (t1 - t0));
                manager.removeIRIMapper(iriMapper);
                manager.removeOntologyLoaderListener(loaderListener);
            }
        }
        finally {
            getProjectReadWriteLock(projectId).readLock().unlock();
        }

    }


    public File getNotesDataDirectory() {
        return projectFileStore.getNotesDataDirectory();
    }

    public File getChangeDataFile() {
        return new File(projectFileStore.getChangesDataDirectory(), CHANGE_DATA_FILE_NAME);
    }


    public boolean exists() {
        return getBinaryOntologyDocumentFile().exists();
    }

    private synchronized OWLAPIProjectAttributes createEmptyProject(NewProjectSettings newProjectSettings) throws IOException {
        try {
            IRI ontologyIRI = createUniqueOntologyIRI();
            OWLOntologyManager rootOntologyManager = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = rootOntologyManager.createOntology(ontologyIRI);
            rootOntologyManager.setOntologyFormat(ontology, new BinaryOWLOntologyDocumentFormat());
            saveNewProjectOntologyAndCreateNotesOntologyDocument(rootOntologyManager, ontology);
            OWLAPIProjectConfiguration configuration = new OWLAPIProjectConfiguration(new DefaultEntityEditorKitFactory(), OWLAPIProjectType.getDefaultProjectType());
            OWLAPIProjectAttributes projectAttributes = configuration.getProjectAttributes();
            saveProjectAttributes(projectAttributes);
            return projectAttributes;
        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
        catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized OWLAPIProjectAttributes createProjectFromSources(NewProjectSettings newProjectSettings) throws IOException {
        try {
            File uploadsDirectory = FileUploadConstants.UPLOADS_DIRECTORY;

            DocumentId documentId = newProjectSettings.getSourceDocumentId();
            File uploadedFile = new File(uploadsDirectory, documentId.getDocumentId());
            if (uploadedFile.exists()) {
                OWLOntologyManager rootOntologyManager = OWLManager.createOWLOntologyManager();
                OWLOntology ontology = rootOntologyManager.loadOntologyFromOntologyDocument(uploadedFile);
                OWLAPIProjectAttributes projectAttributes = createProjectAttributes(ontology);
                rootOntologyManager.setOntologyFormat(ontology, new BinaryOWLOntologyDocumentFormat());
                saveNewProjectOntologyAndCreateNotesOntologyDocument(rootOntologyManager, ontology);
                saveProjectAttributes(projectAttributes);
                deleteSourceFile(uploadedFile);
                return projectAttributes;
            }
            else {
                throw new FileNotFoundException(uploadedFile.getAbsolutePath());
            }

        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
        catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteSourceFile(File sourceFile) {
        sourceFile.delete();
    }

    private OWLAPIProjectAttributes createProjectAttributes(OWLOntology ontology) {
        OWLAPIProjectAttributes projectAttributes;
        if (isOBOFormat(ontology)) {
            OWLAPIProjectConfiguration configuration = new OWLAPIProjectConfiguration(new OBOEntityEditorKitFactory(), OWLAPIProjectType.getOBOProjectType());
            projectAttributes = configuration.getProjectAttributes();
        }
        else {
            OWLAPIProjectConfiguration configuration = new OWLAPIProjectConfiguration(new DefaultEntityEditorKitFactory(), OWLAPIProjectType.getDefaultProjectType());
            projectAttributes = configuration.getProjectAttributes();
        }
        return projectAttributes;
    }

    private boolean isOBOFormat(OWLOntology ontology) {
        OWLOntologyManager man = ontology.getOWLOntologyManager();
        if (man.getOntologyFormat(ontology) instanceof OBOOntologyFormat) {
            return true;
        }
        for (OWLAnnotationProperty property : ontology.getAnnotationPropertiesInSignature()) {
            if (isOBOEntity(property)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOBOEntity(OWLEntity entity) {
        String propertyIRI = entity.getIRI().toString();
        if (propertyIRI.startsWith(OBOPrefix.OBO.getPrefix())) {
            return true;
        }
        else if (propertyIRI.startsWith(OBOPrefix.OBO_IN_OWL.getPrefix())) {
            return true;
        }
        else if (propertyIRI.startsWith(OBOPrefix.IAO.getPrefix())) {
            return true;
        }
        return false;
    }

    private void saveNewProjectOntologyAndCreateNotesOntologyDocument(OWLOntologyManager rootOntologyManager, OWLOntology ontology) throws OWLOntologyStorageException {
        File binaryDocumentFile = getBinaryOntologyDocumentFile();
        binaryDocumentFile.getParentFile().mkdirs();
        rootOntologyManager.saveOntology(ontology, IRI.create(binaryDocumentFile));
    }

    private static IRI createUniqueOntologyIRI() {
        UUID ontologyUUID = UUID.randomUUID();
        String ontologyName = ontologyUUID.toString();
        return IRI.create(GENERATED_ONTOLOGY_IRI_PREFIX + ontologyName);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void saveOntologyChanges(List<OWLOntologyChange> changeList) {
        // Put changes into a buffer
        try {
            getProjectReadWriteLock(projectId).writeLock().lock();
            try {
                File file = getBinaryOntologyDocumentFile();
                BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
                List<OWLOntologyChangeRecordInfo> infoList = new ArrayList<OWLOntologyChangeRecordInfo>();
                for (OWLOntologyChange change : changeList) {
                    OWLOntologyChangeRecord changeRecord = new OWLOntologyChangeRecord(change);
                    infoList.add(changeRecord.getInfo());
                }
                serializer.appendOntologyChanges(file, new OntologyChangeRecordInfoList(infoList, System.currentTimeMillis(), BinaryOWLMetadata.emptyMetadata()));
            }
            catch (IOException e) {
                // TODO:
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

    public void deleteCacheFiles() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    getProjectDownloadCacheLock(projectId).writeLock().lock();
                    File cachedFilesDirectory = projectFileStore.getDownloadCacheDirectory();
                    if (cachedFilesDirectory.exists()) {
                        for (File cachedFile : cachedFilesDirectory.listFiles()) {
                            if (!cachedFile.isHidden()) {
                                System.out.println("Deleting cached file: " + cachedFile.getAbsolutePath());
                                cachedFile.delete();
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

    private File getBinaryOntologyDocumentFile() {
        return new File(projectFileStore.getOntologyDataDirectory(), ROOT_ONTOLOGY_DOCUMENT_NAME);
    }


}
