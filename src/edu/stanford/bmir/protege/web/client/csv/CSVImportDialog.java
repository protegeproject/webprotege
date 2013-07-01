package edu.stanford.bmir.protege.web.client.csv;

import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogController;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVImportDialog extends WebProtegeDialog<CSVImportDescriptor> {

    public CSVImportDialog(DocumentId csvDocumentId) {
        super(new CSVImportDialogController(csvDocumentId));
    }
}
