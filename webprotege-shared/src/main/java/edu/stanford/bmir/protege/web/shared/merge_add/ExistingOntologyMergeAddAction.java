package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

public class ExistingOntologyMergeAddAction extends AbstractHasProjectAction<ExistingOntologyMergeAddResult> {

    private DocumentId documentId;

    private List<OWLOntologyID> selectedOntologies;

    private OWLOntologyID targetOntology;

    private ExistingOntologyMergeAddAction(){
    }

    public ExistingOntologyMergeAddAction(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> selectedOntologies, OWLOntologyID targetOntology) {
        super(projectId);
        this.documentId = documentId;
        this.selectedOntologies = selectedOntologies;
        this.targetOntology = targetOntology;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public List<OWLOntologyID> getSelectedOntologies() {
        return selectedOntologies;
    }

    public OWLOntologyID getTargetOntology() {
        return targetOntology;
    }
}
