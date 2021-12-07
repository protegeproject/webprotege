package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class GetProjectOntologyIdAction implements ProjectAction<GetProjectOntologyIdResult> {

    private ProjectId projectId;

    protected GetProjectOntologyIdAction() {
    }

    public GetProjectOntologyIdAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
