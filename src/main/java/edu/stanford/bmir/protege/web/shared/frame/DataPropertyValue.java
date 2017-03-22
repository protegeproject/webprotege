package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
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

    public DataPropertyValue(OWLDataPropertyData property, OWLDatatypeData value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    protected DataPropertyValue(OWLDataPropertyData property, OWLLiteralData value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    @Override
    public OWLDataPropertyData getProperty() {
        return (OWLDataPropertyData) super.getProperty();
    }
}
