package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetNamedIndividualFrameResult implements Result, GetObjectResult<LabelledFrame<NamedIndividualFrame>>, HasBrowserTextMap {

    private LabelledFrame<NamedIndividualFrame> frame;

    private BrowserTextMap browserTextMap;

    private GetNamedIndividualFrameResult() {
    }

    public GetNamedIndividualFrameResult(LabelledFrame<NamedIndividualFrame> frame, BrowserTextMap browserTextMap) {
        this.frame = frame;
        this.browserTextMap = browserTextMap;
    }

    public LabelledFrame<NamedIndividualFrame> getFrame() {
        return frame;
    }

    @Override
    public LabelledFrame<NamedIndividualFrame> getObject() {
        return frame;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}
