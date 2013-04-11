package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/01/2013
 */
public interface FramePresenterStrategy<L extends LabelledFrame<F>, F extends EntityFrame<E>, E extends OWLEntity> {

    ValueEditor<L> getEditor(ProjectId projectId);


//    UpdateObjectRequest<L> createUpdateObjectRequest(ProjectId projectId, L from, L to);


}
