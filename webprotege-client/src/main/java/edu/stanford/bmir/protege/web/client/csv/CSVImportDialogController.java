package edu.stanford.bmir.protege.web.client.csv;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.csv.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVImportDialogController extends WebProtegeOKCancelDialogController<CSVImportDescriptor> {

    private static final int ROW_LIMIT = 50;

    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    private CSVImportViewImpl csvImportView;

    private ProjectId projectId;

    private DocumentId csvDocumentId;

    private OWLClass importRoot;

    private MessageBox messageBox;

    @AutoFactory
    @Inject
    public CSVImportDialogController(@Provided ProjectId projId,
                                     DocumentId documentId,
                                     OWLClass importRootClass,
                                     @Provided DispatchServiceManager manager,
                                     @Provided@Nonnull DispatchErrorMessageDisplay errorDisplay,
                                     @Provided @Nonnull ProgressDisplay progressDisplay,
                                     @Provided final CSVImportViewImpl csvImportView,
                                     @Provided MessageBox messageBox) {
        super("Import CSV File");
        this.projectId = projId;
        this.csvDocumentId = documentId;
        this.importRoot = importRootClass;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
        this.csvImportView = csvImportView;
        this.dispatchServiceManager = manager;
        this.messageBox = messageBox;

        dispatchServiceManager.execute(new GetCSVGridAction(documentId, ROW_LIMIT), result -> {
            csvImportView.setCSVGrid(result.getCSVGrid());
        });

        setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            dispatchServiceManager.execute(new ImportCSVFileAction(projectId, csvDocumentId, importRoot, data), new DispatchServiceCallbackWithProgressDisplay<ImportCSVFileResult>(CSVImportDialogController.this.errorDisplay, CSVImportDialogController.this.progressDisplay) {
                @Override
                protected String getErrorMessage(Throwable throwable) {
                    return "There was a problem importing the csv file.  Please try again.";
                }

                @Override
                public void handleSuccess(ImportCSVFileResult result) {
                    CSVImportDialogController.this.messageBox.showMessage("CSV import succeeded", result.getRowCount() + " rows were imported");
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
        });
    }

    @Override
    public Widget getWidget() {
        return csvImportView;
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.empty();
    }

    @Override
    public CSVImportDescriptor getData() {
        return csvImportView.getImportDescriptor().get();
    }
}
