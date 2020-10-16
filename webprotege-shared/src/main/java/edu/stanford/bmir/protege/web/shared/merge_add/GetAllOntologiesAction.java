package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class GetAllOntologiesAction extends AbstractHasProjectAction<GetAllOntologiesResult> {

    private DocumentId documentId;

    private GetAllOntologiesAction() {
    }

    public GetAllOntologiesAction(ProjectId projectId, DocumentId documentId) {
        super(projectId);
        this.documentId = documentId;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }
}
