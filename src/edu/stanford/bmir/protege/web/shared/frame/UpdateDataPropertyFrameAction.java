package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateDataPropertyFrameAction extends UpdateFrameAction<DataPropertyFrame, OWLDataProperty> implements HasProjectId {

    private UpdateDataPropertyFrameAction() {
    }

    public UpdateDataPropertyFrameAction(ProjectId projectId, LabelledFrame<DataPropertyFrame> from, LabelledFrame<DataPropertyFrame> to) {
        super(projectId, from, to);
    }
}
