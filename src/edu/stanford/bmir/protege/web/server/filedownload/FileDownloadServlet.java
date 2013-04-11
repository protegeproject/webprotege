package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 * <p>
 *     A servlet which allows ontologies to be downloaded from WebProtege.  See {@link OWLAPIProjectDownloader} for
 *     the piece of machinery that actually does the processing of request parameters and the downloading.
 * </p>
 */
public class FileDownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if(downloadParameters.isProjectDownload()) {
            ProjectId projectId = downloadParameters.getProjectId();
            RevisionNumber revisionNumber = downloadParameters.getRequestedRevision();
            OWLAPIProjectDownloader downloader = new OWLAPIProjectDownloader(projectId, revisionNumber);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream());
            downloader.writeProject(resp, bufferedOutputStream);
            bufferedOutputStream.flush();
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
