package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetAnnotationPropertyFrameResult implements Result, GetObjectResult<AnnotationPropertyFrame> {

    private AnnotationPropertyFrame frame;

    @GwtSerializationConstructor
    private GetAnnotationPropertyFrameResult() {
    }

    public GetAnnotationPropertyFrameResult(AnnotationPropertyFrame frame) {
        this.frame = checkNotNull(frame);
    }

    public AnnotationPropertyFrame getFrame() {
        return frame;
    }

    @Override
    public AnnotationPropertyFrame getObject() {
        return frame;
    }
}
