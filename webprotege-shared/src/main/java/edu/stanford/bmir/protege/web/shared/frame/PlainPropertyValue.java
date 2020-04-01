package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLPrimitive;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
public abstract class PlainPropertyValue {

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    public abstract OWLPrimitive getValue();

    @Nonnull
    public abstract State getState();

    public abstract <R> R accept(PlainPropertyValueVisitor<R> visitor);

    public abstract boolean isAnnotation();

    @Nonnull
    public abstract PlainPropertyValue withState(State state);

    @Nonnull
    public abstract PropertyValue toPropertyValue(@Nonnull FrameComponentRenderer renderer);

    public abstract boolean isLogical();

}
