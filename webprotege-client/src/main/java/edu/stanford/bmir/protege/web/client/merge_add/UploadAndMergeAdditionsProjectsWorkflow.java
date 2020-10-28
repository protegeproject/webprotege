package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.merge_add.GetUploadedAndCurrentOntologiesAction;
import edu.stanford.bmir.protege.web.shared.merge_add.GetUploadedAndCurrentOntologiesResult;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class UploadAndMergeAdditionsProjectsWorkflow {

    @Nonnull
    private final SelectOptionForMergeAdditionsWorkflow selectOptionsWorkflow;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public UploadAndMergeAdditionsProjectsWorkflow(@Nonnull SelectOptionForMergeAdditionsWorkflow selectOptionsWorkflow,
                                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                                   @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                                   @Nonnull ProgressDisplay progressDisplay) {
        this.selectOptionsWorkflow = selectOptionsWorkflow;
        this.dispatchServiceManager = dispatchServiceManager;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
    }

    public void start(ProjectId projectId) {
        uploadProject(projectId);
    }

    private void uploadProject(final ProjectId projectId) {
        UploadFileDialogController uploadFileDialogController = new UploadFileDialogController("Upload ontologies", new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(DocumentId fileDocumentId) {
                getOntologies(projectId, fileDocumentId);
            }

            @Override
            public void handleFileUploadFailed(String errorMessage) {
                GWT.log("Upload failed");
            }
        });
        WebProtegeDialog.showDialog(uploadFileDialogController);
    }

    private void getOntologies(ProjectId projectId, DocumentId documentId){
        dispatchServiceManager.execute(new GetUploadedAndCurrentOntologiesAction(projectId, documentId), new DispatchServiceCallbackWithProgressDisplay<GetUploadedAndCurrentOntologiesResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return "Uploading Ontologies";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Uploading and processing Ontologies. Please Wait.";
            }

            @Override
            public void handleSuccess(GetUploadedAndCurrentOntologiesResult result){
                selectUploadedOntologies(projectId, documentId, result);
            }
        });
    }

    private void selectUploadedOntologies(@Nonnull ProjectId projectId,
                                          @Nonnull DocumentId documentId,
                                          @Nonnull GetUploadedAndCurrentOntologiesResult result){
        List<OWLOntologyID> uploadedOntologyIds = result.getUploadedOntologies();
        SelectOntologiesForMergeView view = new SelectOntologiesForMergeViewImpl(uploadedOntologyIds);
        SelectOntologiesForMergeDialogController controller = new SelectOntologiesForMergeDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            List<OWLOntologyID> selectedUploadedOntologyIds = view.getSelectedOntologies();
            startSelectAdditionsWorkflow(projectId,
                                         documentId,
                                         result.getCurrentOntologyDocumentIds(),
                                         selectedUploadedOntologyIds);
            closer.hide();
        });
        WebProtegeDialog.showDialog(controller);
    }


    private void startSelectAdditionsWorkflow(ProjectId projectId, DocumentId documentId, List<OntologyDocumentId> currentOntologies, List<OWLOntologyID> selectedOntologies) {
        selectOptionsWorkflow.start(projectId, documentId, currentOntologies, selectedOntologies);
    }
}
