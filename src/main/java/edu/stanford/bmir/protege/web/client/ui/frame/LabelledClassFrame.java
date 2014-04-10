package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/12/2012
 */
public class LabelledClassFrame extends LabelledFrame<ClassFrame> {

    private LabelledClassFrame() {
    }

    public LabelledClassFrame(String displayName, ClassFrame frame) {
        super(displayName, frame);
    }
}
