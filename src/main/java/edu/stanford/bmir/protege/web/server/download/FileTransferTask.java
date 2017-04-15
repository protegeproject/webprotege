package edu.stanford.bmir.protege.web.server.download;

import com.google.common.base.Stopwatch;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 *
 * A small task that sends a file to the browser/client.
 */
class FileTransferTask implements Callable<Void> {

    private static Logger logger = LoggerFactory.getLogger(FileTransferTask.class);

    private static final String MIME_TYPE = "application/zip";

    private static final String CONTENT_DISPOSITION_HEADER_FIELD = "Content-Disposition";


    private final Path downloadSource;

    private final HttpServletResponse response;

    private final String fileName;

    /**
     * Creates a {@link FileTransferTask} to transfer the specified file.
     * @param fileToTransfer The file to transfer.
     * @param clientSideFileName The name of the file that should be created on the client side.
     * @param response The {@link HttpServletResponse} that should be used to send the file.
     */
    public FileTransferTask(@Nonnull Path fileToTransfer,
                            @Nonnull String clientSideFileName,
                            @Nonnull HttpServletResponse response) {
        this.fileName = checkNotNull(clientSideFileName);
        this.downloadSource = checkNotNull(fileToTransfer);
        this.response = checkNotNull(response);
    }

    @Override
    public Void call() throws Exception {
        sendFileToClient();
        return null;
    }

    /**
     * Sends the file to the client after setting the mime type, name and content length.
     */
    private void sendFileToClient() throws IOException {
        setFileType();
        setFileName();
        setContentLength();
        streamFileToClient();
    }


    /**
     * Sets the file type (mime type).  Must be called before the file is streamed to the client with
     * {@link #streamFileToClient()}.
     */
    private void setFileType() {
        response.setContentType(MIME_TYPE);
    }

    /**
     * Sets the file name.  Must be called before the file is streamed to the client with
     * {@link #streamFileToClient()}.
     */
    private void setFileName() {
        response.setHeader(CONTENT_DISPOSITION_HEADER_FIELD, "attachment; filename=\"" + fileName + "\"");
    }

    /**
     * Sets the content length.  Must be called before the file is streamed to the
     * client with {@link #streamFileToClient()}.
     */
    private void setContentLength() throws IOException {
        response.setContentLength((int) Files.size(downloadSource));
    }


    private void streamFileToClient() throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(downloadSource))) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
                double sizeMB = Files.size(downloadSource) / (1024.0 * 1024);
                logger.info("Transferring {} MB download to client", String.format("%.2f", sizeMB));

                Stopwatch stopwatch = Stopwatch.createStarted();
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();

                logger.info("Finished transferring {} MB to client after {} ms", sizeMB, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }
        }
    }
}
