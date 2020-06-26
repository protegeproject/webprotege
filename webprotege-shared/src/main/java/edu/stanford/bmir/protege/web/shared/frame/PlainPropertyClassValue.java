package edu.stanford.bmir.protege.web.shared.frame;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(PlainPropertyClassValue.PROPERTY_CLASS_VALUE)
public abstract class PlainPropertyClassValue extends PlainPropertyValue {

    public static final String PROPERTY_CLASS_VALUE = "PropertyClassValue";

    @Nonnull
    public static PlainPropertyClassValue get(@Nonnull OWLObjectProperty property,
                                              @Nonnull OWLClass value,
                                              @Nonnull State state) {
        return new AutoValue_PlainPropertyClassValue(property, value, state);
    }

    @JsonCreator
    @Nonnull
    public static PlainPropertyClassValue get(@Nonnull @JsonProperty(PROPERTY) OWLObjectProperty property,
                                              @Nonnull @JsonProperty(VALUE) OWLClass value) {
        return get(property, value, State.ASSERTED);
    }

    @Nonnull
    @Override
    public abstract OWLObjectProperty getProperty();

    @Nonnull
    @Override
    public abstract OWLClass getValue();

    @Nonnull
    @Override
    public abstract State getState();

    @Override
    public <R> R accept(PlainPropertyValueVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public PlainPropertyClassValue withState(State state) {
        return PlainPropertyClassValue.get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PropertyClassValue toPropertyValue(@Nonnull FrameComponentRenderer renderer) {
        return PropertyClassValue.get(
                renderer.getRendering(getProperty()),
                renderer.getRendering(getValue()),
                getState()
        );
    }

    @Override
    public boolean isLogical() {
        return true;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }
}
