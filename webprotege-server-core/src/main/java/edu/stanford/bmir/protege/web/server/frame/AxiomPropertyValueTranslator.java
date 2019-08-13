package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 * <p>
 * Translates a set of axioms into a set of property values.
 * </p>
 * <p>
 * The translation is as follows:
 * <table>
 * <tr>
 * <td>SubClassOf(A ObjectSomeValuesFrom(R B))</td><td>{@link PropertyClassValue}(R, B)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A ObjectHasValue(R a))</td><td>{@link PropertyIndividualValue}(R, a)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A DataSomeValuesFrom(R T))</td><td>{@link PropertyDatatypeValue}(R, T)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A DataHasValue(R l))</td><td>{@link PropertyLiteralValue}(R, l)</td>
 * </tr>
 * <tr>
 * <td>AnnotationAssertion(P :A l)</td><td>{@link PropertyAnnotationValue}(P, l)</td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * The translation does not do any splitting of axioms.  e.g.
 * SubClassOf(A  ObjectIntersection(B C)) is not split into SubClassOf(A B)  SubClassOf(A C) before the translation.
 * </p>
 */
public class AxiomPropertyValueTranslator extends OWLAxiomVisitorAdapter {

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesIndex;

    @Nonnull
    private final ContextRenderer ren;


    @Inject
    public AxiomPropertyValueTranslator(@Nonnull ContextRenderer ren,
                                        @Nonnull EntitiesInProjectSignatureByIriIndex entitiesIndex) {
        this.ren = checkNotNull(ren);
        this.entitiesIndex = checkNotNull(entitiesIndex);
    }

    public Set<PropertyValue> getPropertyValues(OWLEntity subject,
                                                OWLAxiom axiom,
                                                State initialState) {
        final AxiomTranslator visitor = new AxiomTranslator(subject, initialState, entitiesIndex, ren);
        Set<PropertyValue> result = axiom.accept(visitor);
        if (result == null) {
            return Collections.emptySet();
        }
        else {
            return result;
        }
    }

    public Set<OWLAxiom> getAxioms(OWLEntity subject,
                                   PropertyValue propertyValue,
                                   Mode mode) {
        if (propertyValue.getState() == State.DERIVED) {
            return Collections.emptySet();
        }
        PropertyValueTranslator translator = new PropertyValueTranslator(subject, mode);
        return propertyValue.accept(translator);
    }


    private static class AxiomTranslator extends OWLObjectVisitorExAdapter<Set<PropertyValue>> {

        private final OWLEntity subject;

        private final State initialState;

        private final EntitiesInProjectSignatureByIriIndex entitiesIndex;

        private final ContextRenderer ren;

        private AxiomTranslator(OWLEntity subject,
                                State initialState,
                                EntitiesInProjectSignatureByIriIndex entitiesIndex,
                                ContextRenderer ren) {
            super(null);
            this.subject = subject;
            this.initialState = initialState;
            this.entitiesIndex = entitiesIndex;
            this.ren = ren;
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLSubClassOfAxiom axiom) {
            if (axiom.getSubClass().equals(subject)) {
                return axiom.getSuperClass().accept(new ClassExpressionTranslator(initialState, ren));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLEquivalentClassesAxiom axiom) {
            if (!subject.isOWLClass()) {
                return Collections.emptySet();
            }
            Set<PropertyValue> result = new HashSet<>();
            if (axiom.contains(subject.asOWLClass())) {
                for (OWLClassExpression ce : axiom.getClassExpressions()) {
                    if (!ce.equals(subject)) {
                        for (OWLClassExpression conj : ce.asConjunctSet()) {
                            Set<PropertyValue> vals = conj.accept(new ClassExpressionTranslator(State.DERIVED, ren));
                            if (vals != null) {
                                result.addAll(vals);
                            }
                        }
                    }
                }
            }
            return result;
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLAnnotationAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject.getIRI())) {
                if (axiom.getValue() instanceof IRI) {
                    var entities = entitiesIndex.getEntityInSignature((IRI) axiom.getValue()).collect(Collectors.toSet());
                    if(!entities.isEmpty()) {
                        return entities
                                .stream()
                                .sorted()
                                .map(entity -> PropertyAnnotationValue.get(ren.getAnnotationPropertyData(axiom.getProperty()),
                                                                           ren.getAnnotationValueData(axiom.getValue()),
                                                                           State.ASSERTED))
                                .collect(Collectors.toSet());
                    }
                }
                return toSet(PropertyAnnotationValue.get(ren.getAnnotationPropertyData(axiom.getProperty()),
                                                         ren.getAnnotationValueData(axiom.getValue()),
                                                         State.ASSERTED));

            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectPropertyAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject) && !axiom.getProperty().isAnonymous() && !axiom.getObject()
                                                                                                  .isAnonymous()) {
                return toSet(PropertyIndividualValue.get(ren.getObjectPropertyData(axiom.getProperty().asOWLObjectProperty()),
                                                         ren.getIndividualData(axiom.getObject().asOWLNamedIndividual()),
                                                         State.ASSERTED));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataPropertyAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject)) {
                return toSet(PropertyLiteralValue.get(ren.getDataPropertyData(axiom.getProperty().asOWLDataProperty()),
                                                      OWLLiteralData.get(axiom.getObject()),
                                                      State.ASSERTED));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLClassAssertionAxiom axiom) {
            if (axiom.getIndividual().equals(subject)) {
                return axiom.getClassExpression().accept(new ClassExpressionTranslator(initialState, ren));
            }
            else {
                return null;
            }
        }
    }

    private static Set<PropertyValue> toSet(PropertyValue propertyValue) {
        return Collections.singleton(propertyValue);
    }


    private static class ClassExpressionTranslator extends OWLClassExpressionVisitorExAdapter<Set<PropertyValue>> {

        @Nonnull
        private State state;

        @Nonnull
        private final ContextRenderer ren;


        private ClassExpressionTranslator(@Nonnull State initialState,
                                          @Nonnull ContextRenderer ren) {
            super(null);
            this.state = checkNotNull(initialState);
            this.ren = checkNotNull(ren);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectIntersectionOf ce) {
            state = State.DERIVED;
            Set<PropertyValue> result = new HashSet<>();
            for (OWLClassExpression op : ce.asConjunctSet()) {
                Set<PropertyValue> accept = op.accept(this);
                if (accept != null) {
                    result.addAll(accept);
                }
            }
            return result;
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectSomeValuesFrom desc) {
            if (!desc.getProperty().isAnonymous()) {
                if (!desc.getFiller().isAnonymous()) {
                    return toSet(PropertyClassValue.get(ren.getObjectPropertyData(desc.getProperty().asOWLObjectProperty()),
                                                        ren.getClassData(desc.getFiller().asOWLClass()),
                                                        state));
                }
                else {
                    Set<PropertyValue> result = Sets.newHashSet();
                    for (OWLClassExpression ce : desc.getFiller().asConjunctSet()) {
                        if (!ce.isAnonymous()) {
                            result.add(PropertyClassValue.get(ren.getObjectPropertyData(desc.getProperty().asOWLObjectProperty()),
                                                              ren.getClassData(ce.asOWLClass()),
                                                              State.DERIVED));
                        }
                    }
                    return result;
                }
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectMinCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
                return toSet(PropertyClassValue.get(ren.getObjectPropertyData(ce.getProperty().asOWLObjectProperty()),
                                                    ren.getClassData(ce.getFiller().asOWLClass()),
                                                    state));
            }
            else {
                return toSet(PropertyClassValue.get(ren.getObjectPropertyData(ce.getProperty().asOWLObjectProperty()),
                                                    ren.getClassData(ce.getFiller().asOWLClass()),
                                                    State.DERIVED));
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectExactCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
                return toSet(PropertyClassValue.get(ren.getObjectPropertyData(ce.getProperty().asOWLObjectProperty()),
                                                    ren.getClassData(ce.getFiller().asOWLClass()),
                                                    State.DERIVED));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectHasValue desc) {
            if (!desc.getProperty().isAnonymous() && !desc.getValue().isAnonymous()) {
                return toSet(PropertyIndividualValue.get(ren.getObjectPropertyData(desc.getProperty().asOWLObjectProperty()),
                                                         ren.getIndividualData(desc.getFiller().asOWLNamedIndividual()),
                                                         state));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataSomeValuesFrom desc) {
            if (desc.getFiller().isDatatype()) {
                return toSet(PropertyDatatypeValue.get(ren.getDataPropertyData(desc.getProperty().asOWLDataProperty()),
                                                       ren.getDatatypeData(desc.getFiller().asOWLDatatype()),
                                                       state));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataMinCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
                return toSet(PropertyDatatypeValue.get(ren.getDataPropertyData(ce.getProperty().asOWLDataProperty()),
                                                       ren.getDatatypeData(ce.getFiller().asOWLDatatype()),
                                                       state));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataExactCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
                return toSet(PropertyDatatypeValue.get(ren.getDataPropertyData(ce.getProperty().asOWLDataProperty()),
                                                       ren.getDatatypeData(ce.getFiller().asOWLDatatype()),
                                                       state));
            }
            else {
                return null;
            }
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataHasValue desc) {
            return toSet(PropertyLiteralValue.get(ren.getDataPropertyData(desc.getProperty().asOWLDataProperty()),
                                                  OWLLiteralData.get(desc.getFiller()),
                                                  state));
        }
    }


    private static class PropertyValueTranslator implements PropertyValueVisitor<Set<OWLAxiom>, RuntimeException> {

        private OWLEntity subject;

        private Mode mode;

        private PropertyValueTranslator(OWLEntity subject, Mode mode) {
            this.subject = subject;
            this.mode = mode;
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyClassValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final Set<OWLClassExpression> classExpressions = new HashSet<>();
            classExpressions.add(df.getOWLObjectSomeValuesFrom(propertyValue.getProperty().getEntity(),
                                                               propertyValue.getValue().getEntity()));
            if (mode == Mode.MAXIMAL) {
                classExpressions.add(df.getOWLObjectMinCardinality(1,
                                                                   propertyValue.getProperty().getEntity(),
                                                                   propertyValue.getValue().getEntity()));
            }
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    Set<OWLAxiom> result = new HashSet<>();
                    for (OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLSubClassOfAxiom(subject, ce));
                    }
                    return result;
                }

                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    Set<OWLAxiom> result = new HashSet<>();
                    for (OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLClassAssertionAxiom(ce, subject));
                    }
                    return result;
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyIndividualValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLObjectHasValue(propertyValue.getProperty().getEntity(),
                                                                               propertyValue.getValue().getEntity());
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
                }

                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    return Collections.singleton(df.getOWLObjectPropertyAssertionAxiom(propertyValue.getProperty().getEntity(),
                                                                                       subject,
                                                                                       propertyValue.getValue().getEntity()));
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyDatatypeValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final Set<OWLClassExpression> classExpressions = new HashSet<>();
            classExpressions.add(df.getOWLDataSomeValuesFrom(propertyValue.getProperty().getEntity(),
                                                             propertyValue.getValue().getEntity()));
            if (mode == Mode.MAXIMAL) {
                classExpressions.add(df.getOWLDataMinCardinality(1,
                                                                 propertyValue.getProperty().getEntity(),
                                                                 propertyValue.getValue().getEntity()));
            }
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    Set<OWLAxiom> result = new HashSet<>();
                    for (OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLSubClassOfAxiom(subject, ce));
                    }
                    return result;
                }

                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    Set<OWLAxiom> result = new HashSet<>();
                    for (OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLClassAssertionAxiom(ce, subject));
                    }
                    return result;
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyLiteralValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLDataHasValue(propertyValue.getProperty().getEntity(),
                                                                             propertyValue.getValue().getLiteral());
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
                }

                @Nonnull
                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    return Collections.singleton(df.getOWLDataPropertyAssertionAxiom(propertyValue.getProperty().getEntity(),
                                                                                     subject,
                                                                                     propertyValue.getValue().getObject()));
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(PropertyAnnotationValue propertyValue) {
            OWLDataFactory df = DataFactory.get();
            Optional<OWLAnnotationValue> annotationValue = propertyValue.getValue().asAnnotationValue();
            if (annotationValue.isPresent()) {
                return Collections.singleton(df.getOWLAnnotationAssertionAxiom(propertyValue.getProperty().getEntity(),
                                                                               subject.getIRI(),
                                                                               annotationValue.get()));
            }
            else {
                return null;
            }
        }
    }
}
