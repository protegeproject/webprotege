package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

import java.util.*;

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
 * <td>SubClassOf(A ObjectSomeValuesFrom(R B))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue}(R, B)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A ObjectHasValue(R a))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyIndividualValue}(R, a)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A DataSomeValuesFrom(R T))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyDatatypeValue}(R, T)</td>
 * </tr>
 * <tr>
 * <td>SubClassOf(A DataHasValue(R l))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyLiteralValue}(R, l)</td>
 * </tr>
 * <tr>
 * <td>AnnotationAssertion(P :A l)</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue}(P, l)</td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * The translation does not do any splitting of axioms.  e.g.
 * SubClassOf(A  ObjectIntersection(B C)) is not split into SubClassOf(A B)  SubClassOf(A C) before the translation.
 * </p>
 */
public class AxiomPropertyValueTranslator extends OWLAxiomVisitorAdapter {


    public AxiomPropertyValueTranslator() {
    }

    public Set<PropertyValue> getPropertyValues(OWLEntity subject,
                                                OWLAxiom axiom,
                                                OWLOntology rootOntology,
                                                State initialState,
                                                RenderingManager renderingManager) {
        final AxiomTranslator visitor = new AxiomTranslator(subject, rootOntology, initialState, renderingManager);
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


    private class AxiomTranslator extends OWLObjectVisitorExAdapter<Set<PropertyValue>> {

        private final OWLEntity subject;

        private final OWLOntology rootOntology;

        private final State initialState;

        private final RenderingManager rm;


        private AxiomTranslator(OWLEntity subject,
                                OWLOntology rootOntology,
                                State initialState,
                                RenderingManager rm) {
            super(null);
            this.subject = subject;
            this.rootOntology = rootOntology;
            this.initialState = initialState;
            this.rm = rm;
        }

        @Override
        public Set<PropertyValue> visit(OWLSubClassOfAxiom axiom) {
            if (axiom.getSubClass().equals(subject)) {
                return axiom.getSuperClass().accept(new ClassExpressionTranslator(initialState, rm));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLEquivalentClassesAxiom axiom) {
            if (!subject.isOWLClass()) {
                return Collections.emptySet();
            }
            Set<PropertyValue> result = new HashSet<PropertyValue>();
            if (axiom.contains(subject.asOWLClass())) {
                for (OWLClassExpression ce : axiom.getClassExpressions()) {
                    if (!ce.equals(subject)) {
                        for (OWLClassExpression conj : ce.asConjunctSet()) {
                            Set<PropertyValue> vals = conj.accept(new ClassExpressionTranslator(State.DERIVED, rm));
                            if (vals != null) {
                                result.addAll(vals);
                            }
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public Set<PropertyValue> visit(OWLAnnotationAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject.getIRI())) {
                if (axiom.getValue() instanceof IRI) {
                    Set<OWLEntity> entities = rootOntology.getEntitiesInSignature((IRI) axiom.getValue(),
                                                                                  Imports.INCLUDED);
                    if (!entities.isEmpty()) {
                        List<OWLEntity> sorted = new ArrayList<>(entities);
                        Collections.sort(sorted);
                        return toSet(new PropertyAnnotationValue(rm.getRendering(axiom.getProperty()),
                                                                 rm.getRendering(sorted.get(0)),
                                                                 State.ASSERTED));
                    }
                }
                return toSet(new PropertyAnnotationValue(rm.getRendering(axiom.getProperty()),
                                                         rm.getRendering(axiom.getValue()),
                                                         State.ASSERTED));

            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectPropertyAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject) && !axiom.getProperty().isAnonymous() && !axiom.getObject()
                                                                                                  .isAnonymous()) {
                return toSet(new PropertyIndividualValue(rm.getRendering(axiom.getProperty().asOWLObjectProperty()),
                                                         rm.getRendering(axiom.getObject().asOWLNamedIndividual()),
                                                         State.ASSERTED));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataPropertyAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject)) {
                return toSet(new PropertyLiteralValue(rm.getRendering(axiom.getProperty().asOWLDataProperty()),
                                                      new OWLLiteralData(axiom.getObject()),
                                                      State.ASSERTED));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLClassAssertionAxiom axiom) {
            if (axiom.getIndividual().equals(subject)) {
                return axiom.getClassExpression().accept(new ClassExpressionTranslator(initialState, rm));
            }
            else {
                return null;
            }
        }
    }

    private Set<PropertyValue> toSet(PropertyValue propertyValue) {
        return Collections.singleton(propertyValue);
    }


    private class ClassExpressionTranslator extends OWLClassExpressionVisitorExAdapter<Set<PropertyValue>> {

        private State state;

        private final RenderingManager rm;

        private ClassExpressionTranslator(State initialState,
                                          RenderingManager rm) {
            super(null);
            this.state = initialState;
            this.rm = rm;
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectIntersectionOf ce) {
            state = State.DERIVED;
            Set<PropertyValue> result = new HashSet<PropertyValue>();
            for (OWLClassExpression op : ce.asConjunctSet()) {
                Set<PropertyValue> accept = op.accept(this);
                if (accept != null) {
                    result.addAll(accept);
                }
            }
            return result;
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectSomeValuesFrom desc) {
            if (!desc.getProperty().isAnonymous()) {
                if (!desc.getFiller().isAnonymous()) {
                    return toSet(new PropertyClassValue(rm.getRendering(desc.getProperty().asOWLObjectProperty()),
                                                        rm.getRendering(desc.getFiller().asOWLClass()),
                                                        state));
                }
                else {
                    Set<PropertyValue> result = Sets.newHashSet();
                    for (OWLClassExpression ce : desc.getFiller().asConjunctSet()) {
                        if (!ce.isAnonymous()) {
                            result.add(new PropertyClassValue(rm.getRendering(desc.getProperty().asOWLObjectProperty()),
                                                              rm.getRendering(ce.asOWLClass()),
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

        @Override
        public Set<PropertyValue> visit(OWLObjectMinCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
                return toSet(new PropertyClassValue(rm.getRendering(ce.getProperty().asOWLObjectProperty()),
                                                    rm.getRendering(ce.getFiller().asOWLClass()),
                                                    state));
            }
            else {
                return toSet(new PropertyClassValue(rm.getRendering(ce.getProperty().asOWLObjectProperty()),
                                                    rm.getRendering(ce.getFiller().asOWLClass()),
                                                    State.DERIVED));
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectExactCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
                return toSet(new PropertyClassValue(rm.getRendering(ce.getProperty().asOWLObjectProperty()),
                                                    rm.getRendering(ce.getFiller().asOWLClass()),
                                                    State.DERIVED));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectHasValue desc) {
            if (!desc.getProperty().isAnonymous() && !desc.getValue().isAnonymous()) {
                return toSet(new PropertyIndividualValue(rm.getRendering(desc.getProperty().asOWLObjectProperty()),
                                                         rm.getRendering(desc.getFiller().asOWLNamedIndividual()),
                                                         state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataSomeValuesFrom desc) {
            if (desc.getFiller().isDatatype()) {
                return toSet(new PropertyDatatypeValue(rm.getRendering(desc.getProperty().asOWLDataProperty()),
                                                       rm.getRendering(desc.getFiller().asOWLDatatype()),
                                                       state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataMinCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
                return toSet(new PropertyDatatypeValue(rm.getRendering(ce.getProperty().asOWLDataProperty()),
                                                       rm.getRendering(ce.getFiller().asOWLDatatype()),
                                                       state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataExactCardinality ce) {
            if (ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
                return toSet(new PropertyDatatypeValue(rm.getRendering(ce.getProperty().asOWLDataProperty()),
                                                       rm.getRendering(ce.getFiller().asOWLDatatype()),
                                                       state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataHasValue desc) {
            return toSet(new PropertyLiteralValue(rm.getRendering(desc.getProperty().asOWLDataProperty()),
                                                  new OWLLiteralData(desc.getFiller()),
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
            final Set<OWLClassExpression> classExpressions = new HashSet<OWLClassExpression>();
            classExpressions.add(df.getOWLObjectSomeValuesFrom(propertyValue.getProperty().getEntity(),
                                                               propertyValue.getValue().getEntity()));
            if (mode == Mode.MAXIMAL) {
                classExpressions.add(df.getOWLObjectMinCardinality(1,
                                                                   propertyValue.getProperty().getEntity(),
                                                                   propertyValue.getValue().getEntity()));
            }
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
                    for (OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLSubClassOfAxiom(subject, ce));
                    }
                    return result;
                }

                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
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
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
                }

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
            final Set<OWLClassExpression> classExpressions = new HashSet<OWLClassExpression>();
            classExpressions.add(df.getOWLDataSomeValuesFrom(propertyValue.getProperty().getEntity(),
                                                             propertyValue.getValue().getEntity()));
            if (mode == Mode.MAXIMAL) {
                classExpressions.add(df.getOWLDataMinCardinality(1,
                                                                 propertyValue.getProperty().getEntity(),
                                                                 propertyValue.getValue().getEntity()));
            }
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
                    for (OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLSubClassOfAxiom(subject, ce));
                    }
                    return result;
                }

                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
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
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
                }

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
