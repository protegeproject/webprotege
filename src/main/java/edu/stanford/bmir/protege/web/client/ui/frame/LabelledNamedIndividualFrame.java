package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/12/2012
 */
public class LabelledNamedIndividualFrame extends LabelledFrame<NamedIndividualFrame> implements Serializable {

    public LabelledNamedIndividualFrame() {
    }

    public LabelledNamedIndividualFrame(String displayName, NamedIndividualFrame frame) {
        super(displayName, frame);
    }
}
