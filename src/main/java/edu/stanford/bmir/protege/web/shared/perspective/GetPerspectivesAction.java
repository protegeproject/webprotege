package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class GetPerspectivesAction implements ProjectAction<GetPerspectivesResult> {

    private ProjectId projectId;

    private UserId userId;

    private GetPerspectivesAction() {
    }

    public GetPerspectivesAction(ProjectId projectId, UserId userId) {
        this.projectId = projectId;
        this.userId = userId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getUserId() {
        return userId;
    }
}
