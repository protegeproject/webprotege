package edu.stanford.bmir.protege.web.server.axiom;

import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
public class AxiomIRISubjectProvider {

    private AxiomSubjectProviderVisitor visitor;

    @Inject
    public AxiomIRISubjectProvider(Comparator<IRI> iriComparator) {
        visitor = new AxiomSubjectProviderVisitor(checkNotNull(iriComparator));
    }

    public Optional<IRI> getSubject(OWLAxiom axiom) {
        return axiom.accept(visitor);
    }


    private static class AxiomSubjectProviderVisitor implements OWLAxiomVisitorEx<Optional<IRI>> {

        public static final int BEFORE = -1;

        public static final int SAME = 0;

        public static final int AFTER = 1;


        private Comparator<IRI> iriComparator;

        public AxiomSubjectProviderVisitor(Comparator<IRI> iriComparator) {
            this.iriComparator = iriComparator;
        }

        private Optional<IRI> wrapIterable(Iterable<? extends OWLObject> iterable) {
            Iterator<? extends OWLObject> iterator = iterable.iterator();
            if(!iterator.hasNext()) {
                return empty();
            }
            Optional<IRI> previous = wrap(iterator.next());
            while(iterator.hasNext()) {
                Optional<IRI> current = wrap(iterator.next());
                int diff = compare(previous, current);
                if(diff > 0) {
                    previous = current;
                }
            }
            return previous;
        }

        private int compare(Optional<IRI> iriA, Optional<IRI> iriB) {
            if(iriA.isPresent()) {
                if(iriB.isPresent()) {
                    return iriComparator.compare(iriA.get(), iriB.get());
                }
                else {
                    return BEFORE;
                }
            }
            else {
                if(iriB.isPresent()) {
                    return AFTER;
                }
                else {
                    return SAME;
                }
            }
        }

        private static Optional<IRI> wrap(OWLObject object) {
            if(object instanceof HasIRI) {
                return of(((HasIRI) object).getIRI());
            }
            else if(object instanceof IRI) {
                return of((IRI) object);
            }
            else {
                return empty();
            }
        }

        @Override
        public Optional<IRI> visit(OWLSubClassOfAxiom axiom) {
            return wrap(axiom.getSubClass());
        }

        @Override
        public Optional<IRI> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<IRI> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLReflexiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLDisjointClassesAxiom axiom) {
            return wrapIterable(axiom.getClassExpressions());
        }

        @Override
        public Optional<IRI> visit(OWLDataPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLObjectPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            return wrapIterable(axiom.getProperties());
        }

        @Override
        public Optional<IRI> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<IRI> visit(OWLDifferentIndividualsAxiom axiom) {
            return wrapIterable(axiom.getIndividuals());
        }

        @Override
        public Optional<IRI> visit(OWLDisjointDataPropertiesAxiom axiom) {
            return wrapIterable(axiom.getProperties());
        }

        @Override
        public Optional<IRI> visit(OWLDisjointObjectPropertiesAxiom axiom) {
            return wrapIterable(axiom.getProperties());
        }

        @Override
        public Optional<IRI> visit(OWLObjectPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLObjectPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<IRI> visit(OWLFunctionalObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLSubObjectPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Override
        public Optional<IRI> visit(OWLDisjointUnionAxiom axiom) {
            return wrap(axiom.getOWLClass());
        }

        @Override
        public Optional<IRI> visit(OWLDeclarationAxiom axiom) {
            return wrap(axiom.getEntity());
        }

        @Override
        public Optional<IRI> visit(OWLAnnotationAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<IRI> visit(OWLSymmetricObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLDataPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLFunctionalDataPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLEquivalentDataPropertiesAxiom axiom) {
            return wrapIterable(axiom.getProperties());
        }

        @Override
        public Optional<IRI> visit(OWLClassAssertionAxiom axiom) {
            return wrap(axiom.getIndividual());
        }

        @Override
        public Optional<IRI> visit(OWLEquivalentClassesAxiom axiom) {
            return wrapIterable(axiom.getClassExpressions());
        }

        @Override
        public Optional<IRI> visit(OWLDataPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<IRI> visit(OWLTransitiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLSubDataPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Override
        public Optional<IRI> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLSameIndividualAxiom axiom) {
            return wrapIterable(axiom.getIndividuals());
        }

        @Override
        public Optional<IRI> visit(OWLSubPropertyChainOfAxiom axiom) {
            return wrap(axiom.getSuperProperty());
        }

        @Override
        public Optional<IRI> visit(OWLInverseObjectPropertiesAxiom axiom) {
            return wrap(axiom.getFirstProperty());
        }

        @Override
        public Optional<IRI> visit(OWLHasKeyAxiom axiom) {
            return wrap(axiom.getClassExpression());
        }

        @Override
        public Optional<IRI> visit(OWLDatatypeDefinitionAxiom axiom) {
            return wrap(axiom.getDatatype());
        }

        @Override
        public Optional<IRI> visit(SWRLRule rule) {
            return wrapIterable(rule.getHead());
        }

        @Override
        public Optional<IRI> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Override
        public Optional<IRI> visit(OWLAnnotationPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<IRI> visit(OWLAnnotationPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }
    }
}
