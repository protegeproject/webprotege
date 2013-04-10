package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;

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


    public Set<PropertyValue> getPropertyValues(OWLEntity subject, OWLAxiom axiom, OWLOntology rootOntology) {
        PropertyValue result = axiom.accept(new AxiomTranslator(subject, rootOntology));
        if(result == null) {
            return Collections.emptySet();
        }
        else {
            return Collections.singleton(result);
        }
    }
    
    public Set<OWLAxiom> getAxioms(OWLEntity subject, PropertyValue propertyValue) {
        PropertyValueTranslator translator = new PropertyValueTranslator(subject);
        return Collections.singleton(propertyValue.accept(translator));
    }






    private class AxiomTranslator extends OWLObjectVisitorExAdapter<PropertyValue> {

        private OWLEntity subject;

        private OWLOntology rootOntology;

        private AxiomTranslator(OWLEntity subject, OWLOntology rootOntology) {
            this.subject = subject;
            this.rootOntology = rootOntology;
        }

        @Override
        public PropertyValue visit(OWLSubClassOfAxiom axiom) {
            if (axiom.getSubClass().equals(subject)) {
                return axiom.getSuperClass().accept(new ClassExpressionTranslator());
            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLAnnotationAssertionAxiom axiom) {
            if (axiom.getSubject().equals(subject.getIRI())) {
                if(axiom.getValue() instanceof IRI) {
                    Set<OWLEntity> entities = rootOntology.getEntitiesInSignature((IRI) axiom.getValue(), true);
                    if(!entities.isEmpty()) {
                        List<OWLEntity> sorted = new ArrayList<OWLEntity>(entities);
                        Collections.sort(sorted);
                        return new PropertyAnnotationValue(axiom.getProperty(), sorted.get(0));
                    }
                }
                    return new PropertyAnnotationValue(axiom.getProperty(), axiom.getValue());

            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLObjectPropertyAssertionAxiom axiom) {
            if(axiom.getSubject().equals(subject) && !axiom.getProperty().isAnonymous() && !axiom.getObject().isAnonymous()) {
                return new PropertyIndividualValue(axiom.getProperty().asOWLObjectProperty(), axiom.getObject().asOWLNamedIndividual());
            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLDataPropertyAssertionAxiom axiom) {
            if(axiom.getSubject().equals(subject)) {
                return new PropertyLiteralValue(axiom.getProperty().asOWLDataProperty(), axiom.getObject());
            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLClassAssertionAxiom axiom) {
            if(axiom.getIndividual().equals(subject)) {
                return axiom.getClassExpression().accept(new ClassExpressionTranslator());
            }
            else {
                return null;
            }
        }
    }


    private class ClassExpressionTranslator extends OWLClassExpressionVisitorExAdapter<PropertyValue> {
        @Override
        public PropertyValue visit(OWLObjectSomeValuesFrom desc) {
            if(!desc.getProperty().isAnonymous() && !desc.getFiller().isAnonymous()) {
                return new PropertyClassValue(desc.getProperty().asOWLObjectProperty(), desc.getFiller().asOWLClass());
            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLObjectHasValue desc) {
            if(!desc.getProperty().isAnonymous() && !desc.getValue().isAnonymous()) {
                return new PropertyIndividualValue(desc.getProperty().asOWLObjectProperty(), desc.getValue().asOWLNamedIndividual());
            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLDataSomeValuesFrom desc) {
            if (desc.getFiller().isDatatype()) {
                return new PropertyDatatypeValue(desc.getProperty().asOWLDataProperty(), desc.getFiller().asOWLDatatype());
            }
            else {
                return null;
            }
        }

        @Override
        public PropertyValue visit(OWLDataHasValue desc) {
            return new PropertyLiteralValue(desc.getProperty().asOWLDataProperty(), desc.getValue());
        }
    }

    
    
    private class PropertyValueTranslator implements PropertyValueVisitor<OWLAxiom, RuntimeException> {

        private OWLEntity subject;

        private PropertyValueTranslator(OWLEntity subject) {
            this.subject = subject;
        }

        @Override
        public OWLAxiom visit(final PropertyClassValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLObjectSomeValuesFrom(propertyValue.getProperty(), propertyValue.getValue());
            return subject.accept(new OWLEntityVisitorExAdapter<OWLAxiom>() {
                @Override
                public OWLAxiom visit(OWLClass subject) {
                    return df.getOWLSubClassOfAxiom(subject, classExpression);
                }

                @Override
                public OWLAxiom visit(OWLNamedIndividual subject) {
                    return df.getOWLClassAssertionAxiom(classExpression, subject);
                }
            });
        }

        @Override
        public OWLAxiom visit(final PropertyIndividualValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLObjectHasValue(propertyValue.getProperty(), propertyValue.getValue());
            return subject.accept(new OWLEntityVisitorExAdapter<OWLAxiom>() {
                @Override
                public OWLAxiom visit(OWLClass subject) {
                    return df.getOWLSubClassOfAxiom(subject, classExpression);
                }

                @Override
                public OWLAxiom visit(OWLNamedIndividual subject) {
                    return df.getOWLObjectPropertyAssertionAxiom(propertyValue.getProperty(), subject, propertyValue.getValue());
                }
            });
        }

        @Override
        public OWLAxiom visit(final PropertyDatatypeValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLDataSomeValuesFrom(propertyValue.getProperty(), propertyValue.getValue());
            return subject.accept(new OWLEntityVisitorExAdapter<OWLAxiom>() {
                @Override
                public OWLAxiom visit(OWLClass subject) {
                    return df.getOWLSubClassOfAxiom(subject, classExpression);
                }

                @Override
                public OWLAxiom visit(OWLNamedIndividual subject) {
                    return df.getOWLClassAssertionAxiom(classExpression, subject);
                }
            });
        }

        @Override
        public OWLAxiom visit(final PropertyLiteralValue propertyValue) {
            final OWLDataFactory df = DataFactory.get();
            final OWLClassExpression classExpression = df.getOWLDataHasValue(propertyValue.getProperty(), propertyValue.getValue());
            return subject.accept(new OWLEntityVisitorExAdapter<OWLAxiom>() {
                @Override
                public OWLAxiom visit(OWLClass subject) {
                    return df.getOWLSubClassOfAxiom(subject, classExpression);
                }

                @Override
                public OWLAxiom visit(OWLNamedIndividual subject) {
                    return df.getOWLDataPropertyAssertionAxiom(propertyValue.getProperty(), subject, propertyValue.getValue());
                }
            });
        }

        @Override
        public OWLAxiom visit(PropertyAnnotationValue propertyValue) {
            OWLDataFactory df = DataFactory.get();
            return df.getOWLAnnotationAssertionAxiom(propertyValue.getProperty(), subject.getIRI(), propertyValue.getValue());
        }
    }
}
