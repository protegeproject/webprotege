package edu.stanford.bmir.protege.web.server.filedownload;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 */
public class OWLAPIProjectDownloader {

    public static final String MIME_TYPE = "application/zip";

    public static final String CONTENT_DISPOSITION_HEADER_FIELD = "Content-Disposition";

    private RevisionNumber revision;

    private DownloadFormat format;

    private String fileName;

    private OWLAPIProject project;

    /**
     * Creates a project downloader that downloads the specified revision of the specified project.
     * @param projectId The project to be downloaded.  Not <code>null</code>.
     * @param revision The revision of the project to be downloaded.
     * @param format The format which the project should be downloaded in.
     */
    public OWLAPIProjectDownloader(String fileName, OWLAPIProject project, RevisionNumber revision, DownloadFormat format) {
        this.project = project;
        this.revision = revision;
        this.format = format;
        this.fileName = fileName;
    }
    
    public void writeProject(HttpServletResponse response, OutputStream outputStream) throws IOException {
        try {
            setFileType(response);
            setFileName(response);
//            if(revision.isHead()) {
//                documentStore.exportProject(fileName, outputStream, format);
//            }
//            else {
                exportProjectRevision(fileName, revision, outputStream, format);
//            }

        }
        catch (OWLOntologyStorageException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private void setFileType(HttpServletResponse response) {
        response.setContentType(MIME_TYPE);
    }

    private void setFileName(HttpServletResponse response) {
        String revisionNumber;
        if(revision.isHead()) {
            revisionNumber = "";
        }
        else {
            revisionNumber = "-REVISION-" + Long.toString(revision.getValue());
        }
        String displayName = fileName;
        String fileName = displayName.replaceAll("\\s+", "-") + revisionNumber + "-ontologies." + format.getExtension() + ".zip";
        fileName = fileName.toLowerCase();
        response.setHeader(CONTENT_DISPOSITION_HEADER_FIELD, "attachment; filename=\"" + fileName + "\"");
    }

    private void exportProjectRevision(
            String projectDisplayName,
            RevisionNumber revisionNumber,
            OutputStream outputStream,
            DownloadFormat format) throws IOException, OWLOntologyStorageException {
        checkNotNull(revisionNumber);
        checkNotNull(outputStream);
        checkNotNull(format);

        RevisionManager revisionManager = project.getChangeManager();
        OWLOntologyManager manager = revisionManager.getOntologyManagerForRevision(revisionNumber);
        OWLOntologyID rootOntologyId = project.getRootOntology().getOntologyID();
        Optional<OWLOntology> revisionRootOntology = getOntologyFromManager(manager, rootOntologyId);
        if (revisionRootOntology.isPresent()) {
            saveImportsClosureToStream(projectDisplayName, revisionRootOntology.get(), format, outputStream, revisionNumber);
        }
        else {
            // An error - no flipping ontology!
            WebProtegeProperties properties = WebProtegeInjector.get().getInstance(WebProtegeProperties.class);

            throw new RuntimeException("The ontology could not be downloaded from " + properties.getApplicationName() + ".  Please contact the administrator.");
        }
    }

//    private OWLOntologyManager getOntologyManagerForRevision(RevisionNumber revision) {
//        RevisionManager changeManager = project.getChangeManager();
//        return changeManager.getOntologyManagerForRevision(revision);
//    }

    private void saveImportsClosureToStream(
            String projectDisplayName,
            OWLOntology rootOntology,
            DownloadFormat format,
            OutputStream outputStream,
            RevisionNumber revisionNumber) throws
            IOException,
            OWLOntologyStorageException {
        // TODO: Separate object
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
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
            ZipEntry zipEntry = new ZipEntry(baseFolder + "/imported-ontology-" + importCount + "." + format
                    .getExtension());
            zipOutputStream.putNextEntry(zipEntry);
            ontology.getOWLOntologyManager().saveOntology(ontology, format.getOntologyFormat(), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.finish();
        zipOutputStream.flush();
    }

//     private void createDownloadCacheIfNecessary(String projectDisplayName, DownloadFormat format) throws IOException, OWLOntologyStorageException {
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


    /**
     * Gets an ontology from the manager specified manager.  This method is a workaround for
     * https://github.com/owlcs/owlapi/issues/215
     * https://github.com/protegeproject/webprotege/issues/143
     *
     * @param manager        The manager.  Not {@code null}.
     * @param rootOntologyId The OntologyId.  Not {@code null}.
     * @return The ontology or an absent value if the manager does not contain the ontology.
     */
    private static Optional<OWLOntology> getOntologyFromManager(
            OWLOntologyManager manager,
            OWLOntologyID rootOntologyId) {
        checkNotNull(manager);
        checkNotNull(rootOntologyId);
        for (OWLOntology ont : manager.getOntologies()) {
            if (rootOntologyId.equals(ont.getOntologyID())) {
                return Optional.of(ont);
            }
        }
        if (rootOntologyId.isAnonymous()) {
            if (manager.getOntologies().size() == 1) {
                return Optional.of(manager.getOntologies().iterator().next());
            }
        }
        return Optional.absent();
    }
}
