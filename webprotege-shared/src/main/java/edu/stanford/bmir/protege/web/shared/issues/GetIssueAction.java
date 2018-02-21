package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Sep 2016
 */
public class GetIssueAction implements Action<GetIssueResult> {

    private ProjectId projectId;

    private int issueNumber;

    public GetIssueAction(ProjectId projectId, int issueNumber) {
        this.projectId = projectId;
        this.issueNumber = issueNumber;
    }

    @GwtSerializationConstructor
    private GetIssueAction() {
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public int getIssueNumber() {
        return issueNumber;
    }
}
