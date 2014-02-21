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

    private OWLEntity property;

    private OWLObject value;

    protected PropertyValue() {

    }

    public PropertyValue(OWLEntity property, OWLObject value) {
        this.property = property;
        this.value = value;
    }

    public OWLEntity getProperty() {
        return property;
    }

    public OWLObject getValue() {
        return value;
    }

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

}
