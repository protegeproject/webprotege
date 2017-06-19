package edu.stanford.bmir.protege.web.server.download;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Striped;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.revision.HeadRevisionNumberFinder;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
@ApplicationSingleton
public class ProjectDownloadService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDownloadService.class);

    @Nonnull
    private final ExecutorService downloadGeneratorExecutor;

    @Nonnull
    private final ExecutorService fileTransferExecutor;

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final ApplicationNameSupplier applicationNameSupplier;

    @Nonnull
    private final ProjectDownloadCache projectDownloadCache;

    @Nonnull
    private final HeadRevisionNumberFinder headRevisionNumberFinder;

    private final Striped<Lock> lockStripes = Striped.lazyWeakLock(10);

    @Inject
    public ProjectDownloadService(@Nonnull @DownloadGeneratorExecutor ExecutorService downloadGeneratorExecutor,
                                  @Nonnull @FileTransferExecutor ExecutorService fileTransferExecutor,
                                  @Nonnull ProjectManager projectManager,
                                  @Nonnull ProjectDetailsManager projectDetailsManager,
                                  @Nonnull ApplicationNameSupplier applicationNameSupplier,
                                  @Nonnull ProjectDownloadCache projectDownloadCache,
                                  @Nonnull HeadRevisionNumberFinder headRevisionNumberFinder) {
        this.downloadGeneratorExecutor = checkNotNull(downloadGeneratorExecutor);
        this.fileTransferExecutor = checkNotNull(fileTransferExecutor);
        this.projectManager = checkNotNull(projectManager);
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
        this.applicationNameSupplier = checkNotNull(applicationNameSupplier);
        this.projectDownloadCache = checkNotNull(projectDownloadCache);
        this.headRevisionNumberFinder = checkNotNull(headRevisionNumberFinder);
    }

    public void downloadProject(@Nonnull UserId requester,
                                @Nonnull ProjectId projectId,
                                @Nonnull RevisionNumber revisionNumber,
                                @Nonnull DownloadFormat downloadFormat,
                                @Nonnull HttpServletResponse response) throws IOException {

        RevisionNumber realRevisionNumber;
        if(revisionNumber.isHead()) {
            realRevisionNumber = getHeadRevisionNumber(projectId, requester);
        }
        else {
            realRevisionNumber = revisionNumber;
        }

        Path downloadPath = projectDownloadCache.getCachedDownloadPath(projectId, realRevisionNumber, downloadFormat);

        createDownloadIfNecessary(requester,
                                  projectId,
                                  revisionNumber,
                                  downloadFormat,
                                  downloadPath);

        transferFileToClient(projectId,
                             requester,
                             revisionNumber,
                             downloadFormat,
                             downloadPath,
                             response);
    }

    private void createDownloadIfNecessary(@Nonnull UserId requester,
                                           @Nonnull ProjectId projectId,
                                           @Nonnull RevisionNumber revisionNumber,
                                           @Nonnull DownloadFormat downloadFormat,
                                           @Nonnull Path downloadPath) {
        // This thing always returns the same lock for the same project.
        // This means that we won't create the *same* download more than once.  It
        // does mean that multiple *different* downloads could possibly be created at the same time
        Lock lock = lockStripes.get(projectId);
        try {
            lock.lock();
            if (Files.exists(downloadPath)) {
                logger.info("{} {} Download for the requested revision already exists.  Using cached download.",
                            projectId,
                            requester);
                return;
            }
            CreateDownloadTask task = new CreateDownloadTask(projectManager,
                                                             projectId,
                                                             requester,
                                                             getProjectDisplayName(projectId),
                                                             revisionNumber,
                                                             downloadFormat,
                                                             applicationNameSupplier.get(),
                                                             downloadPath);
            logger.info("{} {} Submitted request to create download to queue", projectId, requester);
            Future<?> futureOfCreateDownload = downloadGeneratorExecutor.submit(task);
            try {
                Stopwatch stopwatch = Stopwatch.createStarted();
                futureOfCreateDownload.get();
                logger.info("{} {} Created download after {} ms", projectId, requester, stopwatch.elapsed(MILLISECONDS));
            } catch (InterruptedException e) {
                logger.info("{} {} The download of this project was interrupted.", projectId, requester);
            } catch (ExecutionException e) {
                logger.info("{} {} An execution exception occurred whilst creating the download.  Cause: {}",
                            projectId,
                            requester,
                            Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse(""),
                            e.getCause());
            }
        }
        finally {
            lock.unlock();
        }
    }

    private String getProjectDisplayName(@Nonnull ProjectId projectId) {
        return projectDetailsManager.getProjectDetails(projectId)
                                    .getDisplayName();
    }

    private void transferFileToClient(@Nonnull ProjectId projectId,
                                      @Nonnull UserId userId,
                                      @Nonnull RevisionNumber revisionNumber,
                                      @Nonnull DownloadFormat downloadFormat,
                                      @Nonnull Path downloadSource,
                                      @Nonnull HttpServletResponse response) {

        String fileName = getClientSideFileName(projectId, revisionNumber, downloadFormat);
        FileTransferTask task = new FileTransferTask(projectId,
                                                     userId,
                                                     downloadSource,
                                                     fileName,
                                                     response);
        Future<?> transferFuture = fileTransferExecutor.submit(task);
        try {
            transferFuture.get();
        } catch (InterruptedException e) {
            logger.info("{} {} The download of this project was interrupted.", projectId, userId);
        } catch (ExecutionException e) {
            logger.info("{} {} An execution exception occurred whilst transferring the project.  Cause: {}",
                        projectId,
                        userId,
                        Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse(""),
                        e.getCause());
        }
    }

    private String getClientSideFileName(ProjectId projectId, RevisionNumber revision, DownloadFormat downloadFormat) {
        String revisionNumberSuffix;
        if (revision.isHead()) {
            revisionNumberSuffix = "";
        }
        else {
            revisionNumberSuffix = "-REVISION-" + Long.toString(revision.getValue());
        }
        String projectDisplayName = projectDetailsManager.getProjectDetails(projectId).getDisplayName();
        String fileName = projectDisplayName.replaceAll("\\s+", "-")
                + revisionNumberSuffix
                + "-ontologies."
                + downloadFormat.getExtension()
                + ".zip";
        return fileName.toLowerCase();
    }

    /**
     * Shuts down this {@link ProjectDownloadService}.
     */
    public void shutDown() {
        logger.info("Shutting down Project Download Service");
        downloadGeneratorExecutor.shutdown();
        fileTransferExecutor.shutdown();
    }

    private RevisionNumber getHeadRevisionNumber(@Nonnull ProjectId projectId, @Nonnull UserId userId) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RevisionNumber headRevisionNumber = headRevisionNumberFinder.getHeadRevisionNumber(projectId);
        logger.info("{} {} Computed head revision number ({}) in {} ms",
                    projectId,
                    userId,
                    headRevisionNumber,
                    stopwatch.elapsed(MILLISECONDS));
        return headRevisionNumber;

    }

}
