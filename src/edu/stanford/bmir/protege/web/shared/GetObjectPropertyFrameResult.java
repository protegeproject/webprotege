package edu.stanford.bmir.protege.web.shared;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameResult extends GetObjectResult<LabelledFrame<ObjectPropertyFrame>> {

    private GetObjectPropertyFrameResult() {
    }

    public GetObjectPropertyFrameResult(LabelledFrame<ObjectPropertyFrame> object) {
        super(object);
    }

}
