package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 * <p>
 *     A {@link OWLPrimitiveData} object wraps either an {@link OWLEntity}, an {@link OWLLiteral} or an
 *     {@link IRI}.
 * </p>
 */
public interface OWLPrimitiveData extends ObjectData {

    @Nonnull
    @Override
    OWLPrimitive getObject();

    <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E;

    <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue);

    PrimitiveType getType();

    default boolean isOWLEntity() {
        return getObject() instanceof OWLEntity;
    }

    default boolean isIRI() {
        return getObject() instanceof IRI;
    }

    default boolean isOWLLiteral() {
        return getObject() instanceof OWLLiteral;
    }

    Optional<OWLAnnotationValue> asAnnotationValue();
}
