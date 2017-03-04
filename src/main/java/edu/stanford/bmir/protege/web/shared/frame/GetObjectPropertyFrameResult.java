package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetObjectPropertyFrameResult implements GetObjectResult<LabelledFrame<ObjectPropertyFrame>>, HasBrowserTextMap {

    private LabelledFrame<ObjectPropertyFrame> frame;

    private BrowserTextMap browserTextMap;

    /**
     * For serialization only
     */
    private GetObjectPropertyFrameResult() {
    }

    public GetObjectPropertyFrameResult(LabelledFrame<ObjectPropertyFrame> frame, BrowserTextMap browserTextMap) {
        this.frame = frame;
        this.browserTextMap = browserTextMap;
    }

    public LabelledFrame<ObjectPropertyFrame> getFrame() {
        return frame;
    }

    @Override
    public LabelledFrame<ObjectPropertyFrame> getObject() {
        return frame;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}
