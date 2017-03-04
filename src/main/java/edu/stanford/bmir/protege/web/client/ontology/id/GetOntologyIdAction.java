package edu.stanford.bmir.protege.web.client.ontology.id;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class GetOntologyIdAction implements Action<GetOntologyIdResult>, HasProjectId {

    private ProjectId projectId;

    private GetOntologyIdAction() {
    }

    public GetOntologyIdAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
