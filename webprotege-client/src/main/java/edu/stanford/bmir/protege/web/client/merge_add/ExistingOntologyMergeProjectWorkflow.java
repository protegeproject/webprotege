package edu.stanford.bmir.protege.web.client.merge_add;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class ExistingOntologyMergeProjectWorkflow {
    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public ExistingOntologyMergeProjectWorkflow(@Nonnull DispatchServiceManager dispatchServiceManager,
                                                @Nonnull MessageBox messageBox,
                                                @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                                @Nonnull ProgressDisplay progressDisplay) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.messageBox = messageBox;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
    }


    public void start(ProjectId projectId, DocumentId documentId,
                      List<OntologyDocumentId> allOntologies,
                      List<OWLOntologyID> selectedOntologies){
        selectExistingOntology(projectId, documentId, allOntologies, selectedOntologies);
    }

    private void selectExistingOntology(@Nonnull ProjectId projectId,
                                        @Nonnull DocumentId uploadedDocumentId,
                                        @Nonnull List<OntologyDocumentId> currentOntologies,
                                        @Nonnull List<OWLOntologyID> uploadedOntologies){
        SelectExistingOntologyView view = new SelectExistingOntologyViewImpl(currentOntologies);
        SelectExistingOntologyDialogController controller = new SelectExistingOntologyDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            mergeIntoExistingOntology(projectId, uploadedDocumentId, uploadedOntologies, view.getOntology());
            closer.hide();
        });
        WebProtegeDialog.showDialog(controller);
    }

    private void mergeIntoExistingOntology(@Nonnull ProjectId projectId,
                                           @Nonnull DocumentId documentId,
                                           @Nonnull List<OWLOntologyID> selectedOntologies,
                                           @Nonnull OntologyDocumentId targetOntology){
        dispatchServiceManager.execute(new ExistingOntologyMergeAddAction(projectId, documentId, selectedOntologies, targetOntology), new DispatchServiceCallbackWithProgressDisplay<ExistingOntologyMergeAddResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return "Uploading and Merging Ontologies";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Merging selected ontologies into existing ontology. Please wait.";
            }

            @Override
            public void handleSuccess(ExistingOntologyMergeAddResult result){
                messageBox.showMessage("The uploaded ontologies were successfully merged into the project");
            }
        });
    }
}
