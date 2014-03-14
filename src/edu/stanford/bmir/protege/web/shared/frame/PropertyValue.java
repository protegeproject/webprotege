package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class PropertyValue implements Comparable<PropertyValue>, Serializable {

    private PropertyValueState state;

    private OWLEntity property;

    private OWLObject value;

    protected PropertyValue() {

    }

    public PropertyValue(OWLEntity property, OWLObject value, PropertyValueState propertyValueState) {
        this.property = property;
        this.value = value;
        this.state = propertyValueState;
    }

    public OWLEntity getProperty() {
        return property;
    }

    public OWLObject getValue() {
        return value;
    }

    public PropertyValueState getState() {
        return state;
    }

    public abstract boolean isValueMostSpecific();

    public abstract boolean isAnnotation();

    public abstract boolean isLogical();

    public abstract <R, E extends Throwable>  R accept(PropertyValueVisitor<R, E> visitor) throws E;

    @Override
    final public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyValue");
        sb.append("(");
        sb.append(getProperty());
        sb.append(" ");
        sb.append(getValue());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int compareTo(PropertyValue o) {
        int diff = this.getProperty().compareTo(o.getProperty());
        if(diff != 0) {
            return diff;
        }
        return this.getValue().compareTo(o.getValue());

    }

    public PropertyValue setState(PropertyValueState state) {
        if(this.state == state) {
            return this;
        }
        else {
            return duplicateWithState(state);
        }
    }

    protected abstract PropertyValue duplicateWithState(PropertyValueState state);

}
