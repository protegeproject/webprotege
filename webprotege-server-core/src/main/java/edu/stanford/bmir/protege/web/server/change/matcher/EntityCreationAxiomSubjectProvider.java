package edu.stanford.bmir.protege.web.server.change.matcher;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-04
 */
public class EntityCreationAxiomSubjectProvider {

    private final OWLAxiomVisitorExAdapter<IRI> entityCreationAxiomVisitor = new OWLAxiomVisitorExAdapter<>(null) {
        @Override
        public IRI visit(OWLDeclarationAxiom axiom) {
            return axiom.getEntity().getIRI();
        }

        @Override
        public IRI visit(OWLClassAssertionAxiom axiom) {
            var individual = axiom.getIndividual();
            if(individual.isAnonymous()) {
                return null;
            }
            var classExpression = axiom.getClassExpression();
            if(classExpression.isAnonymous()) {
                return null;
            }
            return individual.asOWLNamedIndividual().getIRI();
        }

        @Override
        public IRI visit(OWLSubClassOfAxiom axiom) {
            if(axiom.getSubClass().isAnonymous()) {
                return null;
            }
            if(axiom.getSuperClass().isAnonymous()) {
                return null;
            }
            return axiom.getSubClass().asOWLClass().getIRI();
        }

        @Override
        public IRI visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            return axiom.getSubProperty().getIRI();
        }

        @Override
        public IRI visit(OWLSubObjectPropertyOfAxiom axiom) {
            if(axiom.getSubProperty().isAnonymous()) {
                return null;
            }
            if(axiom.getSuperProperty().isAnonymous()) {
                return null;
            }
            return axiom.getSubProperty().asOWLObjectProperty().getIRI();
        }

        @Override
        public IRI visit(OWLSubDataPropertyOfAxiom axiom) {
            if(axiom.getSubProperty().isAnonymous()) {
                return null;
            }
            if(axiom.getSuperProperty().isAnonymous()) {
                return null;
            }
            return axiom.getSubProperty().asOWLDataProperty().getIRI();
        }

        @Override
        public IRI visit(OWLAnnotationAssertionAxiom axiom) {
            if(!(axiom.getSubject() instanceof IRI)) {
                return null;
            }
            return (IRI) axiom.getSubject();
        }
    };

    @SuppressWarnings("ConstantConditions")
    public Optional<IRI> getEntityCreationAxiomSubject(@Nonnull OWLAxiom axiom) {
        var subject = axiom.accept(entityCreationAxiomVisitor);
        return Optional.ofNullable(subject);
    }

}
