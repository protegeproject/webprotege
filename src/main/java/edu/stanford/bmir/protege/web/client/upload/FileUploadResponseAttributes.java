package edu.stanford.bmir.protege.web.client.upload;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2012
 * <p>
 *     A list of attributes that can be returned from a file upload.
 * </p>
 */
public enum FileUploadResponseAttributes {

    /**
     * The attribute name that describes whether the upload was accepted or rejected.
     */
    RESPONSE_TYPE_ATTRIBUTE,

    /**
     * The value of the {@link #RESPONSE_TYPE_ATTRIBUTE} which indicates the upload was accepted.
     */
    RESPONSE_TYPE_VALUE_UPLOAD_ACCEPTED,

    /**
     * The value of the {@link #RESPONSE_TYPE_ATTRIBUTE} which indicates the upload was rejected.
     */
    RESPONSE_TYPE_VALUE_UPLOAD_REJECTED,


    /**
     * The attribute name that points the fileId of an accepted upload.
     */
    UPLOAD_FILE_ID,

    /**
     * The attribute name that points to a message that describes why the upload was rejected.
     */
    UPLOAD_REJECTED_MESSAGE_ATTRIBUTE

    
}
