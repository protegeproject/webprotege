package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;
import edu.stanford.bmir.protege.web.shared.csv.GetCSVGridAction;
import edu.stanford.bmir.protege.web.shared.csv.GetCSVGridResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVImportDialogController extends WebProtegeOKCancelDialogController<CSVImportDescriptor> {

    private static final int ROW_LIMIT = 50;

    private CSVImportViewImpl csvImportView;

    private DocumentId csvDocumentId;

    public CSVImportDialogController(DocumentId documentId) {
        super("Import CSV File");
        this.csvDocumentId = documentId;
        csvImportView = new CSVImportViewImpl();

        DispatchServiceManager.get().execute(new GetCSVGridAction(documentId, ROW_LIMIT), new AsyncCallback<GetCSVGridResult>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(GetCSVGridResult result) {
                csvImportView.setCSVGrid(result.getCSVGrid());
            }
        });
    }

    @Override
    public Widget getWidget() {
        return csvImportView;
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.absent();
    }

    @Override
    public CSVImportDescriptor getData() {
        return csvImportView.getImportDescriptor().get();
    }
}
