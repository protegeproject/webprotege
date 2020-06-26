package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateDataPropertyFrameAction extends UpdateFrameAction implements HasProjectId {

    private UpdateDataPropertyFrameAction() {
    }

    public UpdateDataPropertyFrameAction(ProjectId projectId, PlainDataPropertyFrame from, PlainDataPropertyFrame to) {
        super(projectId, from, to);
    }
}
