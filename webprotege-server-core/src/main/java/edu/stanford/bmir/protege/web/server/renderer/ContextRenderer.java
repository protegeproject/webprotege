package edu.stanford.bmir.protege.web.server.renderer;

import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jul 2018
 */
public class ContextRenderer {

    private final RenderingManager renderingManager;

    @Inject
    public ContextRenderer(@Nonnull RenderingManager renderingManager) {
        this.renderingManager = renderingManager;
    }

    @Nonnull
    public OWLEntityData getEntityData(@Nonnull OWLEntity entity) {
        return renderingManager.getRendering(entity);
    }

    @Nonnull
    public OWLClassData getClassData(@Nonnull OWLClass cls) {
        return renderingManager.getClassData(cls);
    }

    @Nonnull
    public OWLObjectPropertyData getObjectPropertyData(@Nonnull OWLObjectProperty property) {
        return renderingManager.getObjectPropertyData(property);
    }

    @Nonnull
    public OWLDataPropertyData getDataPropertyData(@Nonnull OWLDataProperty property) {
        return renderingManager.getDataPropertyData(property);
    }

    @Nonnull
    public OWLAnnotationPropertyData getAnnotationPropertyData(@Nonnull OWLAnnotationProperty property) {
        return renderingManager.getAnnotationPropertyData(property);
    }

    @Nonnull
    public OWLNamedIndividualData getIndividualData(@Nonnull OWLNamedIndividual individual) {
        return renderingManager.getIndividualData(individual);
    }

    @Nonnull
    public OWLDatatypeData getDatatypeData(@Nonnull OWLDatatype datatype) {
        return renderingManager.getDatatypeData(datatype);
    }

    @Nonnull
    public OWLPrimitiveData getAnnotationValueData(@Nonnull OWLAnnotationValue value) {
        return renderingManager.getRendering(value);
    }
}
