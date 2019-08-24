package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyIndividualValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyLiteralValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
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
class AxiomTranslator {

    @Nonnull
    private final OWLEntity subject;

    @Nonnull
    private final OWLAxiom axiom;

    @Nonnull
    private final State initialState;

    @Nonnull
    private final ContextRenderer renderer;

    @Nonnull
    private final ClassExpressionTranslator classExpressionTranslator;

    @Nonnull
    private final AnnotationTranslator annotationTranslator;

    private final OWLAxiomVisitorExAdapter<Set<PropertyValue>> axiomVisitor = new OWLAxiomVisitorExAdapter<>(Collections
                                                                                                                     .emptySet()) {
        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLSubClassOfAxiom axiom) {
            return translateSubClassOf(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLEquivalentClassesAxiom axiom) {
            return translateEquivalentClasses(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLAnnotationAssertionAxiom axiom) {
            return translateAnnotationAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectPropertyAssertionAxiom axiom) {
            return translateObjectPropertyAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataPropertyAssertionAxiom axiom) {
            return translateDataPropertyAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLClassAssertionAxiom axiom) {
            return translateClassAssertion(axiom);
        }
    };

    @Inject
    @AutoFactory
    public AxiomTranslator(@Nonnull OWLEntity subject,
                           @Nonnull OWLAxiom axiom,
                           @Nonnull State initialState,
                           @Provided ContextRenderer renderer,
                           @Provided @Nonnull ClassExpressionTranslator classExpressionTranslator,
                           @Provided @Nonnull AnnotationTranslator annotationTranslator) {
        this.subject = checkNotNull(subject);
        this.axiom = checkNotNull(axiom);
        this.initialState = checkNotNull(initialState);
        this.renderer = checkNotNull(renderer);
        this.classExpressionTranslator = checkNotNull(classExpressionTranslator);
        this.annotationTranslator = checkNotNull(annotationTranslator);
    }

    /**
     * Translate the supplied axiom to a set of {@link PropertyValue}s.
     */
    @Nonnull
    public Set<PropertyValue> translate() {
        return axiom.accept(axiomVisitor);
    }

    private Set<PropertyValue> translateSubClassOf(OWLSubClassOfAxiom axiom) {
        if(!axiom.getSubClass().equals(subject)) {
            return Collections.emptySet();
        }
        var superClass = axiom.getSuperClass();
        return classExpressionTranslator.translate(initialState, superClass);
    }

    private Set<PropertyValue> translateEquivalentClasses(OWLEquivalentClassesAxiom axiom) {
        if(!subject.isOWLClass()) {
            return Collections.emptySet();
        }
        var classExpressions = axiom.getClassExpressions();
        if(!classExpressions.contains(subject.asOWLClass())) {
            return Collections.emptySet();
        }
        return classExpressions.stream()
                .filter(ce -> !ce.equals(subject))
                .flatMap(this::toPropertyValues)
                .collect(toImmutableSet());
    }

    private Stream<? extends PropertyValue> toPropertyValues(OWLClassExpression ce) {
        return classExpressionTranslator.translate(State.DERIVED, ce).stream();
    }


    @Nonnull
    private Set<PropertyValue> translateAnnotationAssertion(OWLAnnotationAssertionAxiom axiom) {
        if(!axiom.getSubject().equals(subject.getIRI())) {
            return Collections.emptySet();
        }
        return annotationTranslator.translate(axiom.getAnnotation(), State.ASSERTED);
    }


    @Nonnull
    private Set<PropertyValue> translateObjectPropertyAssertion(OWLObjectPropertyAssertionAxiom axiom) {
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
        return Collections.singleton(PropertyIndividualValue.get(renderer.getObjectPropertyData(property),
                                                                 renderer.getIndividualData(object),
                                                                 State.ASSERTED));
    }

    @Nonnull
    private Set<PropertyValue> translateDataPropertyAssertion(OWLDataPropertyAssertionAxiom axiom) {
        if(!axiom.getSubject().equals(subject)) {
            return Collections.emptySet();
        }
        OWLDataProperty property = axiom.getProperty().asOWLDataProperty();
        return Collections.singleton(PropertyLiteralValue.get(renderer.getDataPropertyData(property),
                                                              OWLLiteralData.get(axiom.getObject()),
                                                              State.ASSERTED));
    }

    @Nonnull
    private Set<PropertyValue> translateClassAssertion(OWLClassAssertionAxiom axiom) {
        if(!axiom.getIndividual().equals(subject)) {
            return Collections.emptySet();
        }
        var classExpression = axiom.getClassExpression();
        return classExpressionTranslator.translate(initialState, classExpression);
    }
}
