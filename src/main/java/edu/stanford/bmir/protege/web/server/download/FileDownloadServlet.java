package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
 * A servlet which allows ontologies to be downloaded from WebProtege.  See {@link OWLAPIProjectDownloader} for
 * the piece of machinery that actually does the processing of request parameters and the downloading.
 * </p>
 */
public class FileDownloadServlet extends HttpServlet {

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final ApplicationNameSupplier applicationNameSupplier;

    @Inject
    public FileDownloadServlet(
            @Nonnull ProjectManager projectManager,
            @Nonnull ProjectDetailsManager projectDetailsManager,
            @Nonnull AccessManager accessManager,
            @Nonnull ApplicationNameSupplier applicationNameSupplier) {
        this.projectManager = projectManager;
        this.projectDetailsManager = projectDetailsManager;
        this.accessManager = accessManager;
        this.applicationNameSupplier = applicationNameSupplier;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(req.getSession());
        UserId userId = webProtegeSession.getUserInSession();
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if (!accessManager.hasPermission(Subject.forUser(userId),
                                         new ProjectResource(downloadParameters.getProjectId()),
                                         BuiltInAction.DOWNLOAD_PROJECT)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (downloadParameters.isProjectDownload()) {
            ProjectId projectId = downloadParameters.getProjectId();
            RevisionNumber revisionNumber = downloadParameters.getRequestedRevision();
            DownloadFormat format = downloadParameters.getFormat();
            String displayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
            WebProtegeSession session = new WebProtegeSessionImpl(req.getSession());
            Project project = projectManager.getProject(projectId, session.getUserInSession());
            OWLAPIProjectDownloader downloader = new OWLAPIProjectDownloader(displayName,
                                                                             project,
                                                                             revisionNumber,
                                                                             format,
                                                                             applicationNameSupplier);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream());
            downloader.writeProject(resp, bufferedOutputStream);
            bufferedOutputStream.flush();
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
