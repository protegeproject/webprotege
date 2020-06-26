package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.frame.FrameComponentRenderer;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
public class FrameComponentRendererImpl implements FrameComponentRenderer {

    @Nonnull
    private final RenderingManager rm;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    private OWLEntityVisitorEx<OWLEntityData> entityRenderer = new OWLEntityVisitorEx<>() {
        @Nonnull
        @Override
        public OWLEntityData visit(@Nonnull OWLClass cls) {
            return getRendering(cls);
        }

        @Nonnull
        @Override
        public OWLEntityData visit(@Nonnull OWLObjectProperty property) {
            return getRendering(property);
        }

        @Nonnull
        @Override
        public OWLEntityData visit(@Nonnull OWLDataProperty property) {
            return getRendering(property);
        }

        @Nonnull
        @Override
        public OWLEntityData visit(@Nonnull OWLNamedIndividual individual) {
            return getRendering(individual);
        }

        @Nonnull
        @Override
        public OWLEntityData visit(@Nonnull OWLDatatype datatype) {
            return getRendering(datatype);
        }

        @Nonnull
        @Override
        public OWLEntityData visit(@Nonnull OWLAnnotationProperty property) {
            return getRendering(property);
        }
    };

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesByIriIndex;

    private OWLAnnotationValueVisitorEx<OWLPrimitiveData> annotationRenderer = new OWLAnnotationValueVisitorEx<>() {
        @Nonnull
        @Override
        public OWLPrimitiveData visit(@Nonnull IRI iri) {
            return entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(iri)
                                                .sorted()
                                                .findFirst()
                                                .map(entity -> (OWLPrimitiveData) getEntityRendering(entity))
                                                .orElse(IRIData.get(iri, ImmutableMap.of()));
        }

        @Nonnull
        @Override
        public OWLPrimitiveData visit(@Nonnull OWLAnonymousIndividual individual) {
            throw new RuntimeException("Not supported");
        }

        @Nonnull
        @Override
        public OWLPrimitiveData visit(@Nonnull OWLLiteral literal) {
            return OWLLiteralData.get(literal);
        }
    };

    @Inject
    public FrameComponentRendererImpl(@Nonnull RenderingManager rm,
                                      @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                      @Nonnull EntitiesInProjectSignatureByIriIndex entitiesByIriIndex) {
        this.rm = checkNotNull(rm);
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.entitiesByIriIndex = checkNotNull(entitiesByIriIndex);
    }

    @Nonnull
    @Override
    public OWLClassData getRendering(@Nonnull OWLClass cls) {
        return rm.getClassData(cls);
    }

    @Nonnull
    @Override
    public OWLObjectPropertyData getRendering(@Nonnull OWLObjectProperty property) {
        return rm.getObjectPropertyData(property);
    }

    @Nonnull
    @Override
    public OWLDataPropertyData getRendering(@Nonnull OWLDataProperty property) {
        return rm.getDataPropertyData(property);
    }

    @Nonnull
    @Override
    public OWLAnnotationPropertyData getRendering(@Nonnull OWLAnnotationProperty property) {
        return rm.getAnnotationPropertyData(property);
    }

    @Nonnull
    @Override
    public OWLNamedIndividualData getRendering(@Nonnull OWLNamedIndividual individual) {
        return rm.getIndividualData(individual);
    }

    @Nonnull
    @Override
    public OWLDatatypeData getRendering(@Nonnull OWLDatatype datatype) {
        return rm.getDatatypeData(datatype);
    }

    @Nonnull
    @Override
    public OWLLiteralData getRendering(@Nonnull OWLLiteral literal) {
        return OWLLiteralData.get(literal);
    }

    @Nonnull
    @Override
    public OWLPrimitiveData getRendering(@Nonnull OWLAnnotationValue annotationValue) {
        return annotationValue.accept(annotationRenderer);
    }

    @Nonnull
    public OWLEntityData getEntityRendering(@Nonnull OWLEntity entity) {
        return entity.accept(entityRenderer);
    }

    @Nonnull
    @Override
    public ImmutableSet<OWLEntityData> getRendering(@Nonnull IRI iri) {
        return entitiesByIriIndex.getEntitiesInSignature(iri)
                                                .map(this::getEntityRendering)
                                                .collect(toImmutableSet());
    }
}
