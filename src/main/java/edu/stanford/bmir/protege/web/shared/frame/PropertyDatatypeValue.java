package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public final class PropertyDatatypeValue extends DataPropertyValue {

    private PropertyDatatypeValue() {
    }

    public PropertyDatatypeValue(OWLDataProperty property, OWLDatatype value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    @Override
    public OWLDatatype getValue() {
        return (OWLDatatype) super.getValue();
    }

    @Override
    public boolean isValueMostSpecific() {
        return false;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }

    @Override
    public boolean isLogical() {
        return true;
    }

    @Override
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "PropertyDatatypeValue".hashCode() + getProperty().hashCode() + getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof PropertyDatatypeValue)) {
            return false;
        }
        PropertyDatatypeValue other = (PropertyDatatypeValue) obj;
        return this.getProperty().equals(other.getProperty()) && this.getValue().equals(other.getValue());
    }

    @Override
    protected PropertyValue duplicateWithState(PropertyValueState state) {
        return new PropertyDatatypeValue(getProperty(), getValue(), state);
    }
}
