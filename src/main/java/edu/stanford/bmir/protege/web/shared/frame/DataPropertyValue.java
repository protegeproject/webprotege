package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class DataPropertyValue extends PropertyValue implements ClassFramePropertyValue {


    protected DataPropertyValue() {
    }

    public DataPropertyValue(OWLDataPropertyData property, OWLDatatypeData value, State state) {
        super(property, value, state);
    }

    protected DataPropertyValue(OWLDataPropertyData property, OWLLiteralData value, State state) {
        super(property, value, state);
    }

    @Override
    public OWLDataPropertyData getProperty() {
        return (OWLDataPropertyData) super.getProperty();
    }
}
