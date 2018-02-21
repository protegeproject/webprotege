package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class ObjectPropertyValue extends PropertyValue implements ClassFramePropertyValue {


    protected ObjectPropertyValue() {
    }

    public ObjectPropertyValue(OWLObjectPropertyData property, OWLNamedIndividualData value, State state) {
        super(property, value, state);
    }

    protected ObjectPropertyValue(OWLObjectPropertyData property, OWLClassData value, State state) {
        super(property, value, state);
    }

    @Override
    final public OWLObjectPropertyData getProperty() {
        return (OWLObjectPropertyData) super.getProperty();
    }
}
