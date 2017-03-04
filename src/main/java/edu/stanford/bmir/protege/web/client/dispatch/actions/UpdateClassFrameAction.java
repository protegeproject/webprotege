package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateClassFrameAction extends UpdateFrameAction<ClassFrame, OWLClass> {

    /**
     * For serialization purposes only
     */
    private UpdateClassFrameAction() {
    }

    public UpdateClassFrameAction(ProjectId projectId, LabelledFrame<ClassFrame> from, LabelledFrame<ClassFrame> to) {
        super(projectId, from, to);
    }
}
