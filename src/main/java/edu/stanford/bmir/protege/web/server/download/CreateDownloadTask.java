package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.util.MemoryMonitor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
class CreateDownloadTask implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(CreateDownloadTask.class);

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UserId userId;

    @Nonnull
    private final String projectDisplayName;

    @Nonnull
    private final RevisionNumber revisionNumber;

    @Nonnull
    private final DownloadFormat format;

    @Nonnull
    private final String applicationName;

    @Nonnull
    private final Path downloadPath;

    public CreateDownloadTask(@Nonnull ProjectManager projectManager,
                              @Nonnull ProjectId projectId,
                              @Nonnull UserId userId,
                              @Nonnull String projectDisplayName,
                              @Nonnull RevisionNumber revisionNumber,
                              @Nonnull DownloadFormat format,
                              @Nonnull String applicationName,
                              @Nonnull Path destinationPath) {
        this.projectManager = projectManager;
        this.projectId = projectId;
        this.userId = userId;
        this.projectDisplayName = projectDisplayName;
        this.revisionNumber = revisionNumber;
        this.format = format;
        this.applicationName = applicationName;
        this.downloadPath = destinationPath;
    }

    @Override
    public Void call() throws Exception {
        if(Files.exists(downloadPath)) {
            logger.info("{} {} Project download already exists.  Not recreating download. ({})",
                        projectId,
                        userId,
                        downloadPath.toAbsolutePath());
            return null;
        }
        logger.info("{} {} Creating project download", projectId, userId);
        MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.monitorMemoryUsage();
        Project project = projectManager.getProject(projectId, userId);
        memoryMonitor.monitorMemoryUsage();
        ProjectDownloader downloader = new ProjectDownloader(projectDisplayName,
                                                             project,
                                                             revisionNumber,
                                                             format,
                                                             applicationName);
        logger.info("{} {} Writing download to file: {}", projectId, userId, downloadPath);
        Files.createDirectories(downloadPath.getParent());
        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(downloadPath))) {
            downloader.writeProject(outputStream);
        }
        double sizeInMB = Files.size(downloadPath) / (1024.0 * 1024);
        logger.info("{} {} Finished creating download ({} MB)", projectId, userId, String.format("%.4f", sizeInMB));
        memoryMonitor.monitorMemoryUsage();
        return null;
    }
}
