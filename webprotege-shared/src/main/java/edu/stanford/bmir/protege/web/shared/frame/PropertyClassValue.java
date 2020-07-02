package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PropertyClassValue extends ObjectPropertyValue {

    @Nonnull
    public static PropertyClassValue get(@Nonnull OWLObjectPropertyData property,
                                         @Nonnull OWLClassData value,
                                         @Nonnull State state) {
        return new AutoValue_PropertyClassValue(property, value, state);
    }

    @Override
    public abstract OWLObjectPropertyData getProperty();

    @Override
    public abstract OWLClassData getValue();

    @Override
    public abstract State getState();

    @Override
    public boolean isValueMostSpecific() {
        return false;
    }

    @Override
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
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
    protected PropertyValue duplicateWithState(State state) {
        return PropertyClassValue.get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PlainPropertyClassValue toPlainPropertyValue() {
        return PlainPropertyClassValue.get(
                getProperty().getEntity(),
                getValue().getEntity(),
                getState()
        );
    }
}
