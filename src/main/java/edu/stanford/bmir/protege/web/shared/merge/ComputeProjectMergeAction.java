package edu.stanford.bmir.protege.web.shared.merge;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.client.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ComputeProjectMergeAction extends AbstractHasProjectAction<ComputeProjectMergeResult> {

    private DocumentId projectDocumentId;

    private ComputeProjectMergeAction() {
    }

    public ComputeProjectMergeAction(ProjectId projectId, DocumentId projectDocumentId) {
        super(projectId);
        this.projectDocumentId = projectDocumentId;
    }

    public DocumentId getProjectDocumentId() {
        return projectDocumentId;
    }
}

