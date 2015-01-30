package edu.stanford.bmir.protege.web.client.merge;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.merge.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectWorkflow {

    private DispatchServiceManager dispatchServiceManager;


    public MergeUploadedProjectWorkflow(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void start(ProjectId projectId, DocumentId documentId) {
        computeMerge(projectId, documentId);
    }


    private void computeMerge(final ProjectId projectId, final DocumentId uploadedProjectDocumentId) {
        dispatchServiceManager.execute(new ComputeProjectMergeAction(projectId, uploadedProjectDocumentId), new AbstractWebProtegeAsyncCallback<ComputeProjectMergeResult>() {
            @Override
            public void onSuccess(ComputeProjectMergeResult result) {
                confirmMerge(result, projectId, uploadedProjectDocumentId);
            }
        });
    }

    private void confirmMerge(ComputeProjectMergeResult mergeResult, final ProjectId projectId, final DocumentId documentId) {
        ApplyChangesView view = new ApplyChangesViewImpl();
        view.setDiff(mergeResult.getDiff());
        ApplyChangesDialogController controller = new ApplyChangesDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<MergeData>() {
            @Override
            public void handleHide(MergeData data, WebProtegeDialogCloser closer) {
                performMerge(projectId, documentId);
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);

    }

    private void performMerge(ProjectId projectId, DocumentId uploadedProjectDocumentId) {
        dispatchServiceManager.execute(new MergeUploadedProjectAction(projectId, uploadedProjectDocumentId), new AbstractWebProtegeAsyncCallback<MergeUploadedProjectResult>() {
            @Override
            public void onSuccess(MergeUploadedProjectResult mergeUploadedProjectResult) {
                MessageBox.showMessage("The uploaded ontologies were successfully merged into the project");
            }
        });
    }
}
