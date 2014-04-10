package edu.stanford.bmir.protege.web.server.logging;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2013
 */
public class WebProtegeLoggerEx {

    private WebProtegeLogger logger;

    public WebProtegeLoggerEx(WebProtegeLogger logger) {
        this.logger = logger;
    }

    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemoryBytes = runtime.maxMemory();
        long freeMemoryBytes = runtime.freeMemory();
        long totalMemoryBytes = runtime.totalMemory();
        long usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;
        long availableMemoryMB = toMB(maxMemoryBytes - usedMemoryBytes);
        long usedMemoryMB = toMB(usedMemoryBytes);
        long maxMemoryMB = toMB(maxMemoryBytes);
        double percentageUsed = (100.0 * usedMemoryBytes) / maxMemoryBytes;
        logger.info("Memory Usage: Using %d MB of %d MB (%.2f%%) [%d MB free]", usedMemoryMB, maxMemoryMB, percentageUsed, availableMemoryMB);
    }

    private long toMB(long bytes) {
        return bytes / (1024 * 1024);
    }
}
