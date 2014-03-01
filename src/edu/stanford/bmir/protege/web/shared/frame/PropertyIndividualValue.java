package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public final class PropertyIndividualValue extends ObjectPropertyValue {

    private PropertyIndividualValue() {
    }

    public PropertyIndividualValue(OWLObjectProperty property, OWLNamedIndividual value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }


    @Override
    public OWLNamedIndividual getValue() {
        return (OWLNamedIndividual) super.getValue();
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
        return "PropertyIndividualValue".hashCode() + getProperty().hashCode() + getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof PropertyIndividualValue)) {
            return false;
        }
        PropertyIndividualValue other = (PropertyIndividualValue) obj;
        return this.getProperty().equals(other.getProperty()) && this.getValue().equals(other.getValue());
    }

    @Override
    protected PropertyValue duplicateWithState(PropertyValueState state) {
        return new PropertyIndividualValue(getProperty(), getValue(), state);
    }
}
