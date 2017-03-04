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
public class GetClassFrameResult implements GetObjectResult<LabelledFrame<ClassFrame>>, HasBrowserTextMap {


    private LabelledFrame<ClassFrame> frame;

    private BrowserTextMap browserTextMap;

    /**
     * For serialization purposes only
     */
    private GetClassFrameResult() {
    }

    public GetClassFrameResult(LabelledFrame<ClassFrame> frame, BrowserTextMap browserTextMap) {
        this.frame = frame;
        this.browserTextMap = browserTextMap;
    }

    public LabelledFrame<ClassFrame> getFrame() {
        return frame;
    }

    /**
     * Gets the object.
     *
     * @return The object.  Not {@code null}.
     */
    @Override
    public LabelledFrame<ClassFrame> getObject() {
        return frame;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}
