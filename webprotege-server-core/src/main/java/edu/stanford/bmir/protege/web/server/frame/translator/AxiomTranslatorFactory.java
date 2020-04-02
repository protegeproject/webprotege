package edu.stanford.bmir.protege.web.server.frame.translator;

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
    private final ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator;

    @Nonnull
    private final Annotation2PropertyValueTranslator annotation2PropertyValueTranslator;

    @Inject
    public AxiomTranslatorFactory(@Nonnull ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator,
                                  @Nonnull Annotation2PropertyValueTranslator annotation2PropertyValueTranslator) {
        this.classExpression2PropertyValuesTranslator = classExpression2PropertyValuesTranslator;
        this.annotation2PropertyValueTranslator = annotation2PropertyValueTranslator;
    }

    @Nonnull
    public Axiom2PropertyValuesTranslator create(@Nonnull OWLEntity subject,
                                                 @Nonnull OWLAxiom axiom,
                                                 @Nonnull State initialState) {
        return new Axiom2PropertyValuesTranslator(subject,
                                                  axiom,
                                                  initialState,
                                                  classExpression2PropertyValuesTranslator,
                                                  annotation2PropertyValueTranslator);
    }
}
