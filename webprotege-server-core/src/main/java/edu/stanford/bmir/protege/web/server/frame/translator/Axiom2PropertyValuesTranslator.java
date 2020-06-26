package edu.stanford.bmir.protege.web.server.frame.translator;

import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
class Axiom2PropertyValuesTranslator {

    @Nonnull
    private final OWLEntity subject;

    @Nonnull
    private final OWLAxiom axiom;

    @Nonnull
    private final State initialState;

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

    private final OWLAxiomVisitorExAdapter<Set<PlainPropertyValue>> axiomVisitor = new OWLAxiomVisitorExAdapter<>(
            Collections
                    .emptySet()) {
        @Nonnull
        @Override
        public Set<PlainPropertyValue> visit(OWLSubClassOfAxiom axiom) {
            return translateSubClassOf(axiom);
        }

        @Nonnull
        @Override
        public Set<PlainPropertyValue> visit(OWLEquivalentClassesAxiom axiom) {
            return translateEquivalentClasses(axiom);
        }

        @Nonnull
        @Override
        public Set<PlainPropertyValue> visit(OWLAnnotationAssertionAxiom axiom) {
            return translateAnnotationAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PlainPropertyValue> visit(OWLObjectPropertyAssertionAxiom axiom) {
            return translateObjectPropertyAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PlainPropertyValue> visit(OWLDataPropertyAssertionAxiom axiom) {
            return translateDataPropertyAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PlainPropertyValue> visit(OWLClassAssertionAxiom axiom) {
            return translateClassAssertion(axiom);
        }
    };

    @Inject
    public Axiom2PropertyValuesTranslator(@Nonnull OWLEntity subject,
                                          @Nonnull OWLAxiom axiom,
                                          @Nonnull State initialState,
                                          @Nonnull SubClassOfAxiom2PropertyValuesTranslator subClassOfAxiom2PropertyValuesTranslator,
                                          @Nonnull EquivalentClassesAxiom2PropertyValuesTranslator equivalentClassesAxiom2PropertyValuesTranslator,
                                          @Nonnull ClassAssertionAxiom2PropertyValuesTranslator classAssertionAxiom2PropertyValuesTranslator,
                                          @Nonnull ObjectPropertyAssertionAxiom2PropertyValuesTranslator objectPropertyAssertionAxiom2PropertyValuesTranslator,
                                          @Nonnull DataPropertyAssertionAxiom2PropertyValuesTranslator dataPropertyAssertionAxiom2PropertyValuesTranslator,
                                          @Nonnull AnnotationAssertionAxiom2PropertyValuesTranslator annotationAssertionAxiom2PropertyValuesTranslator) {
        this.subject = checkNotNull(subject);
        this.axiom = checkNotNull(axiom);
        this.initialState = checkNotNull(initialState);
        this.subClassOfAxiom2PropertyValuesTranslator = subClassOfAxiom2PropertyValuesTranslator;
        this.equivalentClassesAxiom2PropertyValuesTranslator = equivalentClassesAxiom2PropertyValuesTranslator;
        this.classAssertionAxiom2PropertyValuesTranslator = classAssertionAxiom2PropertyValuesTranslator;
        this.objectPropertyAssertionAxiom2PropertyValuesTranslator = objectPropertyAssertionAxiom2PropertyValuesTranslator;
        this.dataPropertyAssertionAxiom2PropertyValuesTranslator = dataPropertyAssertionAxiom2PropertyValuesTranslator;
        this.annotationAssertionAxiom2PropertyValuesTranslator = annotationAssertionAxiom2PropertyValuesTranslator;
    }

    /**
     * Translate the supplied axiom to a set of {@link PlainPropertyValue}s.
     */
    @Nonnull
    public Set<PlainPropertyValue> translate() {
        return axiom.accept(axiomVisitor);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateAnnotationAssertion(OWLAnnotationAssertionAxiom axiom) {
        return annotationAssertionAxiom2PropertyValuesTranslator.translate(axiom,
                                                                           subject,
                                                                           initialState);
    }

    private Set<PlainPropertyValue> translateClassAssertion(OWLClassAssertionAxiom axiom) {
        return classAssertionAxiom2PropertyValuesTranslator.translate(axiom,
                                                                      subject,
                                                                      initialState);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateDataPropertyAssertion(OWLDataPropertyAssertionAxiom axiom) {
        return dataPropertyAssertionAxiom2PropertyValuesTranslator.translate(axiom,
                                                                             subject,
                                                                             initialState);
    }

    private Set<PlainPropertyValue> translateEquivalentClasses(OWLEquivalentClassesAxiom axiom) {
        return equivalentClassesAxiom2PropertyValuesTranslator.translate(axiom,
                                                                         subject,
                                                                         initialState);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateObjectPropertyAssertion(OWLObjectPropertyAssertionAxiom axiom) {
        return objectPropertyAssertionAxiom2PropertyValuesTranslator.translate(axiom,
                                                                               subject,
                                                                               initialState);
    }

    private Set<PlainPropertyValue> translateSubClassOf(OWLSubClassOfAxiom axiom) {
        return subClassOfAxiom2PropertyValuesTranslator.translate(axiom,
                                                                  subject,
                                                                  initialState);
    }


}
