package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetUploadedAndCurrentOntologiesAction extends AbstractHasProjectAction<GetUploadedAndCurrentOntologiesResult> {

    private DocumentId documentId;

    private GetUploadedAndCurrentOntologiesAction() {
    }

    public GetUploadedAndCurrentOntologiesAction(@Nonnull ProjectId projectId,
                                                 @Nonnull DocumentId documentId) {
        super(projectId);
        this.documentId = checkNotNull(documentId);
    }

    @Nonnull
    public DocumentId getDocumentId() {
        return documentId;
    }
}
