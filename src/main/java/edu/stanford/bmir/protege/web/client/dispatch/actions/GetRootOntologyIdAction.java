package edu.stanford.bmir.protege.web.client.dispatch.actions;


import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetRootOntologyIdAction extends AbstractHasProjectAction<GetRootOntologyIdResult> {

    /**
     * For serialization purposes only
     */
    private GetRootOntologyIdAction() {
    }

    public GetRootOntologyIdAction(ProjectId projectId) {
        super(projectId);
    }
}
