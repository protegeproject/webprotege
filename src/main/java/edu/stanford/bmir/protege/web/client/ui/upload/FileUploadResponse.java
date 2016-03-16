package edu.stanford.bmir.protege.web.client.ui.upload;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.rpc.data.FileUploadResponseAttributes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class FileUploadResponse {

    public static final String UNKNOWN_REASON_MESSAGE = "Unknown reason";

    private String rawResult;

    private JSONObject jsonObject;

    public FileUploadResponse(String rawResult) {
        this.rawResult = rawResult;
        jsonObject = parseResult();
    }

    private JSONObject parseResult() {
        JSONValue value = JSONParser.parseLenient(trimPreTags(rawResult));
        JSONObject object = value.isObject();
        return object;
    }

    /**
     * The result from a form submission sometimes seems to be surrounded with &lt;pre&gt; tags.  This method
     * strips these tags of the result if the are present.
     * @param result The raw result.
     * @return The raw result minus the pre tags.
     */
    private String trimPreTags(String result) {
        if(result.startsWith("<pre>")) {
            return result.substring(5, result.length() - 6);
        }
        else {
            return result;
        }
    }

    /**
     * If the upload was rejected the method gets an error message which explains why the upload was rejected.
     * @return A string representing the message (not <code>null</code>).
     */
    public String getUploadRejectedMessage() {
        if(jsonObject == null) {
            return "Invalid response";
        }
        JSONValue value = jsonObject.get(FileUploadResponseAttributes.UPLOAD_REJECTED_MESSAGE_ATTRIBUTE.name());
        if(value == null) {
            return UNKNOWN_REASON_MESSAGE;
        }
        JSONString string = value.isString();
        if(string == null) {
            return UNKNOWN_REASON_MESSAGE;
        }
        return string.stringValue();
    }

    /**
     * Determines if the upload was accepted.
     * @return <code>true</code> if the upload was accepted, otherwise <code>false</code>.
     */
    public boolean wasUploadAccepted() {
        if(jsonObject == null) {
            return false;
        }
        JSONValue value = jsonObject.get(FileUploadResponseAttributes.RESPONSE_TYPE_ATTRIBUTE.name());
        if(value == null) {
            return false;
        }
        JSONString string = value.isString();
        if(string == null) {
            return false;
        }
        return string.stringValue().equals(FileUploadResponseAttributes.RESPONSE_TYPE_VALUE_UPLOAD_ACCEPTED.name());
    }


    /**
     * For an upload that was accepted ({@link #wasUploadAccepted()} returns <code>true</code>) this method gets
     * the documentId of the upload.
     * @return The document Id
     */
    public DocumentId getDocumentId() {
        if(jsonObject == null) {
            return new DocumentId("");
        }
        if(!wasUploadAccepted()) {
            return new DocumentId("");
        }
        JSONValue value = jsonObject.get(FileUploadResponseAttributes.UPLOAD_FILE_ID.name());
        if(value == null) {
            return new DocumentId("");
        }
        JSONString string = value.isString();
        if(string == null) {
            return new DocumentId("");
        }
        return new DocumentId(string.stringValue().trim());
    }

}
