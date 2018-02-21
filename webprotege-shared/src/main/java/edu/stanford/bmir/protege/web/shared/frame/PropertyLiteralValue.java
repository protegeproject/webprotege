package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public final class PropertyLiteralValue extends DataPropertyValue {

    private PropertyLiteralValue() {
    }

    public PropertyLiteralValue(OWLDataPropertyData property, OWLLiteralData value, State state) {
        super(property, value, state);
    }

    @Override
    public OWLLiteralData getValue() {
        return (OWLLiteralData) super.getValue();
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
    protected PropertyValue duplicateWithState(State state) {
        return new PropertyLiteralValue(getProperty(), getValue(), state);
    }
}
