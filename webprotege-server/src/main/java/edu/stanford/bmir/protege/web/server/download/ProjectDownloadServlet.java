package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.app.ServerSingleton;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static edu.stanford.bmir.protege.web.server.logging.RequestFormatter.formatAddr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 * <p>
 * A servlet which allows ontologies to be downloaded from WebProtege.  See {@link ProjectDownloader} for
 * the piece of machinery that actually does the processing of request parameters and the downloading.
 * </p>
 */
@ApplicationSingleton
public class ProjectDownloadServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDownloadServlet.class);

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ProjectDownloadService projectDownloadService;

    @Inject
    public ProjectDownloadServlet(@Nonnull AccessManager accessManager,
                                  @Nonnull ProjectDownloadService projectDownloadService) {
        this.accessManager = accessManager;
        this.projectDownloadService = projectDownloadService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(req.getSession());
        UserId userId = webProtegeSession.getUserInSession();
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if(!downloadParameters.isProjectDownload()) {
            logger.info("Bad project download request from {} at {}.  Request URI: {}  Query String: {}",
                        webProtegeSession.getUserInSession(),
                        formatAddr(req),
                        req.getRequestURI(),
                        req.getQueryString());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        logger.info("Received download request from {} at {} for project {}",
                    userId,
                    formatAddr(req),
                    downloadParameters.getProjectId());

        if (!accessManager.hasPermission(Subject.forUser(userId),
                                         new ProjectResource(downloadParameters.getProjectId()),
                                         BuiltInAction.DOWNLOAD_PROJECT)) {
            logger.info("Denied download request as user does not have permission to download this project.");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        else if (downloadParameters.isProjectDownload()) {
            startProjectDownload(resp, userId, downloadParameters);
        }
    }

    private void startProjectDownload(HttpServletResponse resp,
                                      UserId userId,
                                      FileDownloadParameters downloadParameters) throws IOException {
        ProjectId projectId = downloadParameters.getProjectId();
        RevisionNumber revisionNumber = downloadParameters.getRequestedRevision();
        DownloadFormat format = downloadParameters.getFormat();
        projectDownloadService.downloadProject(userId, projectId, revisionNumber, format, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
        projectDownloadService.shutDown();
    }
}
