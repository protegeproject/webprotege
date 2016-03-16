package edu.stanford.bmir.protege.web.shared.csv;

import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2013
 */
public class GetCSVGridAction implements Action<GetCSVGridResult> {

    private DocumentId csvDocumentId;

    private int rowLimit;

    /**
     * For serialization purposes only
     */
    private GetCSVGridAction() {
    }

    /**
     * Constructs a CSVGrid action.
     * @param csvDocumentId The document id that identifies the csv document to retrieve.  Not {@code null}.
     * @param rowLimit The maximum number of rows to read from the document. The value must be greater than zero.
     * @throws NullPointerException if {@code csvDocumentId} is {@code null}.
     * @throws IllegalArgumentException if {@code rowLimit} is not greater than zero.
     */
    public GetCSVGridAction(DocumentId csvDocumentId, int rowLimit) {
        this.csvDocumentId = checkNotNull(csvDocumentId);
        checkArgument(rowLimit > 0);
        this.rowLimit = rowLimit;
    }

    /**
     * Gets the {@link DocumentId} of the CSV document to read.
     * @return The document id.  Not {@code null}.
     */
    public DocumentId getCSVDocumentId() {
        return csvDocumentId;
    }

    /**
     * Gets the row limit.
     * @return The row limit which will be greater than zero.
     */
    public int getRowLimit() {
        return rowLimit;
    }
}
