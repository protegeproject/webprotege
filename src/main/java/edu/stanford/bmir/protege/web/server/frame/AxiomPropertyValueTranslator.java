package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
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
 *     Translates a set of axioms into a set of property values.
 * </p>
 * <p>
 *     The translation is as follows:
 *     <table>
 *         <tr>
 *             <td>SubClassOf(A ObjectSomeValuesFrom(R B))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue}(R, B)</td>
 *         </tr>
 *         <tr>
 *             <td>SubClassOf(A ObjectHasValue(R a))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyIndividualValue}(R, a)</td>
 *         </tr>
 *         <tr>
 *             <td>SubClassOf(A DataSomeValuesFrom(R T))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyDatatypeValue}(R, T)</td>
 *         </tr>
 *         <tr>
 *             <td>SubClassOf(A DataHasValue(R l))</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyLiteralValue}(R, l)</td>
 *         </tr>
 *         <tr>
 *             <td>AnnotationAssertion(P :A l)</td><td>{@link edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue}(P, l)</td>
 *         </tr>
 *     </table>
 * </p>
 * <p>
 *     The translation does not do any splitting of axioms.  e.g.
 *     SubClassOf(A  ObjectIntersection(B C)) is not split into SubClassOf(A B)  SubClassOf(A C) before the translation.
 * </p>
 */
public class AxiomPropertyValueTranslator extends OWLAxiomVisitorAdapter {



    public Set<PropertyValue> getPropertyValues(OWLEntity subject, OWLAxiom axiom, OWLOntology rootOntology, PropertyValueState initialState) {
        final AxiomTranslator visitor = new AxiomTranslator(subject, rootOntology, initialState);
        Set<PropertyValue> result = axiom.accept(visitor);
        if(result == null) {
            return Collections.emptySet();
        }
        else {
            return result;
        }
    }
    
    public Set<OWLAxiom> getAxioms(OWLEntity subject, PropertyValue propertyValue, Mode mode) {
        if(propertyValue.getState() == PropertyValueState.DERIVED) {
            return Collections.emptySet();
        }
        PropertyValueTranslator translator = new PropertyValueTranslator(subject, mode);
        return propertyValue.accept(translator);
    }







    private class AxiomTranslator extends OWLObjectVisitorExAdapter<Set<PropertyValue>> {

        private OWLEntity subject;

        private OWLOntology rootOntology;

        private PropertyValueState initialState;

        private AxiomTranslator(OWLEntity subject, OWLOntology rootOntology, PropertyValueState initialState) {
            super(null);
            this.subject = subject;
            this.rootOntology = rootOntology;
            this.initialState = initialState;
        }

        @Override
        public Set<PropertyValue> visit(OWLSubClassOfAxiom axiom) {
            if (axiom.getSubClass().equals(subject)) {
                return axiom.getSuperClass().accept(new ClassExpressionTranslator(initialState));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLEquivalentClassesAxiom axiom) {
            if(!subject.isOWLClass()) {
                return Collections.emptySet();
            }
            Set<PropertyValue> result = new HashSet<PropertyValue>();
            if(axiom.contains(subject.asOWLClass())) {
                for(OWLClassExpression ce : axiom.getClassExpressions()) {
                    if(!ce.equals(subject)) {
                        for (OWLClassExpression conj : ce.asConjunctSet()) {
                            Set<PropertyValue> vals = conj.accept(new ClassExpressionTranslator(PropertyValueState.DERIVED));
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
                if(axiom.getValue() instanceof IRI) {
                    Set<OWLEntity> entities = rootOntology.getEntitiesInSignature((IRI) axiom.getValue(), true);
                    if(!entities.isEmpty()) {
                        List<OWLEntity> sorted = new ArrayList<OWLEntity>(entities);
                        Collections.sort(sorted);
                        return toSet(new PropertyAnnotationValue(axiom.getProperty(), sorted.get(0), PropertyValueState.ASSERTED));
                    }
                }
                    return toSet(new PropertyAnnotationValue(axiom.getProperty(), axiom.getValue(), PropertyValueState.ASSERTED));

            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectPropertyAssertionAxiom axiom) {
            if(axiom.getSubject().equals(subject) && !axiom.getProperty().isAnonymous() && !axiom.getObject().isAnonymous()) {
                return toSet(new PropertyIndividualValue(axiom.getProperty().asOWLObjectProperty(), axiom.getObject().asOWLNamedIndividual(), PropertyValueState.ASSERTED));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataPropertyAssertionAxiom axiom) {
            if(axiom.getSubject().equals(subject)) {
                return toSet(new PropertyLiteralValue(axiom.getProperty().asOWLDataProperty(), axiom.getObject(), PropertyValueState.ASSERTED));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLClassAssertionAxiom axiom) {
            if(axiom.getIndividual().equals(subject)) {
                return axiom.getClassExpression().accept(new ClassExpressionTranslator(initialState));
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

        private PropertyValueState state;

        private ClassExpressionTranslator(PropertyValueState initialState) {
            super(null);
            this.state = initialState;
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectIntersectionOf ce) {
            state = PropertyValueState.DERIVED;
            Set<PropertyValue> result = new HashSet<PropertyValue>();
            for(OWLClassExpression op : ce.asConjunctSet()) {
                Set<PropertyValue> accept = op.accept(this);
                if (accept != null) {
                    result.addAll(accept);
                }
            }
            return result;
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectSomeValuesFrom desc) {
            if(!desc.getProperty().isAnonymous()) {
                if (!desc.getFiller().isAnonymous()) {
                    return toSet(new PropertyClassValue(desc.getProperty().asOWLObjectProperty(), desc.getFiller().asOWLClass(), state));
                }
                else {
                    Set<PropertyValue> result = Sets.newHashSet();
                    for(OWLClassExpression ce : desc.getFiller().asConjunctSet()) {
                        if (!ce.isAnonymous()) {
                            result.add(new PropertyClassValue(desc.getProperty().asOWLObjectProperty(), ce.asOWLClass(), PropertyValueState.DERIVED));
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
            if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
                return toSet(new PropertyClassValue(ce.getProperty().asOWLObjectProperty(), ce.getFiller().asOWLClass(), state));
            }
            else {
                return toSet(new PropertyClassValue(ce.getProperty().asOWLObjectProperty(), ce.getFiller().asOWLClass(), PropertyValueState.DERIVED));
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectExactCardinality ce) {
            if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && !ce.getFiller().isAnonymous()) {
                return toSet(new PropertyClassValue(ce.getProperty().asOWLObjectProperty(), ce.getFiller().asOWLClass(), PropertyValueState.DERIVED));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLObjectHasValue desc) {
            if(!desc.getProperty().isAnonymous() && !desc.getValue().isAnonymous()) {
                return toSet(new PropertyIndividualValue(desc.getProperty().asOWLObjectProperty(), desc.getValue().asOWLNamedIndividual(), state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataSomeValuesFrom desc) {
            if (desc.getFiller().isDatatype()) {
                return toSet(new PropertyDatatypeValue(desc.getProperty().asOWLDataProperty(), desc.getFiller().asOWLDatatype(), state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataMinCardinality ce) {
            if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
                return toSet(new PropertyDatatypeValue(ce.getProperty().asOWLDataProperty(), ce.getFiller().asOWLDatatype(), state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataExactCardinality ce) {
            if(ce.getCardinality() == 1 && !ce.getProperty().isAnonymous() && ce.getFiller().isDatatype()) {
                return toSet(new PropertyDatatypeValue(ce.getProperty().asOWLDataProperty(), ce.getFiller().asOWLDatatype(), state));
            }
            else {
                return null;
            }
        }

        @Override
        public Set<PropertyValue> visit(OWLDataHasValue desc) {
            return toSet(new PropertyLiteralValue(desc.getProperty().asOWLDataProperty(), desc.getValue(), state));
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
            classExpressions.add(df.getOWLObjectSomeValuesFrom(propertyValue.getProperty(), propertyValue.getValue()));
            if(mode == Mode.MAXIMAL) {
                classExpressions.add(df.getOWLObjectMinCardinality(1, propertyValue.getProperty(), propertyValue.getValue()));
            }
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
                    for(OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLSubClassOfAxiom(subject, ce));
                    }
                    return result;
                }

                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
                    for(OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLClassAssertionAxiom(ce, subject));
                    }
                    return result;
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyIndividualValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLObjectHasValue(propertyValue.getProperty(), propertyValue.getValue());
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    return Collections.<OWLAxiom>singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
                }

                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    return Collections.<OWLAxiom>singleton(df.getOWLObjectPropertyAssertionAxiom(propertyValue.getProperty(), subject, propertyValue.getValue()));
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyDatatypeValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final Set<OWLClassExpression> classExpressions = new HashSet<OWLClassExpression>();
            classExpressions.add(df.getOWLDataSomeValuesFrom(propertyValue.getProperty(), propertyValue.getValue()));
            if(mode == Mode.MAXIMAL) {
                classExpressions.add(df.getOWLDataMinCardinality(1, propertyValue.getProperty(), propertyValue.getValue()));
            }
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
                    for(OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLSubClassOfAxiom(subject, ce));
                    }
                    return result;
                }

                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    Set<OWLAxiom> result = new HashSet<OWLAxiom>();
                    for(OWLClassExpression ce : classExpressions) {
                        result.add(df.getOWLClassAssertionAxiom(ce, subject));
                    }
                    return result;
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(final PropertyLiteralValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLDataHasValue(propertyValue.getProperty(), propertyValue.getValue());
            return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
                @Override
                public Set<OWLAxiom> visit(OWLClass subject) {
                    return Collections.<OWLAxiom>singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
                }

                @Override
                public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                    return Collections.<OWLAxiom>singleton(df.getOWLDataPropertyAssertionAxiom(propertyValue.getProperty(), subject, propertyValue.getValue()));
                }
            });
        }

        @Override
        public Set<OWLAxiom> visit(PropertyAnnotationValue propertyValue) {
            OWLDataFactory df = DataFactory.get();
            return Collections.<OWLAxiom>singleton(df.getOWLAnnotationAssertionAxiom(propertyValue.getProperty(), subject.getIRI(), propertyValue.getValue()));
        }
    }
}
