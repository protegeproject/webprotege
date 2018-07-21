package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public abstract class PropertyValue implements Comparable<PropertyValue>, Serializable {


    public abstract OWLPropertyData getProperty();

    public abstract OWLPrimitiveData getValue();

    public abstract State getState();

    public abstract boolean isValueMostSpecific();

    public abstract boolean isAnnotation();

    public abstract boolean isLogical();

    public abstract <R, E extends Throwable>  R accept(PropertyValueVisitor<R, E> visitor) throws E;

    public PropertyValue setState(State state) {
        if(getState() == state) {
            return this;
        }
        else {
            return duplicateWithState(state);
        }
    }

    protected abstract PropertyValue duplicateWithState(State state);

    @Override
    public int compareTo(PropertyValue o) {
        return 0;
    }
}
