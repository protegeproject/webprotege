package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateObjectPropertyFrameAction extends UpdateFrameAction<ObjectPropertyFrame, OWLObjectProperty> {

    private UpdateObjectPropertyFrameAction() {
    }

    public UpdateObjectPropertyFrameAction(ProjectId projectId, LabelledFrame<ObjectPropertyFrame> from, LabelledFrame<ObjectPropertyFrame> to) {
        super(projectId, from, to);
    }
}
