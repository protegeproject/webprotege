package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateFrameAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateClassFrameAction extends UpdateFrameAction<ClassFrame, OWLClassData> {

    @GwtSerializationConstructor
    private UpdateClassFrameAction() {
    }

    public UpdateClassFrameAction(ProjectId projectId, ClassFrame from, ClassFrame to) {
        super(projectId, from, to);
    }
}
