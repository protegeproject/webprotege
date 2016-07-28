package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsAction extends AbstractHasProjectAction<GetOntologyAnnotationsResult> {

    private GetOntologyAnnotationsAction() {
    }

    public GetOntologyAnnotationsAction(ProjectId projectId) {
        super(projectId);
    }
}
