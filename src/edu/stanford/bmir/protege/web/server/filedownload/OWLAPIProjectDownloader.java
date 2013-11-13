package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 */
public class OWLAPIProjectDownloader {

    public static final String MIME_TYPE = "application/zip";

    public static final String CONTENT_DISPOSITION_HEADER_FIELD = "Content-Disposition";

    private ProjectId projectId;

    private RevisionNumber revision;

    private DownloadFormat format;

    /**
     * Creates a project downloader that downloads the specified revision of the specified project.
     * @param projectId The project to be downloaded.  Not <code>null</code>.
     * @param revision The revision of the project to be downloaded.
     * @param format The format which the project should be downloaded in.
     */
    public OWLAPIProjectDownloader(ProjectId projectId, RevisionNumber revision, DownloadFormat format) {
        this.projectId = projectId;
        this.revision = revision;
        this.format = format;
    }
    
    public void writeProject(HttpServletResponse response, OutputStream outputStream) throws IOException {
        try {
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
            setFileType(response);
            setFileName(response);
            if(revision.isHead()) {
                documentStore.exportProject(outputStream, format);
            }
            else {
                documentStore.exportProjectRevision(revision, outputStream, format);
            }

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
        String displayName = OWLAPIProjectMetadataManager.getManager().getDisplayName(projectId);
        String fileName = displayName.replaceAll("\\s+", "-") + revisionNumber + "-ontologies." + format.getExtension() + ".zip";
        fileName = fileName.toLowerCase();
        response.setHeader(CONTENT_DISPOSITION_HEADER_FIELD, "attachment; filename=\"" + fileName + "\"");
    }
}
