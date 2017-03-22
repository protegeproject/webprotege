package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetDataPropertyFrameResult implements GetObjectResult<LabelledFrame<DataPropertyFrame>> {

    private LabelledFrame<DataPropertyFrame> frame;

    @GwtSerializationConstructor
    private GetDataPropertyFrameResult() {
    }

    public GetDataPropertyFrameResult(LabelledFrame<DataPropertyFrame> frame) {
        this.frame = checkNotNull(frame);
    }

    /**
     * Gets the object.
     *
     * @return The object.  Not {@code null}.
     */
    @Override
    public LabelledFrame<DataPropertyFrame> getObject() {
        return frame;
    }

    public LabelledFrame<DataPropertyFrame> getFrame() {
        return frame;
    }
}
