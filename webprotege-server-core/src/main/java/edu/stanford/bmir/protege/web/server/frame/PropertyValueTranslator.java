package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
class PropertyValueTranslator implements PropertyValueVisitor<Set<OWLAxiom>, RuntimeException> {

    private OWLEntity subject;

    private Mode mode;

    PropertyValueTranslator(OWLEntity subject,
                            Mode mode) {
        this.subject = subject;
        this.mode = mode;
    }

    @Override
    public Set<OWLAxiom> visit(final PropertyClassValue propertyValue) {
        final OWLDataFactory df = DataFactory.get();
        final Set<OWLClassExpression> classExpressions = new HashSet<>();
        classExpressions.add(df.getOWLObjectSomeValuesFrom(propertyValue.getProperty().getEntity(), propertyValue
                .getValue()
                .getEntity()));
        if(mode == Mode.MAXIMAL) {
            classExpressions.add(df.getOWLObjectMinCardinality(1, propertyValue.getProperty().getEntity(), propertyValue.getValue().getEntity()));
        }
        return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                Set<OWLAxiom> result = new HashSet<>();
                for(OWLClassExpression ce : classExpressions) {
                    result.add(df.getOWLSubClassOfAxiom(subject, ce));
                }
                return result;
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                Set<OWLAxiom> result = new HashSet<>();
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
        final OWLClassExpression classExpression = df.getOWLObjectHasValue(propertyValue
                                                                                   .getProperty()
                                                                                   .getEntity(), propertyValue
                                                                                   .getValue()
                                                                                   .getEntity());
        return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                return Collections.singleton(df.getOWLObjectPropertyAssertionAxiom(propertyValue
                                                                                           .getProperty()
                                                                                           .getEntity(), subject, propertyValue
                                                                                           .getValue()
                                                                                           .getEntity()));
            }
        });
    }

    @Override
    public Set<OWLAxiom> visit(final PropertyDatatypeValue propertyValue) {
        final OWLDataFactory df = DataFactory.get();
        final Set<OWLClassExpression> classExpressions = new HashSet<>();
        classExpressions.add(df.getOWLDataSomeValuesFrom(propertyValue.getProperty().getEntity(), propertyValue
                .getValue()
                .getEntity()));
        if(mode == Mode.MAXIMAL) {
            classExpressions.add(df.getOWLDataMinCardinality(1, propertyValue.getProperty().getEntity(), propertyValue.getValue().getEntity()));
        }
        return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                Set<OWLAxiom> result = new HashSet<>();
                for(OWLClassExpression ce : classExpressions) {
                    result.add(df.getOWLSubClassOfAxiom(subject, ce));
                }
                return result;
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                Set<OWLAxiom> result = new HashSet<>();
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
        final OWLClassExpression classExpression = df.getOWLDataHasValue(propertyValue
                                                                                 .getProperty()
                                                                                 .getEntity(), propertyValue.getValue().getLiteral());
        return subject.accept(new OWLEntityVisitorExAdapter<Set<OWLAxiom>>(null) {
            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLClass subject) {
                return Collections.singleton(df.getOWLSubClassOfAxiom(subject, classExpression));
            }

            @Nonnull
            @Override
            public Set<OWLAxiom> visit(OWLNamedIndividual subject) {
                return Collections.singleton(df.getOWLDataPropertyAssertionAxiom(propertyValue
                                                                                         .getProperty()
                                                                                         .getEntity(), subject, propertyValue
                                                                                         .getValue()
                                                                                         .getObject()));
            }
        });
    }

    @Override
    public Set<OWLAxiom> visit(PropertyAnnotationValue propertyValue) {
        OWLDataFactory df = DataFactory.get();
        Optional<OWLAnnotationValue> annotationValue = propertyValue.getValue().asAnnotationValue();
        if(annotationValue.isPresent()) {
            return Collections.singleton(df.getOWLAnnotationAssertionAxiom(propertyValue
                                                                                   .getProperty()
                                                                                   .getEntity(), subject.getIRI(), annotationValue
                                                                                   .get()));
        }
        else {
            return null;
        }
    }
}
