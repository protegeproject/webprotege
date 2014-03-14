package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public final class PropertyLiteralValue extends DataPropertyValue {

    private PropertyLiteralValue() {
    }

    public PropertyLiteralValue(OWLDataProperty property, OWLLiteral value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    public PropertyLiteralValue(OWLDataProperty property, int value, PropertyValueState propertyValueState) {
        super(property, DataFactory.getOWLLiteral(value), propertyValueState);
    }


    public PropertyLiteralValue(OWLDataProperty property, double value, PropertyValueState propertyValueState) {
        super(property, DataFactory.getOWLLiteral(value), propertyValueState);
    }


    public PropertyLiteralValue(OWLDataProperty property, String value, PropertyValueState propertyValueState) {
        super(property, DataFactory.getOWLLiteral(value), propertyValueState);
    }


    public PropertyLiteralValue(OWLDataProperty property, boolean value, PropertyValueState propertyValueState) {
        super(property, DataFactory.getOWLLiteral(value), propertyValueState);
    }

    @Override
    public OWLLiteral getValue() {
        return (OWLLiteral) super.getValue();
    }

    @Override
    public boolean isValueMostSpecific() {
        return true;
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
        return "PropertyLiteralValue".hashCode() + getProperty().hashCode() + getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof PropertyLiteralValue)) {
            return false;
        }
        PropertyLiteralValue other = (PropertyLiteralValue) obj;
        return this.getProperty().equals(other.getProperty()) && this.getValue().equals(other.getValue());
    }

    @Override
    protected PropertyValue duplicateWithState(PropertyValueState state) {
        return new PropertyLiteralValue(getProperty(), getValue(), state);
    }
}
