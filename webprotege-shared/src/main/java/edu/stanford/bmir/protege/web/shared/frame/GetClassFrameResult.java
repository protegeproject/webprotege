package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetClassFrameResult implements GetObjectResult<ClassFrame> {


    private ClassFrame frame;

    /**
     * For serialization purposes only
     */
    private GetClassFrameResult() {
    }

    public GetClassFrameResult(ClassFrame frame) {
        this.frame = frame;
    }

    public ClassFrame getFrame() {
        return frame;
    }

    /**
     * Gets the object.
     *
     * @return The object.  Not {@code null}.
     */
    @Override
    public ClassFrame getObject() {
        return frame;
    }
}
