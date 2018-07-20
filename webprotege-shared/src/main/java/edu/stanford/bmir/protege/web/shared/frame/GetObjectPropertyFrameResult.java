package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetObjectPropertyFrameResult implements GetObjectResult<ObjectPropertyFrame> {

    private ObjectPropertyFrame frame;

    @GwtSerializationConstructor
    private GetObjectPropertyFrameResult() {
    }

    public GetObjectPropertyFrameResult(ObjectPropertyFrame frame) {
        this.frame = checkNotNull(frame);
    }

    public ObjectPropertyFrame getFrame() {
        return frame;
    }

    @Override
    public ObjectPropertyFrame getObject() {
        return frame;
    }
}
