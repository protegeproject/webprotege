package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-22
 */
public class AxiomTranslatorFactory {

    @Nonnull
    private final ContextRenderer renderer;

    @Nonnull
    private final ClassExpressionTranslator classExpressionTranslator;

    @Nonnull
    private final AnnotationTranslator annotationTranslator;

    @Inject
    public AxiomTranslatorFactory(@Nonnull ContextRenderer renderer,
                                  @Nonnull ClassExpressionTranslator classExpressionTranslator,
                                  @Nonnull AnnotationTranslator annotationTranslator) {
        this.renderer = renderer;
        this.classExpressionTranslator = classExpressionTranslator;
        this.annotationTranslator = annotationTranslator;
    }

    @Nonnull
    public AxiomTranslator create(@Nonnull OWLEntity subject,
                                  @Nonnull OWLAxiom axiom,
                                  @Nonnull State initialState) {
        return create(subject, axiom, initialState, renderer);
    }

    @Nonnull
    public AxiomTranslator create(@Nonnull OWLEntity subject,
                                  @Nonnull OWLAxiom axiom,
                                  @Nonnull State initialState,
                                  @Nonnull ContextRenderer renderer) {
        return new AxiomTranslator(subject,
                                   axiom,
                                   initialState,
                                   renderer,
                                   classExpressionTranslator,
                                   annotationTranslator);
    }
}
