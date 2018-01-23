package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetNamedIndividualFrameResult implements Result, GetObjectResult<LabelledFrame<NamedIndividualFrame>> {

    private LabelledFrame<NamedIndividualFrame> frame;

    @GwtSerializationConstructor
    private GetNamedIndividualFrameResult() {
    }

    public GetNamedIndividualFrameResult(LabelledFrame<NamedIndividualFrame> frame) {
        this.frame = checkNotNull(frame);
    }

    public LabelledFrame<NamedIndividualFrame> getFrame() {
        return frame;
    }

    @Override
    public LabelledFrame<NamedIndividualFrame> getObject() {
        return frame;
    }
}
