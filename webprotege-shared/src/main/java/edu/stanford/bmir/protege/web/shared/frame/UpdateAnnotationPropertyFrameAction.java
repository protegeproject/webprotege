package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateAnnotationPropertyFrameAction extends UpdateFrameAction {

    @GwtSerializationConstructor
    private UpdateAnnotationPropertyFrameAction() {
    }

    public UpdateAnnotationPropertyFrameAction(ProjectId projectId, PlainAnnotationPropertyFrame from, PlainAnnotationPropertyFrame to) {
        super(projectId, from, to);
    }
}
