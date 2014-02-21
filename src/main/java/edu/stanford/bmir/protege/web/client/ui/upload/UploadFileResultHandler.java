package edu.stanford.bmir.protege.web.client.ui.upload;

import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public interface UploadFileResultHandler {

    void handleFileUploaded(DocumentId fileDocumentId);

    void handleFileUploadFailed(String errorMessage);
}
