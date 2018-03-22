package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetClassFrameResult implements GetObjectResult<LabelledFrame<ClassFrame>> {


    private LabelledFrame<ClassFrame> frame;

    private Collection<Tag> tags;

    /**
     * For serialization purposes only
     */
    private GetClassFrameResult() {
    }

    public GetClassFrameResult(LabelledFrame<ClassFrame> frame, Collection<Tag> tags) {
        this.frame = frame;
        this.tags = new ArrayList<>(tags);
    }

    public LabelledFrame<ClassFrame> getFrame() {
        return frame;
    }

    public Collection<Tag> getTags() {
        return new ArrayList<>(tags);
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
}
