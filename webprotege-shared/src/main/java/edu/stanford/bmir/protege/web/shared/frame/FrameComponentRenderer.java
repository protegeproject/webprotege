package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
public interface FrameComponentRenderer {

    @Nonnull
    OWLClassData getRendering(@Nonnull OWLClass cls);

    @Nonnull
    OWLObjectPropertyData getRendering(@Nonnull OWLObjectProperty property);

    @Nonnull
    OWLDataPropertyData getRendering(@Nonnull OWLDataProperty property);

    @Nonnull
    OWLAnnotationPropertyData getRendering(@Nonnull OWLAnnotationProperty property);

    @Nonnull
    OWLNamedIndividualData getRendering(@Nonnull OWLNamedIndividual individual);

    @Nonnull
    OWLDatatypeData getRendering(@Nonnull OWLDatatype datatype);

    @Nonnull
    OWLLiteralData getRendering(@Nonnull OWLLiteral literal);

    @Nonnull
    OWLPrimitiveData getRendering(@Nonnull OWLAnnotationValue annotationValue);

    @Nonnull
    ImmutableSet<OWLEntityData> getRendering(@Nonnull IRI iri);

    @Nonnull
    OWLEntityData getEntityRendering(@Nonnull OWLEntity entity);
}
