package edu.stanford.bmir.protege.web.server.frame.translator;

import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

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
    private final ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator;

    @Nonnull
    private final Annotation2PropertyValueTranslator annotation2PropertyValueTranslator;

    private final OWLAxiomVisitorExAdapter<Set<PlainPropertyValue>> axiomVisitor = new OWLAxiomVisitorExAdapter<>(Collections
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
                                          @Nonnull ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator,
                                          @Nonnull Annotation2PropertyValueTranslator annotation2PropertyValueTranslator) {
        this.subject = checkNotNull(subject);
        this.axiom = checkNotNull(axiom);
        this.initialState = checkNotNull(initialState);
        this.classExpression2PropertyValuesTranslator = checkNotNull(classExpression2PropertyValuesTranslator);
        this.annotation2PropertyValueTranslator = checkNotNull(annotation2PropertyValueTranslator);
    }

    /**
     * Translate the supplied axiom to a set of {@link PlainPropertyValue}s.
     */
    @Nonnull
    public Set<PlainPropertyValue> translate() {
        return axiom.accept(axiomVisitor);
    }

    private Set<PlainPropertyValue> translateSubClassOf(OWLSubClassOfAxiom axiom) {
        if(!axiom.getSubClass().equals(subject)) {
            return Collections.emptySet();
        }
        var superClass = axiom.getSuperClass();
        return classExpression2PropertyValuesTranslator.translate(initialState, superClass);
    }

    private Set<PlainPropertyValue> translateEquivalentClasses(OWLEquivalentClassesAxiom axiom) {
        if(!subject.isOWLClass()) {
            return Collections.emptySet();
        }
        var classExpressions = axiom.getClassExpressions();
        if(!classExpressions.contains(subject.asOWLClass())) {
            return Collections.emptySet();
        }
        return classExpressions.stream()
                .filter(ce -> !ce.equals(subject))
                .flatMap(this::toDerivedPlainPropertyValues)
                .collect(toImmutableSet());
    }

    private Stream<? extends PlainPropertyValue> toDerivedPlainPropertyValues(OWLClassExpression ce) {
        return classExpression2PropertyValuesTranslator.translate(State.DERIVED, ce).stream();
    }


    @Nonnull
    private Set<PlainPropertyValue> translateAnnotationAssertion(OWLAnnotationAssertionAxiom axiom) {
        if(!axiom.getSubject().equals(subject.getIRI())) {
            return Collections.emptySet();
        }
        return annotation2PropertyValueTranslator.translate(axiom.getAnnotation(), State.ASSERTED);
    }


    @Nonnull
    private Set<PlainPropertyValue> translateObjectPropertyAssertion(OWLObjectPropertyAssertionAxiom axiom) {
        if(axiom.getProperty().isAnonymous()) {
            return Collections.emptySet();
        }
        if(axiom.getObject().isAnonymous()) {
            return Collections.emptySet();
        }
        if(!axiom.getSubject().equals(subject)) {
            return Collections.emptySet();
        }
        var property = axiom.getProperty().asOWLObjectProperty();
        var object = axiom.getObject().asOWLNamedIndividual();
        return Collections.singleton(PlainPropertyIndividualValue.get(property,
                                                                      object,
                                                                      State.ASSERTED));
    }

    @Nonnull
    private Set<PlainPropertyValue> translateDataPropertyAssertion(OWLDataPropertyAssertionAxiom axiom) {
        if(!axiom.getSubject().equals(subject)) {
            return Collections.emptySet();
        }
        OWLDataProperty property = axiom.getProperty().asOWLDataProperty();
        return Collections.singleton(PlainPropertyLiteralValue.get(property,
                                                              axiom.getObject(),
                                                              State.ASSERTED));
    }

    @Nonnull
    private Set<PlainPropertyValue> translateClassAssertion(OWLClassAssertionAxiom axiom) {
        if(!axiom.getIndividual().equals(subject)) {
            return Collections.emptySet();
        }
        var classExpression = axiom.getClassExpression();
        return classExpression2PropertyValuesTranslator.translate(initialState, classExpression);
    }
}
