package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetDataPropertyFrameResult implements GetObjectResult<LabelledFrame<DataPropertyFrame>>, HasBrowserTextMap {

    private LabelledFrame<DataPropertyFrame> frame;

    private BrowserTextMap browserTextMap;

    /**
     * For serialization only
     */
    private GetDataPropertyFrameResult() {
    }

    public GetDataPropertyFrameResult(LabelledFrame<DataPropertyFrame> frame, BrowserTextMap browserTextMap) {
        this.frame = checkNotNull(frame);
        this.browserTextMap = checkNotNull(browserTextMap);
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

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}
