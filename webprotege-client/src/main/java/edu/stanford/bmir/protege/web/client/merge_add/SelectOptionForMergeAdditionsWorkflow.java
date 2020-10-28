package edu.stanford.bmir.protege.web.client.merge_add;

import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;

public class SelectOptionForMergeAdditionsWorkflow {

    @Nonnull
    private final NewOntologyMergeProjectWorkflow newOntologyWorkflow;

    @Nonnull
    private final ExistingOntologyMergeProjectWorkflow existingOntologyWorkflow;

    @Inject
    public SelectOptionForMergeAdditionsWorkflow(@Nonnull NewOntologyMergeProjectWorkflow newOntologyWorkflow,
                                                 @Nonnull ExistingOntologyMergeProjectWorkflow existingOntologyWorkflow) {
        this.newOntologyWorkflow = newOntologyWorkflow;
        this.existingOntologyWorkflow = existingOntologyWorkflow;
    }

    public void start(@Nonnull ProjectId projectId,
                      @Nonnull DocumentId documentId,
                      List<OntologyDocumentId> currentOntologies,
                      List<OWLOntologyID> selectedOntologies) {
        chooseOption(projectId, documentId, currentOntologies, selectedOntologies);
    }

    private void chooseOption(@Nonnull ProjectId projectId,
                              @Nonnull DocumentId documentId,
                              @Nonnull List<OntologyDocumentId> currrentOntologies,
                              @Nonnull List<OWLOntologyID> selectedOntologies){
        final SelectOptionView view = new SelectOptionViewImpl();
        SelectOptionDialogController controller = new SelectOptionDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            int option = view.getSelectedOption();
            if(option == 1) {
                mergeAddNewOntology(projectId, documentId, selectedOntologies);
            }
            else{
                mergeIntoExisting(projectId, documentId, currrentOntologies, selectedOntologies);
            }
            closer.hide();
        });
        WebProtegeDialog.showDialog(controller);
    }

    private void mergeAddNewOntology(@Nonnull ProjectId projectId,
                                     @Nonnull DocumentId documentId,
                                     @Nonnull List<OWLOntologyID> ontologyList){
        newOntologyWorkflow.start(projectId, documentId, ontologyList);
    }

    private void mergeIntoExisting(@Nonnull ProjectId projectId, DocumentId documentId,
                                   @Nonnull List<OntologyDocumentId> currentOntologies,
                                   @Nonnull List<OWLOntologyID> selectedOntologies){
        existingOntologyWorkflow.start(projectId, documentId, currentOntologies, selectedOntologies);
    }
}
