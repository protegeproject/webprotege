package edu.stanford.bmir.protege.web.shared.dispatch.actions;

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
public class GetNamedIndividualFrameResult implements Result, GetObjectResult<NamedIndividualFrame> {

    private NamedIndividualFrame frame;

    @GwtSerializationConstructor
    private GetNamedIndividualFrameResult() {
    }

    public GetNamedIndividualFrameResult(NamedIndividualFrame frame) {
        this.frame = checkNotNull(frame);
    }

    public NamedIndividualFrame getFrame() {
        return frame;
    }

    @Override
    public NamedIndividualFrame getObject() {
        return frame;
    }
}
