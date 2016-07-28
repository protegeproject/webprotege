package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class GetIssuesAction implements Action<GetIssuesResult>, HasProjectId {

    private ProjectId projectId;

    private GetIssuesAction() {
    }

    public GetIssuesAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
