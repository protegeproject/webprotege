package edu.stanford.bmir.protege.web.server.frame;

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
    private final ClassExpression2PropertyValueTranslator classExpression2PropertyValueTranslator;

    @Nonnull
    private final Annotation2PropertyValueTranslator annotation2PropertyValueTranslator;

    @Inject
    public AxiomTranslatorFactory(@Nonnull ClassExpression2PropertyValueTranslator classExpression2PropertyValueTranslator,
                                  @Nonnull Annotation2PropertyValueTranslator annotation2PropertyValueTranslator) {
        this.classExpression2PropertyValueTranslator = classExpression2PropertyValueTranslator;
        this.annotation2PropertyValueTranslator = annotation2PropertyValueTranslator;
    }

    @Nonnull
    public Axiom2OutgoingPropertyValuesTranslator create(@Nonnull OWLEntity subject,
                                                         @Nonnull OWLAxiom axiom,
                                                         @Nonnull State initialState) {
        return new Axiom2OutgoingPropertyValuesTranslator(subject,
                                                          axiom,
                                                          initialState,
                                                          classExpression2PropertyValueTranslator,
                                                          annotation2PropertyValueTranslator);
    }
}
