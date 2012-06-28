package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStoreImpl;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
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

    public OWLAPIProjectDownloader(ProjectId projectId) {
        this.projectId = projectId;
    }
    
    public void writeProject(HttpServletResponse response, OutputStream outputStream) throws IOException {
        try {
            OWLAPIProjectDocumentStoreImpl documentStore = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(projectId);
            setFileType(response);
            setFileName(response);
            documentStore.exportProject(outputStream, new RDFXMLOntologyFormat());
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
        String fileName = projectId.getProjectName().replaceAll("\\s+", "-") + "-ontologies.zip";
        fileName = fileName.toLowerCase();
        response.setHeader(CONTENT_DISPOSITION_HEADER_FIELD, "attachment; filename=\"" + fileName + "\"");
    }
}
