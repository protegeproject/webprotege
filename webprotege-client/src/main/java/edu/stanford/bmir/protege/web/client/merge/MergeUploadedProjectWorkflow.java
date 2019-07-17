package edu.stanford.bmir.protege.web.client.merge;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.merge.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectWorkflow {

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public MergeUploadedProjectWorkflow(@Nonnull DispatchServiceManager dispatchServiceManager, MessageBox messageBox, @Nonnull DispatchErrorMessageDisplay errorDisplay, @Nonnull ProgressDisplay progressDisplay) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBox = checkNotNull(messageBox);
        this.errorDisplay = checkNotNull(errorDisplay);
        this.progressDisplay = checkNotNull(progressDisplay);
    }

    public void start(ProjectId projectId, DocumentId documentId) {
        computeMerge(projectId, documentId);
    }


    private void computeMerge(final ProjectId projectId, final DocumentId uploadedProjectDocumentId) {
        dispatchServiceManager.execute(new ComputeProjectMergeAction(projectId, uploadedProjectDocumentId), new DispatchServiceCallbackWithProgressDisplay<ComputeProjectMergeResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return "Uploading and merging ontologies";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Uploading ontologies and computing the diff.  Please wait.";
            }

            @Override
            public void handleSuccess(ComputeProjectMergeResult mergeResult) {
                confirmMerge(mergeResult, projectId, uploadedProjectDocumentId);
            }
        });
    }

    private void confirmMerge(ComputeProjectMergeResult mergeResult, final ProjectId projectId, final DocumentId documentId) {
        final ApplyChangesView view = new ApplyChangesViewImpl();
        List<DiffElement<String, SafeHtml>> diff = mergeResult.getDiff();
        view.setDiff(diff);
        if(diff.isEmpty()) {
            view.displayEmptyDiffMessage();
        }
        ApplyChangesDialogController controller = new ApplyChangesDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<MergeData>() {
            @Override
            public void handleHide(MergeData data, WebProtegeDialogCloser closer) {
                performMerge(projectId, documentId, view.getCommitMessage());
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);

    }

    private void performMerge(ProjectId projectId, DocumentId uploadedProjectDocumentId, String commitMessage) {

        dispatchServiceManager.execute(new MergeUploadedProjectAction(projectId, uploadedProjectDocumentId, commitMessage), new DispatchServiceCallbackWithProgressDisplay<MergeUploadedProjectResult>(errorDisplay, progressDisplay) {

            @Override
            public String getProgressDisplayTitle() {
                return "Merging ontologies";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Applying changes to merge ontologies.  Please wait.";
            }

            @Override
            public void handleSuccess(MergeUploadedProjectResult mergeUploadedProjectResult) {
                messageBox.showMessage("The uploaded ontologies were successfully merged into the project");
            }
        });
    }
}
