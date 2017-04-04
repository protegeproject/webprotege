package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.csv.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVImportDialogController extends WebProtegeOKCancelDialogController<CSVImportDescriptor> {

    private static final int ROW_LIMIT = 50;

    private final DispatchServiceManager dispatchServiceManager;

    private CSVImportViewImpl csvImportView;

    private ProjectId projectId;

    private DocumentId csvDocumentId;

    private OWLClass importRoot;

    public CSVImportDialogController(ProjectId projId, DocumentId documentId, OWLClass importRootClass, DispatchServiceManager manager, final CSVImportViewImpl csvImportView) {
        super("Import CSV File");
        this.projectId = projId;
        this.csvDocumentId = documentId;
        this.importRoot = importRootClass;
        this.csvImportView = csvImportView;
        this.dispatchServiceManager = manager;

        dispatchServiceManager.execute(new GetCSVGridAction(documentId, ROW_LIMIT), new DispatchServiceCallback<GetCSVGridResult>() {
            @Override
            public void handleSuccess(GetCSVGridResult result) {
                csvImportView.setCSVGrid(result.getCSVGrid());
            }
        });

        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<CSVImportDescriptor>() {
            @Override
            public void handleHide(CSVImportDescriptor data, WebProtegeDialogCloser closer) {
                dispatchServiceManager.execute(new ImportCSVFileAction(projectId, csvDocumentId, importRoot, data), new DispatchServiceCallbackWithProgressDisplay<ImportCSVFileResult>() {
                    @Override
                    protected String getErrorMessage(Throwable throwable) {
                        return "There was a problem importing the csv file.  Please try again.";
                    }

                    @Override
                    public void handleSuccess(ImportCSVFileResult result) {
                        MessageBox.showMessage("CSV import succeeded", result.getRowCount() + " rows were imported");
                    }

                    @Override
                    public String getProgressDisplayTitle() {
                        return "Importing CSV file";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Please wait.";
                    }
                });
                closer.hide();
            }
        });
    }

    @Override
    public Widget getWidget() {
        return csvImportView;
    }

    @Nonnull
    @Override
    public java.util.Optional<Focusable> getInitialFocusable() {
        return java.util.Optional.empty();
    }

    @Override
    public CSVImportDescriptor getData() {
        return csvImportView.getImportDescriptor().get();
    }
}
