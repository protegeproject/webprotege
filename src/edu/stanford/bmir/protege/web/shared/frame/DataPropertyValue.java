package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class DataPropertyValue extends PropertyValue implements ClassFramePropertyValue {


    protected DataPropertyValue() {
    }

    public DataPropertyValue(OWLDataProperty property, OWLDatatype value) {
        super(property, value);
    }

    protected DataPropertyValue(OWLDataProperty property, OWLLiteral value) {
        super(property, value);
    }

    @Override
    public OWLDataProperty getProperty() {
        return (OWLDataProperty) super.getProperty();
    }
}
