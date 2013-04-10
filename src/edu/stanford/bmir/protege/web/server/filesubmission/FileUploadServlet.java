package edu.stanford.bmir.protege.web.server.filesubmission;

import edu.stanford.bmir.protege.web.client.rpc.data.FileUploadResponseAttributes;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnloadableImportException;

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

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 * <p>
 * A servlet for uploading files to web protege.  The servlet as a very weak form of security in that it only
 * accepts files from registered users.
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
public class FileUploadServlet extends HttpServlet {

    public static final String TEMP_FILE_PREFIX = "upload-";

    public static final String TEMP_FILE_SUFFIX = "";

    public static final long DEFAULT_MAX_FILE_SIZE = Long.MAX_VALUE;

    public static final String RESPONSE_MIME_TYPE = "text/html";

    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(FileUploadServlet.class);


    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Received upload...");
        resp.setHeader("Content-Type", RESPONSE_MIME_TYPE);
        try {
            if (ServletFileUpload.isMultipartContent(req)) {

                LOGGER.info("POST is multipart (OK)");

                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(DEFAULT_MAX_FILE_SIZE);

                List<FileItem> items = upload.parseRequest(req);

                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        File uploadedFile = createServerSideFile();
                        LOGGER.info("Created server side file: " + uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);
                        resp.setStatus(HttpServletResponse.SC_CREATED);
                        try {
                            processOntology(uploadedFile);
                            LOGGER.info("Sending response to client");
                            sendSuccessMessage(resp, uploadedFile.getName());
                        }
                        catch (OWLOntologyCreationException e) {
                            LOGGER.info("Caught OWLOntologyCreationException %s", e.toString());
                            sendErrorMessage(resp, e);
                        }
                        return;
                    }
                }
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not find form file item");
            }
            else {
                LOGGER.info("BAD REQUEST: POST must be multipart encoding.");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "POST must be multipart encoding.");
            }

        }
        catch (Throwable e) {
            LOGGER.severe(e);
            sendErrorMessage(resp, e);
        }
    }
    
    private void sendSuccessMessage(HttpServletResponse response, String fileName) throws IOException {
        PrintWriter writer = response.getWriter();
        writeJSONPairs(writer,
                new Pair(FileUploadResponseAttributes.RESPONSE_TYPE_ATTRIBUTE.name(), FileUploadResponseAttributes.RESPONSE_TYPE_VALUE_UPLOAD_ACCEPTED.name()),
                new Pair(FileUploadResponseAttributes.UPLOAD_FILE_ID.name(), fileName));

    }

    private void sendErrorMessage(HttpServletResponse response, Throwable exception) throws IOException {
        String errorMessage;
        if(exception instanceof OWLOntologyCreationException) {
            errorMessage = getErrorMessage((OWLOntologyCreationException) exception);
        }
        else {
            errorMessage = exception.getMessage();
        }
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


    private static String getErrorMessage(OWLOntologyCreationException e) {
        if(e instanceof UnparsableOntologyException) {
            UnparsableOntologyException unparsableOntologyException = (UnparsableOntologyException) e;
            return "Could not parse ontology. Please load your ontology document into Protege to check that it is well formed.";
        }
        else if(e instanceof UnloadableImportException) {
            UnloadableImportException importException = (UnloadableImportException) e;
            return "Could not load imported ontology: " + importException.getImportsDeclaration().getIRI();
        }
        else if(e instanceof OWLOntologyCreationIOException) {
            OWLOntologyCreationIOException ioException = (OWLOntologyCreationIOException) e;
            return "Problem reading ontology document: " + ioException.getCause().getMessage();
        }
        return e.getMessage();
    }

    /**
     * Creates a fresh file on the server.
     * @return The file.
     * @throws IOException If there was a problem creating the file.
     */
    private File createServerSideFile() throws IOException {
        FileUploadConstants.UPLOADS_DIRECTORY.mkdirs();
        File tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, FileUploadConstants.UPLOADS_DIRECTORY);
        LOGGER.info("Created temp file: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    
    
    private void processOntology(File ontologyDocument) throws OWLOntologyCreationException {
        LOGGER.info("Parsing uploaded file ontology...");
        OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
        manager.loadOntologyFromOntologyDocument(ontologyDocument);
        LOGGER.info("    .... parsed");
    }
    
    
    private class Pair {
        
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
