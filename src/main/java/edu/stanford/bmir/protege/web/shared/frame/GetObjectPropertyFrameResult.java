package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetObjectPropertyFrameResult implements GetObjectResult<LabelledFrame<ObjectPropertyFrame>> {

    private LabelledFrame<ObjectPropertyFrame> frame;

    @GwtSerializationConstructor
    private GetObjectPropertyFrameResult() {
    }

    public GetObjectPropertyFrameResult(LabelledFrame<ObjectPropertyFrame> frame) {
        this.frame = checkNotNull(frame);
    }

    public LabelledFrame<ObjectPropertyFrame> getFrame() {
        return frame;
    }

    @Override
    public LabelledFrame<ObjectPropertyFrame> getObject() {
        return frame;
    }
}
