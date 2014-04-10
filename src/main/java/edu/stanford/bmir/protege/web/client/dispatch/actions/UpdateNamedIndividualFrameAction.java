package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateNamedIndividualFrameAction extends UpdateFrameAction<NamedIndividualFrame, OWLNamedIndividual> {

    /**
     * For serialization purposes only
     */
    private UpdateNamedIndividualFrameAction() {
    }

    public UpdateNamedIndividualFrameAction(ProjectId projectId, LabelledFrame<NamedIndividualFrame> from, LabelledFrame<NamedIndividualFrame> to) {
        super(projectId, from, to);
    }
}
