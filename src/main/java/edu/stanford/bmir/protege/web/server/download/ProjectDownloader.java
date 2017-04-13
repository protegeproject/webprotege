package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 */
public class ProjectDownloader {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDownloader.class);

    public static final String MIME_TYPE = "application/zip";

    public static final String CONTENT_DISPOSITION_HEADER_FIELD = "Content-Disposition";

    @Nonnull
    private final RevisionNumber revision;

    @Nonnull
    private final DownloadFormat format;

    @Nonnull
    private final String fileName;

    @Nonnull
    private final Project project;

    @Nonnull
    private final ApplicationNameSupplier applicationNameSupplier;

    /**
     * Creates a project downloader that downloads the specified revision of the specified project.
     * @param project The project to be downloaded.  Not <code>null</code>.
     * @param revision The revision of the project to be downloaded.
     * @param format The format which the project should be downloaded in.
     */
    @Inject
    public ProjectDownloader(@Nonnull String fileName,
                             @Nonnull Project project,
                             @Nonnull RevisionNumber revision,
                             @Nonnull DownloadFormat format,
                             @Nonnull ApplicationNameSupplier applicationNameSupplier) {
        this.project = project;
        this.revision = revision;
        this.format = format;
        this.fileName = fileName;
        this.applicationNameSupplier = checkNotNull(applicationNameSupplier);
    }
    
    public void writeProject(HttpServletResponse response, OutputStream outputStream) throws IOException {
        try {
            setFileType(response);
            setFileName(response);
            exportProjectRevision(fileName, revision, outputStream, format);

        }
        catch (OWLOntologyStorageException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private void setFileType(HttpServletResponse response) {
        response.setContentType(MIME_TYPE);
    }

    private void setFileName(@Nonnull HttpServletResponse response) {
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
            logger.info("Project download failed because the root ontology could not be retrived from the manager.  Project: ", project.getProjectId());
            throw new RuntimeException("The ontology could not be downloaded from " + applicationNameSupplier.get() + ".  Please contact the administrator.");
        }
    }

    private void saveImportsClosureToStream(
            String projectDisplayName,
            OWLOntology rootOntology,
            DownloadFormat format,
            OutputStream outputStream,
            RevisionNumber revisionNumber) throws
            IOException,
            OWLOntologyStorageException {
        MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
        // TODO: Separate object
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        String baseFolder = projectDisplayName.replace(" ", "-") + "-ontologies-" + format.getExtension();
        baseFolder = baseFolder.toLowerCase();
        baseFolder = baseFolder + "-REVISION-" + (revisionNumber.isHead() ? "HEAD" : revisionNumber.getValue());
        ZipEntry rootOntologyEntry = new ZipEntry(baseFolder + "/root-ontology." + format.getExtension());
        zipOutputStream.putNextEntry(rootOntologyEntry);
        rootOntology.getOWLOntologyManager().saveOntology(rootOntology, format.getDocumentFormat(), zipOutputStream);
        zipOutputStream.closeEntry();
        int importCount = 0;
        for (OWLOntology ontology : rootOntology.getImports()) {
            importCount++;
            ZipEntry zipEntry = new ZipEntry(baseFolder + "/imported-ontology-" + importCount + "." + format
                    .getExtension());
            zipOutputStream.putNextEntry(zipEntry);
            ontology.getOWLOntologyManager().saveOntology(ontology, format.getDocumentFormat(), zipOutputStream);
            zipOutputStream.closeEntry();
            memoryMonitor.monitorMemoryUsage();
        }
        zipOutputStream.finish();
        zipOutputStream.flush();
    }

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
        return Optional.empty();
    }
}
