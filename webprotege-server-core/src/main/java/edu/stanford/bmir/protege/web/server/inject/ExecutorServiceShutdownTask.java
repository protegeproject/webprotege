package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.shared.HasDispose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-21
 */
public class ExecutorServiceShutdownTask implements HasDispose {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceShutdownTask.class);

    private static final int SHUTDOWN_TIMEOUT = 5;

    private static final TimeUnit SHUTDOWN_TIMEOUT_UNIT = TimeUnit.MINUTES;

    @Nonnull
    private final ExecutorService executorService;

    @Nonnull
    private final String serviceName;

    @Inject
    public ExecutorServiceShutdownTask(@Nonnull ExecutorService executorService,
                                       @Nonnull String serviceName) {
        this.executorService = checkNotNull(executorService);
        this.serviceName = checkNotNull(serviceName);
    }

    /**
     * Exposed for testing purposes
     */
    @Nonnull
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void dispose() {
        executorService.shutdown();
        try {
            logger.info("Shutting down {} ...", serviceName);
            executorService.awaitTermination(SHUTDOWN_TIMEOUT, SHUTDOWN_TIMEOUT_UNIT);
            logger.info("    ... {} shutdown complete", serviceName);

        } catch (InterruptedException e) {
            logger.error("Could not shutdown {} within {} {}", SHUTDOWN_TIMEOUT, SHUTDOWN_TIMEOUT_UNIT);
        }

    }
}
