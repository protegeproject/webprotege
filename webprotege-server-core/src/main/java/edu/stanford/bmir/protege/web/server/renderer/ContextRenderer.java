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
    public OWLEntityData getRendering(@Nonnull OWLEntity entity) {
        return renderingManager.getRendering(entity);
    }

    @Nonnull
    public OWLClassData getRendering(@Nonnull OWLClass cls) {
        return renderingManager.getRendering(cls);
    }

    @Nonnull
    public OWLObjectPropertyData getRendering(@Nonnull OWLObjectProperty property) {
        return renderingManager.getRendering(property);
    }

    @Nonnull
    public OWLDataPropertyData getRendering(@Nonnull OWLDataProperty property) {
        return renderingManager.getRendering(property);
    }

    @Nonnull
    public OWLAnnotationPropertyData getRendering(@Nonnull OWLAnnotationProperty property) {
        return renderingManager.getRendering(property);
    }

    @Nonnull
    public OWLNamedIndividualData getRendering(@Nonnull OWLNamedIndividual individual) {
        return renderingManager.getRendering(individual);
    }

    @Nonnull
    public OWLDatatypeData getRendering(@Nonnull OWLDatatype datatype) {
        return renderingManager.getRendering(datatype);
    }
}
