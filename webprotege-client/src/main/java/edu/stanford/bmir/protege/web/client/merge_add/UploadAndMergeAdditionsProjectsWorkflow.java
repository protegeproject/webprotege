package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesAction;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
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
        dispatchServiceManager.execute(new GetAllOntologiesAction(projectId, documentId), new DispatchServiceCallbackWithProgressDisplay<GetAllOntologiesResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return "Uploading Ontologies";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Uploading and processing Ontologies. Please Wait.";
            }

            @Override
            public void handleSuccess(GetAllOntologiesResult result){
                selectOntologies(projectId, documentId, result);
            }
        });
    }

    private void selectOntologies(ProjectId projectId, DocumentId documentId, GetAllOntologiesResult result){
        ArrayList<OWLOntologyID> list = (ArrayList<OWLOntologyID>) result.getOntologies();

        SelectOntologiesForMergeView view = new SelectOntologiesForMergeViewImpl(list);
        SelectOntologiesForMergeDialogController controller = new SelectOntologiesForMergeDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<List<OWLOntologyID>>() {
            @Override
            public void handleHide(List<OWLOntologyID> data, WebProtegeDialogCloser closer) {
                List<OWLOntologyID> l = view.getSelectedOntologies();
                startSelectAdditionsWorkflow(projectId, documentId, list, l);
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);
    }


    private void startSelectAdditionsWorkflow(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies) {
        selectOptionsWorkflow.start(projectId, documentId, allOntologies, selectedOntologies);
    }
}
