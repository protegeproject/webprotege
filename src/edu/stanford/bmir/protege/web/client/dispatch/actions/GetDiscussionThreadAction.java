package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class GetDiscussionThreadAction extends AbstractHasProjectAction<GetDiscussionThreadResult> {

    private OWLEntity targetEntity;


    private GetDiscussionThreadAction() {
    }

    public GetDiscussionThreadAction(ProjectId projectId, OWLEntity targetEntity) {
        super(projectId);
        this.targetEntity = targetEntity;
    }

    public OWLEntity getTargetEntity() {
        return targetEntity;
    }
}
