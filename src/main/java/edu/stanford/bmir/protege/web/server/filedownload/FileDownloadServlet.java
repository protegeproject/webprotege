package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.server.inject.ApplicationName;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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

    @Nonnull
    private final OWLAPIProjectManager projectManager;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final String applicationName;

    @Inject
    public FileDownloadServlet(
            @Nonnull OWLAPIProjectManager projectManager,
            @Nonnull ProjectDetailsManager projectDetailsManager,
            @Nonnull @ApplicationName String applicationName) {
        this.projectManager = projectManager;
        this.projectDetailsManager = projectDetailsManager;
        this.applicationName = applicationName;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if(downloadParameters.isProjectDownload()) {
            ProjectId projectId = downloadParameters.getProjectId();
            RevisionNumber revisionNumber = downloadParameters.getRequestedRevision();
            DownloadFormat format = downloadParameters.getFormat();
            String displayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
            OWLAPIProject project = projectManager.getProject(projectId);
            OWLAPIProjectDownloader downloader = new OWLAPIProjectDownloader(displayName, project, revisionNumber, format, applicationName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream());
            downloader.writeProject(resp, bufferedOutputStream);
            bufferedOutputStream.flush();
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
