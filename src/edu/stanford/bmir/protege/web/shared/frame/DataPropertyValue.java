package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class DataPropertyValue extends PropertyValue implements ClassFramePropertyValue {


    protected DataPropertyValue() {
    }

    public DataPropertyValue(OWLDataProperty property, OWLDatatype value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    protected DataPropertyValue(OWLDataProperty property, OWLLiteral value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    @Override
    public OWLDataProperty getProperty() {
        return (OWLDataProperty) super.getProperty();
    }
}
