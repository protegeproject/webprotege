package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
@ApplicationSingleton
public class ProjectDownloadService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDownloadService.class);

    @Nonnull
    private final ExecutorService executorService;

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final ApplicationNameSupplier applicationNameSupplier;

    @Inject
    public ProjectDownloadService(@Nonnull @DownloadExecutor ExecutorService executorService,
                                  @Nonnull ProjectManager projectManager,
                                  @Nonnull ProjectDetailsManager projectDetailsManager,
                                  @Nonnull ApplicationNameSupplier applicationNameSupplier) {
        this.executorService = checkNotNull(executorService);
        this.projectManager = checkNotNull(projectManager);
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
        this.applicationNameSupplier = checkNotNull(applicationNameSupplier);
    }

    public void downloadProject(@Nonnull UserId requester,
                                @Nonnull ProjectId projectId,
                                @Nonnull RevisionNumber revisionNumber,
                                @Nonnull DownloadFormat downloadFormat,
                                @Nonnull HttpServletResponse response) {
        DownloadTask task = new DownloadTask(
                projectManager,
                projectId,
                requester,
                projectDetailsManager.getProjectDetails(projectId).getDisplayName(),
                revisionNumber,
                downloadFormat,
                applicationNameSupplier.get(),
                response
        );
        logger.info("{} Submitted download request to download queue", projectId);
        Future<?> future = executorService.submit(task);
        try {
            future.get();
        } catch (InterruptedException e) {
            logger.info("{} The download of this project was interrupted.", projectId);
        } catch (ExecutionException e) {
            logger.info("{} An execution exception occurred whilst downloading the project.  Cause: {}",
                        projectId,
                        Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse(""),
                        e.getCause());
        }
    }

    /**
     * Shuts down this {@link ProjectDownloadService}.
     */
    public void shutDown() {
        logger.info("Shutting down Project Download Service");
        executorService.shutdown();
    }

    private static class DownloadTask implements Callable<Void> {

        @Nonnull
        private final ProjectManager projectManager;

        @Nonnull
        private final ProjectId projectId;

        @Nonnull
        private final UserId userId;

        @Nonnull
        private final String displayName;

        @Nonnull
        private final RevisionNumber revisionNumber;

        @Nonnull
        private final DownloadFormat format;

        @Nonnull
        private final String applicationName;

        @Nonnull
        private final HttpServletResponse response;

        public DownloadTask(@Nonnull ProjectManager projectManager,
                            @Nonnull ProjectId projectId,
                            @Nonnull UserId userId,
                            @Nonnull String displayName,
                            @Nonnull RevisionNumber revisionNumber,
                            @Nonnull DownloadFormat format,
                            @Nonnull String applicationName,
                            @Nonnull HttpServletResponse response) {
            this.projectManager = projectManager;
            this.projectId = projectId;
            this.userId = userId;
            this.displayName = displayName;
            this.revisionNumber = revisionNumber;
            this.format = format;
            this.applicationName = applicationName;
            this.response = response;
        }

        @Override
        public Void call() throws Exception {
            logger.info("{} Starting project download", projectId);
            MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
            memoryMonitor.monitorMemoryUsage();
            Project project = projectManager.getProject(projectId, userId);
            memoryMonitor.monitorMemoryUsage();
            ProjectDownloader downloader = new ProjectDownloader(displayName,
                                                                 project,
                                                                 revisionNumber,
                                                                 format,
                                                                 applicationName);
            logger.info("{} Sending download to client", projectId);
            downloader.writeProject(response);
            logger.info("{} Finished download", projectId);
            memoryMonitor.monitorMemoryUsage();
            return null;
        }
    }

}
