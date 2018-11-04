package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.app.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Nov 2018
 */
@ApplicationSingleton
public class ProjectCacheManager implements HasDispose {

    private static final Logger logger = LoggerFactory.getLogger(ProjectCacheManager.class);

    private static final long PROJECT_PURGE_CHECK_INTERVAL_MS = 30_000;

    private final ScheduledExecutorService purgeService;

    private final ProjectCache projectCache;

    @Nonnull
    private final DisposableObjectManager disposableObjectManager;

    @Inject
    public ProjectCacheManager(@Nonnull ProjectCache projectCache,
                               @Nonnull DisposableObjectManager disposableObjectManager) {
        this.projectCache = checkNotNull(projectCache);
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
        this.purgeService = Executors.newSingleThreadScheduledExecutor(r -> new Thread("Project purge service"));
    }

    public void start() {
        disposableObjectManager.register(this);
        purgeService.scheduleAtFixedRate(projectCache::purgeDormantProjects,
                                         0,
                                         PROJECT_PURGE_CHECK_INTERVAL_MS,
                                         TimeUnit.MILLISECONDS);
        logger.info("Started project purge service with check interval of {} ms", PROJECT_PURGE_CHECK_INTERVAL_MS);
    }

    public void dispose() {
        logger.info("Shutting down project purge service");
        purgeService.shutdown();
        logger.info("Project purge service had been shut down");
    }
}
