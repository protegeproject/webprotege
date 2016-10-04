package edu.stanford.bmir.protege.web.shared.merge;

import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectAction extends AbstractHasProjectAction<MergeUploadedProjectResult> {

    private DocumentId uploadedDocumentId;

    private String commitMessage;

    private MergeUploadedProjectAction() {
    }

    public MergeUploadedProjectAction(ProjectId projectId, DocumentId uploadedDocumentId, String commitMessage) {
        super(projectId);
        this.uploadedDocumentId = uploadedDocumentId;
        this.commitMessage = commitMessage;
    }

    public DocumentId getUploadedDocumentId() {
        return uploadedDocumentId;
    }

    public String getCommitMessage() {
        return commitMessage;
    }
}
