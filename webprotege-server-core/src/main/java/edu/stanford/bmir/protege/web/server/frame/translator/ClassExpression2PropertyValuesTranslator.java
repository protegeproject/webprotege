package edu.stanford.bmir.protege.web.server.frame.translator;

import edu.stanford.bmir.protege.web.server.owlapi.ConjunctSet;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
public class ClassExpression2PropertyValuesTranslator {

    @Inject
    public ClassExpression2PropertyValuesTranslator() {
    }

    /**
     * Translates the originally supplied class expression into a set of {@link PlainPropertyValue}s.
     * @return A set of {@link PlainPropertyValue}s representing the class expression.
     */
    public Set<PlainPropertyValue> translate(
            @Nonnull State initialState,
            @Nonnull OWLClassExpression classExpression) {
        return translate(classExpression, initialState);
    }

    private Set<PlainPropertyValue> translate(@Nonnull OWLClassExpression classExpression,
                                         @Nonnull State state) {
        OWLClassExpressionVisitorExAdapter<Set<PlainPropertyValue>> visitor = new OWLClassExpressionVisitorExAdapter<>(
                Collections.emptySet()) {
            @Nonnull
            @Override
            public Set<PlainPropertyValue> visit(OWLObjectIntersectionOf ce) {
                return translateObjectIntersectionOf(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLObjectSomeValuesFrom ce) {
                return translateObjectSomeValuesFrom(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLObjectMinCardinality ce) {
                return translateObjectMinCardinality(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLObjectExactCardinality ce) {
                return translateObjectExactCardinality(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLObjectHasValue ce) {
                return translateObjectHasValue(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLDataSomeValuesFrom ce) {
                return translateDataSomeValuesFrom(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLDataMinCardinality ce) {
                return translateDataMinCardinality(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLDataExactCardinality ce) {
                return translateDataExactCardinality(ce, state);
            }

            @Override
            public Set<PlainPropertyValue> visit(OWLDataHasValue ce) {
                return translateDataHasValue(ce, state);
            }
        };
        return checkNotNull(classExpression).accept(visitor);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateObjectSomeValuesFrom(OWLObjectSomeValuesFrom desc,
                                                             @Nonnull State state) {
        return translateObjectPropertyFiller(desc.getProperty(), desc.getFiller(), state);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateObjectMinCardinality(OWLObjectMinCardinality ce,
                                                             @Nonnull State state) {
        // Min cardinality of one is syntactic sugar for SomeValuesFrom
        var nextState = state;
        if(ce.getCardinality() != 1) {
            nextState = State.DERIVED;
        }
        return translateObjectCardinality(ce, nextState);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateObjectExactCardinality(OWLObjectExactCardinality ce,
                                                               @Nonnull State state) {
        return translateObjectCardinality(ce, State.DERIVED);
    }

    private Set<PlainPropertyValue> translateObjectCardinality(OWLObjectCardinalityRestriction ce,
                                                          @Nonnull State state) {
        return translateObjectPropertyFiller(ce.getProperty(), ce.getFiller(), state);
    }

    private Set<PlainPropertyValue> translateObjectPropertyFiller(OWLObjectPropertyExpression propertyExpression,
                                                             OWLClassExpression fillerExpression,
                                                             @Nonnull State state) {
        if(propertyExpression.isAnonymous()) {
            return Collections.emptySet();
        }
        var property = propertyExpression.asOWLObjectProperty();
        final State nextState;
        if(fillerExpression.isAnonymous()) {
            nextState = State.DERIVED;
        }
        else {
            nextState = state;
        }
        return fillerExpression
                .asConjunctSet()
                .stream()
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .map(filler -> PlainPropertyClassValue.get(property,
                                                      filler,
                                                      nextState))
                .collect(toSet());
    }

    @Nonnull
    private Set<PlainPropertyValue> translateObjectHasValue(OWLObjectHasValue desc,
                                                       @Nonnull State state) {
        if(desc.getProperty().isAnonymous()) {
            return Collections.emptySet();
        }
        if(desc.getFiller().isAnonymous()) {
            return Collections.emptySet();
        }
        var property = desc.getProperty().asOWLObjectProperty();
        var filler = desc.getFiller().asOWLNamedIndividual();
        return Collections.singleton(PlainPropertyIndividualValue.get(property, filler, state));
    }

    @Nonnull
    private Set<PlainPropertyValue> translateDataSomeValuesFrom(OWLDataSomeValuesFrom desc,
                                                           @Nonnull State state) {
        return translateDataPropertyFiller(desc.getProperty(), desc.getFiller(), state);
    }

    private Set<PlainPropertyValue> translateDataPropertyFiller(OWLDataPropertyExpression propertyExpression,
                                                           OWLDataRange dataRange,
                                                           @Nonnull State state) {
        if(propertyExpression.isAnonymous()) {
            return Collections.emptySet();
        }
        var property = propertyExpression.asOWLDataProperty();
        if(dataRange.isAnonymous()) {
            return Collections.emptySet();
        }
        var filler = dataRange.asOWLDatatype();
        return Collections.singleton(PlainPropertyDatatypeValue.get(property, filler, state));
    }


    @Nonnull
    private Set<PlainPropertyValue> translateObjectIntersectionOf(OWLObjectIntersectionOf ce,
                                                                  @Nonnull State state) {
        return ConjunctSet.asConjuncts(ce)
                 .flatMap(exp -> translate(exp, State.DERIVED).stream())
                 .collect(toSet());
    }

    @Nonnull
    private Set<PlainPropertyValue> translateDataMinCardinality(OWLDataMinCardinality ce,
                                                           @Nonnull State state) {
        if(ce.getCardinality() != 1) {
            state = State.DERIVED;
        }
        return translateDataCardinality(ce, state);
    }

    private Set<PlainPropertyValue> translateDataCardinality(OWLDataCardinalityRestriction ce,
                                                        @Nonnull State state) {
        return translateDataPropertyFiller(ce.getProperty(), ce.getFiller(), state);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateDataExactCardinality(OWLDataExactCardinality ce,
                                                             @Nonnull State state) {
        state = State.DERIVED;
        return translateDataCardinality(ce, state);
    }

    @Nonnull
    private Set<PlainPropertyValue> translateDataHasValue(OWLDataHasValue desc,
                                                     @Nonnull State state) {
        var property = desc.getProperty().asOWLDataProperty();
        return Collections.singleton(PlainPropertyLiteralValue.get(property, desc.getFiller(), state));
    }
}
