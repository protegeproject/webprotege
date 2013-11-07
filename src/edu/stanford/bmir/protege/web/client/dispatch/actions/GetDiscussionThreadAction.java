package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class GetDiscussionThreadAction extends AbstractHasProjectAction<GetDiscussionThreadResult> {

    private OWLEntity targetEntity;


    /**
     * For serialization purposes only
     */
    private GetDiscussionThreadAction() {
    }

    public GetDiscussionThreadAction(ProjectId projectId, OWLEntity targetEntity) {
        super(projectId);
        this.targetEntity = checkNotNull(targetEntity);
    }

    public OWLEntity getTargetEntity() {
        return targetEntity;
    }
}
