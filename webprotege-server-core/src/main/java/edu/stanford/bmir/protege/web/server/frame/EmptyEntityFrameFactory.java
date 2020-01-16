package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-15
 */
public class EmptyEntityFrameFactory {

    @Nonnull
    private final ContextRenderer renderingManager;

    @Inject
    public EmptyEntityFrameFactory(@Nonnull ContextRenderer renderingManager) {
        this.renderingManager = checkNotNull(renderingManager);
    }

    public EntityFrame getEmptyEntityFrame(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<EntityFrame>() {
            @Nonnull
            @Override
            public EntityFrame visit(@Nonnull OWLClass cls) {
                return ClassFrame.empty(renderingManager.getClassData(cls));
            }

            @Nonnull
            @Override
            public EntityFrame visit(@Nonnull OWLObjectProperty property) {
                return ObjectPropertyFrame.empty(renderingManager.getObjectPropertyData(property));
            }

            @Nonnull
            @Override
            public EntityFrame visit(@Nonnull OWLDataProperty property) {
                return DataPropertyFrame.empty(renderingManager.getDataPropertyData(property));
            }

            @Nonnull
            @Override
            public EntityFrame visit(@Nonnull OWLNamedIndividual individual) {
                return NamedIndividualFrame.empty(renderingManager.getIndividualData(individual));
            }

            @Nonnull
            @Override
            public EntityFrame visit(@Nonnull OWLDatatype datatype) {
                throw new UnsupportedOperationException();
            }

            @Nonnull
            @Override
            public EntityFrame visit(@Nonnull OWLAnnotationProperty property) {
                return AnnotationPropertyFrame.empty(renderingManager.getAnnotationPropertyData(property));
            }
        });
    }
}
