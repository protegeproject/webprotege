package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetDataPropertyFrameResult implements GetObjectResult<DataPropertyFrame> {

    private DataPropertyFrame frame;

    @GwtSerializationConstructor
    private GetDataPropertyFrameResult() {
    }

    public GetDataPropertyFrameResult(DataPropertyFrame frame) {
        this.frame = checkNotNull(frame);
    }

    /**
     * Gets the object.
     *
     * @return The object.  Not {@code null}.
     */
    @Override
    public DataPropertyFrame getObject() {
        return frame;
    }

    public DataPropertyFrame getFrame() {
        return frame;
    }
}
