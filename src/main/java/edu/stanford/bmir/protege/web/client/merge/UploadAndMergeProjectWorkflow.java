package edu.stanford.bmir.protege.web.client.merge;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class UploadAndMergeProjectWorkflow {

    private MergeUploadedProjectWorkflow mergeWorkflow;

    public UploadAndMergeProjectWorkflow(MergeUploadedProjectWorkflow mergeWorkflow) {
        this.mergeWorkflow = mergeWorkflow;
    }

    public void start(ProjectId projectId) {
        uploadProject(projectId);
    }

    private void uploadProject(final ProjectId projectId) {
        UploadFileDialogController uploadFileDialogController = new UploadFileDialogController("Upload ontologies", new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(DocumentId fileDocumentId) {
                startMergeWorkflow(projectId, fileDocumentId);
            }

            @Override
            public void handleFileUploadFailed(String errorMessage) {
                GWT.log("Upload failed");
            }
        });
        WebProtegeDialog.showDialog(uploadFileDialogController);
    }

    private void startMergeWorkflow(ProjectId projectId, DocumentId documentId) {
        mergeWorkflow.start(projectId, documentId);
    }

}
