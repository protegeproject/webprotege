package edu.stanford.bmir.protege.web.client.entity;

import edu.stanford.bmir.protege.web.client.csv.CSVImportDialogController;
import edu.stanford.bmir.protege.web.client.csv.CSVImportViewImpl;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Provider;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 4 Dec 2017
 */
public class CsvImportPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectId projectId;

    private final OWLClass baseClass;

    private final Provider<PrimitiveDataEditor> primitiveDataEditorProvider;

    public CsvImportPresenter(DispatchServiceManager dispatchServiceManager, ProjectId projectId, OWLClass baseClass, Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.baseClass = baseClass;
        this.primitiveDataEditorProvider = primitiveDataEditorProvider;
    }

    public void startImport() {
        UploadFileResultHandler uploadResultHandler = new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(final DocumentId fileDocumentId) {
                WebProtegeDialog<CSVImportDescriptor> csvImportDialog = new WebProtegeDialog<>(
                        new CSVImportDialogController(
                                projectId,
                                fileDocumentId,
                                baseClass,
                                dispatchServiceManager,
                                new CSVImportViewImpl(
                                        primitiveDataEditorProvider)));
                csvImportDialog.setVisible(true);

            }

            @Override
            public void handleFileUploadFailed(
                    String errorMessage) {
                ProgressMonitor.get().hideProgressMonitor();
                MessageBox.showAlert("Error uploading CSV file", errorMessage);
            }
        };
        UploadFileDialogController controller = new UploadFileDialogController("Upload CSV",
                                                                               uploadResultHandler);

        WebProtegeDialog.showDialog(controller);
    }
}
