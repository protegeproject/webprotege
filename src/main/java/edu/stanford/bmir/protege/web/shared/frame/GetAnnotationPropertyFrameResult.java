package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetAnnotationPropertyFrameResult implements Result, GetObjectResult<LabelledFrame<AnnotationPropertyFrame>>, HasBrowserTextMap {

    private LabelledFrame<AnnotationPropertyFrame> frame;

    private BrowserTextMap browserTextMap;

    private GetAnnotationPropertyFrameResult() {
    }

    public GetAnnotationPropertyFrameResult(LabelledFrame<AnnotationPropertyFrame> frame, BrowserTextMap browserTextMap) {
        this.frame = frame;
        this.browserTextMap = browserTextMap;
    }

    public LabelledFrame<AnnotationPropertyFrame> getFrame() {
        return frame;
    }

    @Override
    public LabelledFrame<AnnotationPropertyFrame> getObject() {
        return frame;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}
