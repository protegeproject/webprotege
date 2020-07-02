package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.FrameComponentRenderer;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
public class FrameComponentSessionRenderer implements FrameComponentRenderer {

    private final Map<Object, Object> cache = new HashMap<>();

    @Nonnull
    private final FrameComponentRenderer delegate;

    @Inject
    public FrameComponentSessionRenderer(@Nonnull FrameComponentRenderer delegate) {
        this.delegate = checkNotNull(delegate);
    }


    @SuppressWarnings("unchecked")
    private <K, V> V get(K key, Supplier<V> loader) {
        var v = cache.get(key);
        if(v == null) {
            v = loader.get();
            cache.put(key, v);
        }
        return (V) v;
    }

    @Nonnull
    @Override
    public OWLClassData getRendering(@Nonnull OWLClass cls) {
        return get(cls, () -> delegate.getRendering(cls));
    }

    @Nonnull
    @Override
    public OWLObjectPropertyData getRendering(@Nonnull OWLObjectProperty property) {
        return get(property, () -> delegate.getRendering(property));
    }

    @Nonnull
    @Override
    public OWLDataPropertyData getRendering(@Nonnull OWLDataProperty property) {
        return get(property, () -> delegate.getRendering(property));
    }

    @Nonnull
    @Override
    public OWLAnnotationPropertyData getRendering(@Nonnull OWLAnnotationProperty property) {
        return get(property, () -> delegate.getRendering(property));
    }

    @Nonnull
    @Override
    public OWLNamedIndividualData getRendering(@Nonnull OWLNamedIndividual individual) {
        return get(individual, () -> delegate.getRendering(individual));
    }

    @Nonnull
    @Override
    public OWLDatatypeData getRendering(@Nonnull OWLDatatype datatype) {
        return get(datatype, () -> delegate.getRendering(datatype));
    }

    @Nonnull
    @Override
    public OWLLiteralData getRendering(@Nonnull OWLLiteral literal) {
        return get(literal, () -> delegate.getRendering(literal));
    }

    @Nonnull
    @Override
    public OWLPrimitiveData getRendering(@Nonnull OWLAnnotationValue annotationValue) {
        return get(annotationValue, () -> delegate.getRendering(annotationValue));
    }

    @Nonnull
    @Override
    public ImmutableSet<OWLEntityData> getRendering(@Nonnull IRI iri) {
        return get(iri, () -> delegate.getRendering(iri));
    }

    @Nonnull
    @Override
    public OWLEntityData getEntityRendering(@Nonnull OWLEntity entity) {
        return get(entity, () -> delegate.getEntityRendering(entity));
    }
}
