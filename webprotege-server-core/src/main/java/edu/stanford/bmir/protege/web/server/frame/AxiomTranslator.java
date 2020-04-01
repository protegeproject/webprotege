package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    public AxiomTranslator(@Nonnull OWLEntity subject,
                           @Nonnull OWLAxiom axiom,
                           @Nonnull State initialState,
                           @Nonnull ContextRenderer renderer,
                           @Nonnull ClassExpressionTranslator classExpressionTranslator,
                           @Nonnull AnnotationTranslator annotationTranslator) {
        this.subject = checkNotNull(subject);
        this.axiom = checkNotNull(axiom);
        this.initialState = checkNotNull(initialState);
        this.renderer = checkNotNull(renderer);
        this.classExpressionTranslator = checkNotNull(classExpressionTranslator);
        this.annotationTranslator = checkNotNull(annotationTranslator);
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
        return classExpressionTranslator.translate(initialState, superClass);
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
                .flatMap(this::toPlainPropertyValues)
                .collect(toImmutableSet());
    }

    private Stream<? extends PlainPropertyValue> toPlainPropertyValues(OWLClassExpression ce) {
        return classExpressionTranslator.translate(State.DERIVED, ce).stream();
    }


    @Nonnull
    private Set<PlainPropertyValue> translateAnnotationAssertion(OWLAnnotationAssertionAxiom axiom) {
        if(!axiom.getSubject().equals(subject.getIRI())) {
            return Collections.emptySet();
        }
        return annotationTranslator.translate(axiom.getAnnotation(), State.ASSERTED);
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
        return classExpressionTranslator.translate(initialState, classExpression);
    }
}
