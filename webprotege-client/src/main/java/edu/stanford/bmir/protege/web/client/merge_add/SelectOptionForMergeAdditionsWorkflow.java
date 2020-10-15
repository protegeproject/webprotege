package edu.stanford.bmir.protege.web.client.merge_add;

import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
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

    public void start(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies) {
        chooseOption(projectId, documentId, allOntologies, selectedOntologies);
    }

    private void chooseOption(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies){
        final SelectOptionView view = new SelectOptionViewImpl();
        SelectOptionDialogController controller = new SelectOptionDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Integer>() {
            @Override
            public void handleHide(Integer data, WebProtegeDialogCloser closer) {
                int option = view.getSelectedOption();
                if(option == 1) {
                    mergeAddNewOntology(projectId, documentId, selectedOntologies);
                }
                else{
                    mergeIntoExisting(projectId, documentId, allOntologies, selectedOntologies);
                }
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);
    }

    private void mergeAddNewOntology(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> ontologyList){
        newOntologyWorkflow.start(projectId, documentId, ontologyList);
    }

    private void mergeIntoExisting(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies){
        existingOntologyWorkflow.start(projectId, documentId, allOntologies, selectedOntologies);
    }
}
