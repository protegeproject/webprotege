package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
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

    @Nonnull
    private final OWLClassExpression classExpression;

    @Nonnull
    private State state;

    private final OWLClassExpressionVisitorExAdapter<Set<PropertyValue>> visitor = new OWLClassExpressionVisitorExAdapter<>(Collections
                                                                                                                                    .emptySet()) {
        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectIntersectionOf ce) {
            return translateObjectIntersectionOf(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectSomeValuesFrom ce) {
            return translateObjectSomeValuesFrom(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectMinCardinality ce) {
            return translateObjectMinCardinality(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectExactCardinality ce) {
            return translateObjectExactCardinality(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectHasValue ce) {
            return translateObjectHasValue(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLDataSomeValuesFrom ce) {
            return translateDataSomeValuesFrom(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLDataMinCardinality ce) {
            return translateDataMinCardinality(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLDataExactCardinality ce) {
            return translateDataExactCardinality(ce);
        }

        @Override
        public Set<PropertyValue> visit(OWLDataHasValue ce) {
            return translateDataHasValue(ce);
        }
    };


    @Inject
    @AutoFactory
    public ClassExpressionTranslator(@Provided @Nonnull ContextRenderer ren,
                                      @Nonnull State initialState,
                                      @Nonnull OWLClassExpression classExpression) {
        this.ren = checkNotNull(ren);
        this.state = checkNotNull(initialState);
        this.classExpression = checkNotNull(classExpression);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectIntersectionOf(OWLObjectIntersectionOf ce) {
        state = State.DERIVED;
        return ce.asConjunctSet()
                .stream()
                .flatMap(exp -> translate(exp).stream())
                .collect(toSet());
    }

    /**
     * Translates the originally supplied class expression into a set of {@link PropertyValue}s.
     * @return A set of {@link PropertyValue}s representing the class expression.
     */
    public Set<PropertyValue> translate() {
        return translate(classExpression);
    }

    private Set<PropertyValue> translate(@Nonnull OWLClassExpression classExpression) {
        return checkNotNull(classExpression).accept(visitor);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectSomeValuesFrom(OWLObjectSomeValuesFrom desc) {
        return translateObjectPropertyFiller(desc.getProperty(), desc.getFiller());
    }

    @Nonnull
    private Set<PropertyValue> translateObjectMinCardinality(OWLObjectMinCardinality ce) {
        return translateObjectCardinality(ce);
    }

    @Nonnull
    private Set<PropertyValue> translateObjectExactCardinality(OWLObjectExactCardinality ce) {
        return translateObjectCardinality(ce);
    }

    private Set<PropertyValue> translateObjectCardinality(OWLObjectCardinalityRestriction ce) {
        if(ce.getCardinality() != 1) {
            state = State.DERIVED;
        }
        return translateObjectPropertyFiller(ce.getProperty(), ce.getFiller());
    }

    private Set<PropertyValue> translateObjectPropertyFiller(OWLObjectPropertyExpression propertyExpression,
                                                             OWLClassExpression fillerExpression) {
        if(propertyExpression.isAnonymous()) {
            return Collections.emptySet();
        }
        var property = propertyExpression.asOWLObjectProperty();
        if(fillerExpression.isAnonymous()) {
            state = State.DERIVED;
        }
        return fillerExpression
                .asConjunctSet()
                .stream()
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .map(filler -> PropertyClassValue.get(ren.getObjectPropertyData(property),
                                                      ren.getClassData(filler), state))
                .collect(toSet());
    }

    @Nonnull
    private Set<PropertyValue> translateObjectHasValue(OWLObjectHasValue desc) {
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
    private Set<PropertyValue> translateDataSomeValuesFrom(OWLDataSomeValuesFrom desc) {
        return translateDataPropertyFiller(desc.getProperty(), desc.getFiller());
    }

    private Set<PropertyValue> translateDataPropertyFiller(OWLDataPropertyExpression propertyExpression,
                                                           OWLDataRange dataRange) {
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
    private Set<PropertyValue> translateDataMinCardinality(OWLDataMinCardinality ce) {
        return translateDataCardinality(ce);
    }

    private Set<PropertyValue> translateDataCardinality(OWLDataCardinalityRestriction ce) {
        if(ce.getCardinality() != 1) {
            state = State.DERIVED;
        }
        return translateDataPropertyFiller(ce.getProperty(), ce.getFiller());
    }

    @Nonnull
    private Set<PropertyValue> translateDataExactCardinality(OWLDataExactCardinality ce) {
        return translateDataCardinality(ce);
    }

    @Nonnull
    private Set<PropertyValue> translateDataHasValue(OWLDataHasValue desc) {
        var property = desc.getProperty().asOWLDataProperty();
        return Collections.singleton(PropertyLiteralValue.get(ren.getDataPropertyData(property), OWLLiteralData.get(desc.getFiller()), state));
    }
}
