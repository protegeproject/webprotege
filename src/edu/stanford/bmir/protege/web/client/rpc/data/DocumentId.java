package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 * <p>
 *     Identifies a document that exists on a WebProtege server.
 * </p>
 */
public class DocumentId implements Serializable {

    private String documentId;



    /**
     * Constructs an ServerFileId.
     * @param documentId A string that identifies a document on the server.  This string just acts as a "handle" to a file - it does
     * not reveal location specific information.  Not <code>null</code>.
     * @throws NullPointerException is documentId is <code>null</code>.
     */
    public DocumentId(String documentId) {
        if(documentId == null) {
            throw new NullPointerException("documentId must not be null");
        }
        this.documentId = documentId;
    }

    /**
     * Serialization
     */
    private DocumentId() {
    }

    /**
     * Gets the document id.
     * @return A string that identifies the document.  This string just acts as a "handle" to a document - it does
     * not reveal location specific information (assuming the document actually exists as a file on the server).
     */
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public int hashCode() {
        return documentId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DocumentId)) {
            return false;
        }
        DocumentId other = (DocumentId) obj;
        return other.documentId.equals(this.documentId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DocumentId(");
        sb.append(documentId);
        sb.append(")");
        return sb.toString();
    }
}
