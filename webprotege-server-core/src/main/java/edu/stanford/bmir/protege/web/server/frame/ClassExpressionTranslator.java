package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
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
class ClassExpressionTranslator {

    @Nonnull
    private final ContextRenderer ren;

    @Inject
    public ClassExpressionTranslator(@Nonnull ContextRenderer ren) {
        this.ren = checkNotNull(ren);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectIntersectionOf(OWLObjectIntersectionOf ce,
                                                             @Nonnull State state) {
        return ce.asConjunctSet()
                .stream()
                .flatMap(exp -> translate(exp, State.DERIVED).stream())
                .collect(toSet());
    }

    /**
     * Translates the originally supplied class expression into a set of {@link PropertyValue}s.
     * @return A set of {@link PropertyValue}s representing the class expression.
     */
    public Set<PropertyValue> translate(
            @Nonnull State initialState,
            @Nonnull OWLClassExpression classExpression) {
        return translate(classExpression, initialState);
    }

    private Set<PropertyValue> translate(@Nonnull OWLClassExpression classExpression,
                                         @Nonnull State state) {
        var visitor = new OWLClassExpressionVisitorExAdapter<Set<PropertyValue>>(Collections
                                                                       .emptySet()) {
            @Nonnull
            @Override
            public Set<PropertyValue> visit(OWLObjectIntersectionOf ce) {
                return translateObjectIntersectionOf(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLObjectSomeValuesFrom ce) {
                return translateObjectSomeValuesFrom(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLObjectMinCardinality ce) {
                return translateObjectMinCardinality(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLObjectExactCardinality ce) {
                return translateObjectExactCardinality(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLObjectHasValue ce) {
                return translateObjectHasValue(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLDataSomeValuesFrom ce) {
                return translateDataSomeValuesFrom(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLDataMinCardinality ce) {
                return translateDataMinCardinality(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLDataExactCardinality ce) {
                return translateDataExactCardinality(ce, state);
            }

            @Override
            public Set<PropertyValue> visit(OWLDataHasValue ce) {
                return translateDataHasValue(ce, state);
            }
        };
        return checkNotNull(classExpression).accept(visitor);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectSomeValuesFrom(OWLObjectSomeValuesFrom desc,
                                                             @Nonnull State state) {
        return translateObjectPropertyFiller(desc.getProperty(), desc.getFiller(), state);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectMinCardinality(OWLObjectMinCardinality ce,
                                                             @Nonnull State state) {
        // Min cardinality of one is syntactic sugar for SomeValuesFrom
        var nextState = state;
        if(ce.getCardinality() != 1) {
            nextState = State.DERIVED;
        }
        return translateObjectCardinality(ce, nextState);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectExactCardinality(OWLObjectExactCardinality ce,
                                                               @Nonnull State state) {
        return translateObjectCardinality(ce, State.DERIVED);
    }

    private Set<PropertyValue> translateObjectCardinality(OWLObjectCardinalityRestriction ce,
                                                          @Nonnull State state) {
        return translateObjectPropertyFiller(ce.getProperty(), ce.getFiller(), state);
    }

    private Set<PropertyValue> translateObjectPropertyFiller(OWLObjectPropertyExpression propertyExpression,
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
                .map(filler -> PropertyClassValue.get(ren.getObjectPropertyData(property),
                                                      ren.getClassData(filler),
                                                      nextState))
                .collect(toSet());
    }

    @Nonnull
    private Set<PropertyValue> translateObjectHasValue(OWLObjectHasValue desc,
                                                       @Nonnull State state) {
        if(desc.getProperty().isAnonymous()) {
            return Collections.emptySet();
        }
        if(desc.getFiller().isAnonymous()) {
            return Collections.emptySet();
        }
        var property = desc.getProperty().asOWLObjectProperty();
        var filler = desc.getFiller().asOWLNamedIndividual();
        return Collections.singleton(PropertyIndividualValue.get(ren.getObjectPropertyData(property), ren.getIndividualData(filler), state));
    }

    @Nonnull
    private Set<PropertyValue> translateDataSomeValuesFrom(OWLDataSomeValuesFrom desc,
                                                           @Nonnull State state) {
        return translateDataPropertyFiller(desc.getProperty(), desc.getFiller(), state);
    }

    private Set<PropertyValue> translateDataPropertyFiller(OWLDataPropertyExpression propertyExpression,
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
        return Collections.singleton(PropertyDatatypeValue.get(ren.getDataPropertyData(property), ren.getDatatypeData(filler), state));
    }

    @Nonnull
    private Set<PropertyValue> translateDataMinCardinality(OWLDataMinCardinality ce,
                                                           @Nonnull State state) {
        if(ce.getCardinality() != 1) {
            state = State.DERIVED;
        }
        return translateDataCardinality(ce, state);
    }

    private Set<PropertyValue> translateDataCardinality(OWLDataCardinalityRestriction ce,
                                                        @Nonnull State state) {
        return translateDataPropertyFiller(ce.getProperty(), ce.getFiller(), state);
    }

    @Nonnull
    private Set<PropertyValue> translateDataExactCardinality(OWLDataExactCardinality ce,
                                                             @Nonnull State state) {
        state = State.DERIVED;
        return translateDataCardinality(ce, state);
    }

    @Nonnull
    private Set<PropertyValue> translateDataHasValue(OWLDataHasValue desc,
                                                     @Nonnull State state) {
        var property = desc.getProperty().asOWLDataProperty();
        return Collections.singleton(PropertyLiteralValue.get(ren.getDataPropertyData(property), OWLLiteralData.get(desc.getFiller()), state));
    }
}
