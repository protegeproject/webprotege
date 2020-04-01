package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PlainPropertyIndividualValue extends PlainPropertyValue {

    @Nonnull
    public static PlainPropertyIndividualValue get(OWLObjectProperty property, OWLNamedIndividual value, State state) {
        return new AutoValue_PlainPropertyIndividualValue(property, value, state);
    }

    @Nonnull
    @Override
    public abstract OWLObjectProperty getProperty();

    @Nonnull
    @Override
    public abstract OWLNamedIndividual getValue();

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
    public PropertyIndividualValue toPropertyValue(@Nonnull FrameComponentRenderer renderer) {
        return PropertyIndividualValue.get(
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
