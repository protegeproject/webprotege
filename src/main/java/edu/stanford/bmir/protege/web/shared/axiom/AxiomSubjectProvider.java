package edu.stanford.bmir.protege.web.shared.axiom;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.object.OWLObjectSelector;
import org.semanticweb.owlapi.model.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/01/15
 */
public class AxiomSubjectProvider {

    private final SubjectProvider subjectProvider;

    /**
     * Constructs an AxiomSubjectProvider.
     * @param classExpressionSelector A selector for class expressions that chooses a class expression to be the
     *                                subject.  This is used for nary class axioms, for example
     *                                {@link OWLEquivalentClassesAxiom} axioms.  Not {@code null}.
     * @param objectPropertyExpressionSelector A selector for object properties that chooses an object property to be
     *                                         the subject.  This is used for nary property axioms, for example,
     *                                         {@link OWLEquivalentObjectPropertiesAxiom}.  Not {@code null}.
     * @param dataPropertyExpressionSelector A selector for data properties that chooses a data property to be the
     *                                       subject.  This is used for nary property axioms, for example
     *                                       {@link OWLEquivalentDataPropertiesAxiom} axioms.  Not {@code null}.
     * @param individualSelector A selector for individuals that chooses an individual to be the subject.  This is used
     *                           for nary individual axioms, for example {@link OWLSameIndividualAxiom} axioms.
     * @param atomSelector A selector for SWRL atoms that chooses an atom to be the subject.  This is used to select
     *                     an atom that is in the head of a {@link SWRLRule} to be the subject. Not {@code null}.
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    public AxiomSubjectProvider(OWLObjectSelector<OWLClassExpression> classExpressionSelector,
                                OWLObjectSelector<OWLObjectPropertyExpression> objectPropertyExpressionSelector,
                                OWLObjectSelector<OWLDataPropertyExpression> dataPropertyExpressionSelector,
                                OWLObjectSelector<OWLIndividual> individualSelector,
                                OWLObjectSelector<SWRLAtom> atomSelector) {
        subjectProvider = new SubjectProvider(
                classExpressionSelector,
                objectPropertyExpressionSelector,
                dataPropertyExpressionSelector,
                individualSelector,
                atomSelector);
    }

    /**
     * Gets the subject of the specified axiom.
     * @param axiom The axiom.  Not {@code null}.
     * @return The (possibly absent) subject.  Not {@code null}.
     */
    public Optional<? extends OWLObject> getSubject(OWLAxiom axiom) {
        return checkNotNull(axiom).accept(subjectProvider);
    }




    private static class SubjectProvider implements OWLAxiomVisitorEx<Optional<? extends OWLObject>> {

        private final OWLObjectSelector<OWLClassExpression> classExpressionSelector;

        private final OWLObjectSelector<OWLObjectPropertyExpression> objectPropertyExpressionSelector;

        private final OWLObjectSelector<OWLDataPropertyExpression> dataPropertyExpressionSelector;

        private final OWLObjectSelector<OWLIndividual> individualSelector;

        private final OWLObjectSelector<SWRLAtom> atomSelector;


        public SubjectProvider(OWLObjectSelector<OWLClassExpression> classExpressionSelector,
                               OWLObjectSelector<OWLObjectPropertyExpression> objectPropertyExpressionSelector,
                               OWLObjectSelector<OWLDataPropertyExpression> dataPropertyExpressionSelector,
                               OWLObjectSelector<OWLIndividual> individualSelector,
                               OWLObjectSelector<SWRLAtom> atomSelector) {
            this.classExpressionSelector = checkNotNull(classExpressionSelector);
            this.objectPropertyExpressionSelector = checkNotNull(objectPropertyExpressionSelector);
            this.individualSelector = checkNotNull(individualSelector);
            this.dataPropertyExpressionSelector = checkNotNull(dataPropertyExpressionSelector);
            this.atomSelector = checkNotNull(atomSelector);
        }

        public static Optional<? extends OWLObject> wrap(OWLObject object) {
            return Optional.of(object);
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSubClassOfAxiom axiom) {
            return wrap(axiom.getSubClass());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLReflexiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDisjointClassesAxiom axiom) {
            return classExpressionSelector.selectOne(axiom.getClassExpressions());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDataPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLObjectPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            return objectPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDifferentIndividualsAxiom axiom) {
            return individualSelector.selectOne(axiom.getIndividuals());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDisjointDataPropertiesAxiom axiom) {
            return dataPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDisjointObjectPropertiesAxiom axiom) {
            return objectPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLObjectPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLObjectPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLFunctionalObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSubObjectPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDisjointUnionAxiom axiom) {
            return wrap(axiom.getOWLClass());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDeclarationAxiom axiom) {
            return wrap(axiom.getEntity());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLAnnotationAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSymmetricObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDataPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLFunctionalDataPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLEquivalentDataPropertiesAxiom axiom) {
            return dataPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLClassAssertionAxiom axiom) {
            return wrap(axiom.getIndividual());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLEquivalentClassesAxiom axiom) {
            return classExpressionSelector.selectOne(axiom.getClassExpressions());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDataPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLTransitiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSubDataPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSameIndividualAxiom axiom) {
            return individualSelector.selectOne(axiom.getIndividuals());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSubPropertyChainOfAxiom axiom) {
            return wrap(axiom.getSuperProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLInverseObjectPropertiesAxiom axiom) {
            return wrap(axiom.getFirstProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLHasKeyAxiom axiom) {
            return wrap(axiom.getClassExpression());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLDatatypeDefinitionAxiom axiom) {
            return wrap(axiom.getDatatype());
        }

        @Override
        public Optional<? extends OWLObject> visit(SWRLRule axiom) {
            return atomSelector.selectOne(axiom.getHead());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLAnnotationPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Override
        public Optional<? extends OWLObject> visit(OWLAnnotationPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }
    }
}
