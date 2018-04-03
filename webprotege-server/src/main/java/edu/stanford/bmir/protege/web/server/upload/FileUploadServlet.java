package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.server.util.FileContentsSizeCalculator;
import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.upload.FileUploadResponseAttributes;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.logging.RequestFormatter.formatAddr;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 * <p>
 * A servlet for uploading files to web protege.
 * </p>
 * <p>
 *     If the upload succeeds then the server returns an HTTP 201 response (indicating that a new resource was created
 *     on the server) and the body of the response consists of the name of the file resource.  The upload will fail if
 *     the content encoding is not multi-part. In this case, the server will return an HTTP 400 response code
 *     (indicating that the request was not well formed).  If the file could not be created on the server for what ever
 *     reason, the server will return an HTTP 500 response code (internal server error) and the body of the response
 *     will contain an message that describes the problem.
 * </p>
 */
@ApplicationSingleton
public class FileUploadServlet extends HttpServlet {

    public static final Logger logger = LoggerFactory.getLogger(FileUploadServlet.class);

    public static final String TEMP_FILE_PREFIX = "upload-";

    public static final String TEMP_FILE_SUFFIX = "";

    public static final String RESPONSE_MIME_TYPE = "text/html";

    @UploadsDirectory
    @Nonnull
    private final File uploadsDirectory;

    private final AccessManager accessManager;

    private final ApplicationNameSupplier applicationNameSupplier;

    private final MaxUploadSizeSupplier maxUploadSizeSupplier;

    @Inject
    public FileUploadServlet(
            @Nonnull AccessManager accessManager,
            @Nonnull ApplicationNameSupplier applicationNameSupplier,
            @Nonnull MaxUploadSizeSupplier maxUploadSizeSupplier,
            @Nonnull @UploadsDirectory File uploadsDirectory) {
        this.accessManager = checkNotNull(accessManager);
        this.applicationNameSupplier = checkNotNull(applicationNameSupplier);
        this.maxUploadSizeSupplier = checkNotNull(maxUploadSizeSupplier);
        this.uploadsDirectory = checkNotNull(uploadsDirectory);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(req.getSession());
        UserId userId = webProtegeSession.getUserInSession();
        if(!accessManager.hasPermission(Subject.forUser(userId),
                                    ApplicationResource.get(),
                                    BuiltInAction.UPLOAD_PROJECT)) {
            sendErrorMessage(resp, "You do not have permission to upload files to " + applicationNameSupplier.get());
        }

        logger.info("Received upload request from {} at {}",
                    webProtegeSession.getUserInSession(),
                    formatAddr(req));
        resp.setHeader("Content-Type", RESPONSE_MIME_TYPE);
        try {
            if (ServletFileUpload.isMultipartContent(req)) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(maxUploadSizeSupplier.get());
                List<FileItem> items = upload.parseRequest(req);

                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        File uploadedFile = createServerSideFile();
                        item.write(uploadedFile);
                        long sizeInBytes = uploadedFile.length();
                        long computedFileSizeInBytes = computeFileSize(uploadedFile);
                        logger.info("File size is {} bytes.  Computed file size is {} bytes.", sizeInBytes, computedFileSizeInBytes);
                        if(computedFileSizeInBytes > maxUploadSizeSupplier.get()) {
                            sendFileSizeTooLargeResponse(resp);
                        }
                        else {
                            logger.info("Stored uploaded file with name {}", uploadedFile.getName());
                            resp.setStatus(HttpServletResponse.SC_CREATED);
                            sendSuccessMessage(resp, uploadedFile.getName());
                        }
                        return;
                    }
                }
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not find form file item");
            }
            else {
                logger.info("Bad upload request: POST must be multipart encoding.");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "POST must be multipart encoding.");
            }

        }
        catch (FileUploadBase.FileSizeLimitExceededException | FileUploadBase.SizeLimitExceededException e) {
            sendFileSizeTooLargeResponse(resp);
        }
        catch (FileUploadBase.FileUploadIOException | FileUploadBase.IOFileUploadException e) {
            logger.info("File upload failed because an IOException occurred: {}", e.getMessage(), e);
            sendErrorMessage(resp, "File upload failed because of an IOException");
        }
        catch (FileUploadBase.InvalidContentTypeException e) {
            logger.info("File upload failed because the content type was invalid: {}", e.getMessage());
            sendErrorMessage(resp, "File upload failed because the content type is invalid");
        }
        catch (FileUploadException e) {
            logger.info("File upload failed: {}", e.getMessage());
            sendErrorMessage(resp, "File upload failed");
        } catch (Exception e) {
            logger.info("File upload failed because of an error when trying to write the file item: {}", e.getMessage(), e);
            sendErrorMessage(resp, "File upload failed");
        }
    }

    private void sendFileSizeTooLargeResponse(HttpServletResponse resp) throws IOException {
        logger.info("File upload failed because the file exceeds the maximum allowed size.");
        sendErrorMessage(resp, String.format("The file that you attempted to upload is too large.  " +
                                                     "Files (or zipped file contents) must not exceed %d MB.",
                                             maxUploadSizeSupplier.get() / (1024 * 1024)));
    }

    private void sendSuccessMessage(HttpServletResponse response, String fileName) throws IOException {
        PrintWriter writer = response.getWriter();
        writeJSONPairs(writer,
                new Pair(FileUploadResponseAttributes.RESPONSE_TYPE_ATTRIBUTE.name(), FileUploadResponseAttributes.RESPONSE_TYPE_VALUE_UPLOAD_ACCEPTED.name()),
                new Pair(FileUploadResponseAttributes.UPLOAD_FILE_ID.name(), fileName));

    }

    private void sendErrorMessage(HttpServletResponse response, String errorMessage) throws IOException {
        writeJSONPairs(response.getWriter(), 
                new Pair(FileUploadResponseAttributes.RESPONSE_TYPE_ATTRIBUTE.name(), FileUploadResponseAttributes.RESPONSE_TYPE_VALUE_UPLOAD_REJECTED.name()),
                new Pair(FileUploadResponseAttributes.UPLOAD_REJECTED_MESSAGE_ATTRIBUTE.name(), errorMessage)
        );

    }
    
    private void writeJSONPairs(PrintWriter printWriter, Pair ... pairs) {
        printWriter.println("{");
        for(Iterator<Pair> it = Arrays.asList(pairs).iterator(); it.hasNext(); ) {
            Pair pair = it.next();
            String string = pair.getString();
            writeString(printWriter, string);
            printWriter.print(" : ");
            writeString(printWriter, pair.getValue());
            if(it.hasNext()) {
                printWriter.println(",");
            }
            else {
                printWriter.println();
            }
        }
        printWriter.println("}");
        printWriter.flush();
    }

    private void writeString(PrintWriter printWriter, String string) {
        printWriter.print("\"");
        printWriter.print(string);
        printWriter.print("\"");
    }

    /**
     * Creates a fresh file on the server.
     * @return The file.
     * @throws IOException If there was a problem creating the file.
     */
    private File createServerSideFile() throws IOException {
        uploadsDirectory.mkdirs();
        return File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, uploadsDirectory);
    }

    private long computeFileSize(@Nonnull File uploadedFile) throws IOException {
        FileContentsSizeCalculator sizeCalculator = new FileContentsSizeCalculator(new ZipInputStreamChecker());
        return sizeCalculator.getContentsSize(uploadedFile);
    }
    
    private static class Pair {
        
        private String string;
        
        private String value;

        private Pair(String string, String value) {
            this.string = string;
            this.value = value;
        }

        public String getString() {
            return string;
        }

        public String getValue() {
            return value;
        }
    }

}
