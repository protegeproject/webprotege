package edu.stanford.bmir.protege.web.shared.merge;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectAction extends AbstractHasProjectAction<MergeUploadedProjectResult> {

    private DocumentId uploadedDocumentId;

    private MergeUploadedProjectAction() {
    }

    public MergeUploadedProjectAction(ProjectId projectId, DocumentId uploadedDocumentId) {
        super(projectId);
        this.uploadedDocumentId = uploadedDocumentId;
    }

    public DocumentId getUploadedDocumentId() {
        return uploadedDocumentId;
    }
}
