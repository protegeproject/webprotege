package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

public class ExistingOntologyMergeAddAction extends AbstractHasProjectAction<ExistingOntologyMergeAddResult> {

    private DocumentId documentId;

    private List<OWLOntologyID> selectedOntologies;

    private OntologyDocumentId targetOntology;

    private ExistingOntologyMergeAddAction(){
    }

    public ExistingOntologyMergeAddAction(@Nonnull ProjectId projectId,
                                          @Nonnull DocumentId documentId,
                                          @Nonnull List<OWLOntologyID> selectedOntologies,
                                          @Nonnull OntologyDocumentId targetOntology) {
        super(projectId);
        this.documentId = documentId;
        this.selectedOntologies = selectedOntologies;
        this.targetOntology = checkNotNull(targetOntology);
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public List<OWLOntologyID> getSelectedOntologies() {
        return selectedOntologies;
    }

    public OntologyDocumentId getTargetOntology() {
        return targetOntology;
    }
}
