package edu.stanford.bmir.protege.web.server.util;

import org.slf4j.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2017
 */
public class MemoryMonitor {

    public final static long WARNING_THRESHOLD = 500 * 1024 * 1024;

    private final Logger logger;

    private long maxMemoryBytes;

    private long freeMemoryBytes;

    private long usedMemoryBytes;

    private long remainingMemoryBytes;

    private double percentageUsed;


    public MemoryMonitor(Logger logger) {
        this.logger = logger;
    }

    private void update() {
        Runtime runtime = Runtime.getRuntime();
        maxMemoryBytes = runtime.maxMemory();
        freeMemoryBytes = runtime.freeMemory();
        long totalMemoryBytes = runtime.totalMemory();
        usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;
        remainingMemoryBytes = maxMemoryBytes - usedMemoryBytes;
        percentageUsed = (100.0 * usedMemoryBytes) / maxMemoryBytes;
    }

    /**
     * Checks the current memory usage and if free memory is below the value specified by {@link #WARNING_THRESHOLD}
     * an warning is logged.
     */
    public void monitorMemoryUsage() {
        update();
        if(freeMemoryBytes < WARNING_THRESHOLD) {
            logger.warn("Low Memory: Using %d MB of %d MB (%.2f%%) [%d MB free]",
                        toMB(usedMemoryBytes), toMB(maxMemoryBytes), percentageUsed, toMB(remainingMemoryBytes));
        }
    }

    /**
     * Logs the current memory usage.
     */
    public void logMemoryUsage() {
        update();
        logger.warn("Memory Usage: Using %d MB of %d MB (%.2f%%) [%d MB free]",
                    toMB(usedMemoryBytes), toMB(maxMemoryBytes), percentageUsed, toMB(remainingMemoryBytes));
    }

    private static long toMB(long bytes) {
        return bytes / (1024 * 1024);
    }
}
