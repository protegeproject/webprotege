package edu.stanford.bmir.protege.web.server.download;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.project.PrefixDeclarationsStore;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
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

    @Nonnull
    private final RevisionNumber revision;

    @Nonnull
    private final DownloadFormat format;

    @Nonnull
    private final String fileName;

    @Nonnull
    private final PrefixDeclarationsStore prefixDeclarationsStore;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final ProjectId projectId;

    /**
     * Creates a project downloader that downloads the specified revision of the specified project.
     *
     * @param revisionManager         The revision manager of project to be downloaded.  Not <code>null</code>.
     * @param revision                The revision of the project to be downloaded.
     * @param format                  The format which the project should be downloaded in.
     * @param prefixDeclarationsStore The prefix declarations store that is used to retrieve customised prefixes
     */
    @AutoFactory
    @Inject
    public ProjectDownloader(@Nonnull ProjectId projectId,
                             @Nonnull String fileName,
                             @Nonnull RevisionNumber revision,
                             @Nonnull DownloadFormat format,
                             @Nonnull RevisionManager revisionManager,
                             @Provided @Nonnull PrefixDeclarationsStore prefixDeclarationsStore) {
        this.projectId = checkNotNull(projectId);
        this.revision = checkNotNull(revision);
        this.revisionManager = checkNotNull(revisionManager);
        this.format = checkNotNull(format);
        this.fileName = checkNotNull(fileName);
        this.prefixDeclarationsStore = checkNotNull(prefixDeclarationsStore);
    }

    public void writeProject(OutputStream outputStream) throws IOException {
        try {
            exportProjectRevision(fileName, revision, outputStream, format);

        } catch(OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

    private void exportProjectRevision(@Nonnull String projectDisplayName,
                                       @Nonnull RevisionNumber revisionNumber,
                                       @Nonnull OutputStream outputStream,
                                       @Nonnull DownloadFormat format) throws IOException, OWLOntologyStorageException {
        OWLOntologyManager manager = revisionManager.getOntologyManagerForRevision(revisionNumber);
        saveOntologiesToStream(projectDisplayName, manager, format, outputStream, revisionNumber);
    }

    private void saveOntologiesToStream(@Nonnull String projectDisplayName,
                                        @Nonnull OWLOntologyManager manager,
                                        @Nonnull DownloadFormat format,
                                        @Nonnull OutputStream outputStream,
                                        @Nonnull RevisionNumber revisionNumber) throws IOException, OWLOntologyStorageException {
        // TODO: Separate object
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream))) {
            String baseFolder = projectDisplayName.replace(" ", "-") + "-ontologies-" + format.getExtension();
            baseFolder = baseFolder.toLowerCase();
            baseFolder = baseFolder + "-REVISION-" + (revisionNumber.isHead() ? "HEAD" : revisionNumber.getValue());
            for(var ontology : manager.getOntologies()) {
                var documentFormat = format.getDocumentFormat();
                if(documentFormat.isPrefixOWLOntologyFormat()) {
                    var prefixDocumentFormat = documentFormat.asPrefixOWLOntologyFormat();
                    Map<String, String> prefixes = prefixDeclarationsStore.find(projectId).getPrefixes();
                    prefixes.forEach(prefixDocumentFormat::setPrefix);
                }
                var ontologyShortForm = getOntologyShortForm(ontology);
                var ontologyDocumentFileName = ontologyShortForm.replace(":", "_");
                ZipEntry zipEntry = new ZipEntry(baseFolder + "/" + ontologyDocumentFileName + "." + format.getExtension());
                zipOutputStream.putNextEntry(zipEntry);
                ontology.getOWLOntologyManager().saveOntology(ontology, documentFormat, zipOutputStream);
                zipOutputStream.closeEntry();
                logMemoryUsage();
            }
            zipOutputStream.finish();
            zipOutputStream.flush();
        }
    }

    private void logMemoryUsage() {
        MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.monitorMemoryUsage();
    }

    private String getOntologyShortForm(OWLOntology ontology) {
        return new OntologyIRIShortFormProvider().getShortForm(ontology);
    }
}
