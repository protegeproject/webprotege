package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.io.Serializable;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class LabelledObjectPropertyFrame extends LabelledFrame<ObjectPropertyFrame> implements Serializable {

    public LabelledObjectPropertyFrame() {
    }

    public LabelledObjectPropertyFrame(String displayName, ObjectPropertyFrame frame) {
        super(displayName, frame);
    }
}
