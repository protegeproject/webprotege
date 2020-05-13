package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(PlainPropertyLiteralValue.PROPERTY_LITERAL_VALUE)
public abstract class PlainPropertyLiteralValue extends PlainPropertyValue {

    public static final String PROPERTY_LITERAL_VALUE = "PropertyLiteralValue";

    public static PlainPropertyLiteralValue get(@Nonnull OWLDataProperty dataProperty,
                                                @Nonnull OWLLiteral value,
                                                @Nonnull State state) {
        return new AutoValue_PlainPropertyLiteralValue(dataProperty, value, state);
    }

    @JsonCreator
    @Nonnull
    public static PlainPropertyLiteralValue get(@Nonnull @JsonProperty(PROPERTY) OWLDataProperty dataProperty,
                                                @Nonnull @JsonProperty(VALUE) OWLLiteral value) {
        return get(dataProperty, value, State.ASSERTED);
    }

    @Nonnull
    @Override
    public abstract OWLDataProperty getProperty();

    @Nonnull
    @Override
    public abstract OWLLiteral getValue();

    @Nonnull
    @Override
    public abstract State getState();

    @Override
    public <R> R accept(PlainPropertyValueVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public PlainPropertyValue withState(State state) {
        return get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PropertyLiteralValue toPropertyValue(@Nonnull FrameComponentRenderer renderer) {
        return PropertyLiteralValue.get(
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
