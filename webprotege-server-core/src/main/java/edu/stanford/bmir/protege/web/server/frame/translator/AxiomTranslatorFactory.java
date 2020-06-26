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
    private final SubClassOfAxiom2PropertyValuesTranslator subClassOfAxiom2PropertyValuesTranslator;

    @Nonnull
    private final EquivalentClassesAxiom2PropertyValuesTranslator equivalentClassesAxiom2PropertyValuesTranslator;

    @Nonnull
    private final ClassAssertionAxiom2PropertyValuesTranslator classAssertionAxiom2PropertyValuesTranslator;

    @Nonnull
    private final ObjectPropertyAssertionAxiom2PropertyValuesTranslator objectPropertyAssertionAxiom2PropertyValuesTranslator;

    @Nonnull
    private final DataPropertyAssertionAxiom2PropertyValuesTranslator dataPropertyAssertionAxiom2PropertyValuesTranslator;

    @Nonnull
    private final AnnotationAssertionAxiom2PropertyValuesTranslator annotationAssertionAxiom2PropertyValuesTranslator;

    @Inject
    public AxiomTranslatorFactory(@Nonnull SubClassOfAxiom2PropertyValuesTranslator subClassOfAxiom2PropertyValuesTranslator,
                                  @Nonnull EquivalentClassesAxiom2PropertyValuesTranslator equivalentClassesAxiom2PropertyValuesTranslator,
                                  @Nonnull ClassAssertionAxiom2PropertyValuesTranslator classAssertionAxiom2PropertyValuesTranslator,
                                  @Nonnull ObjectPropertyAssertionAxiom2PropertyValuesTranslator objectPropertyAssertionAxiom2PropertyValuesTranslator,
                                  @Nonnull DataPropertyAssertionAxiom2PropertyValuesTranslator dataPropertyAssertionAxiom2PropertyValuesTranslator,
                                  @Nonnull AnnotationAssertionAxiom2PropertyValuesTranslator annotationAssertionAxiom2PropertyValuesTranslator) {
        this.subClassOfAxiom2PropertyValuesTranslator = subClassOfAxiom2PropertyValuesTranslator;
        this.equivalentClassesAxiom2PropertyValuesTranslator = equivalentClassesAxiom2PropertyValuesTranslator;
        this.classAssertionAxiom2PropertyValuesTranslator = classAssertionAxiom2PropertyValuesTranslator;
        this.objectPropertyAssertionAxiom2PropertyValuesTranslator = objectPropertyAssertionAxiom2PropertyValuesTranslator;
        this.dataPropertyAssertionAxiom2PropertyValuesTranslator = dataPropertyAssertionAxiom2PropertyValuesTranslator;
        this.annotationAssertionAxiom2PropertyValuesTranslator = annotationAssertionAxiom2PropertyValuesTranslator;
    }

    @Nonnull
    public Axiom2PropertyValuesTranslator create(@Nonnull OWLEntity subject,
                                                 @Nonnull OWLAxiom axiom,
                                                 @Nonnull State initialState) {
        return new Axiom2PropertyValuesTranslator(subject,
                                                  axiom,
                                                  initialState,
                                                  subClassOfAxiom2PropertyValuesTranslator,
                                                  equivalentClassesAxiom2PropertyValuesTranslator,
                                                  classAssertionAxiom2PropertyValuesTranslator,
                                                  objectPropertyAssertionAxiom2PropertyValuesTranslator,
                                                  dataPropertyAssertionAxiom2PropertyValuesTranslator,
                                                  annotationAssertionAxiom2PropertyValuesTranslator);
    }
}
