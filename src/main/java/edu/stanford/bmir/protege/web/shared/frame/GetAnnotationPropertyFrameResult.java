package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetAnnotationPropertyFrameResult implements Result, GetObjectResult<LabelledFrame<AnnotationPropertyFrame>> {

    private LabelledFrame<AnnotationPropertyFrame> frame;

    @GwtSerializationConstructor
    private GetAnnotationPropertyFrameResult() {
    }

    public GetAnnotationPropertyFrameResult(LabelledFrame<AnnotationPropertyFrame> frame) {
        this.frame = checkNotNull(frame);
    }

    public LabelledFrame<AnnotationPropertyFrame> getFrame() {
        return frame;
    }

    @Override
    public LabelledFrame<AnnotationPropertyFrame> getObject() {
        return frame;
    }
}
