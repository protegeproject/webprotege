package edu.stanford.bmir.protege.web.shared.axiom;

import edu.stanford.bmir.protege.web.shared.object.OWLObjectSelector;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

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
    @Inject
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

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSubClassOfAxiom axiom) {
            return wrap(axiom.getSubClass());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLNegativeObjectPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLAsymmetricObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLReflexiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDisjointClassesAxiom axiom) {
            return classExpressionSelector.selectOne(axiom.getClassExpressions());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDataPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLObjectPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLEquivalentObjectPropertiesAxiom axiom) {
            return objectPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLNegativeDataPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDifferentIndividualsAxiom axiom) {
            return individualSelector.selectOne(axiom.getIndividuals());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDisjointDataPropertiesAxiom axiom) {
            return dataPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDisjointObjectPropertiesAxiom axiom) {
            return objectPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLObjectPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLObjectPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLFunctionalObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSubObjectPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDisjointUnionAxiom axiom) {
            return wrap(axiom.getOWLClass());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDeclarationAxiom axiom) {
            return wrap(axiom.getEntity());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLAnnotationAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSymmetricObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDataPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLFunctionalDataPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLEquivalentDataPropertiesAxiom axiom) {
            return dataPropertyExpressionSelector.selectOne(axiom.getProperties());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLClassAssertionAxiom axiom) {
            return wrap(axiom.getIndividual());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLEquivalentClassesAxiom axiom) {
            return classExpressionSelector.selectOne(axiom.getClassExpressions());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDataPropertyAssertionAxiom axiom) {
            return wrap(axiom.getSubject());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLTransitiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLIrreflexiveObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSubDataPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLInverseFunctionalObjectPropertyAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSameIndividualAxiom axiom) {
            return individualSelector.selectOne(axiom.getIndividuals());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSubPropertyChainOfAxiom axiom) {
            return wrap(axiom.getSuperProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLInverseObjectPropertiesAxiom axiom) {
            return wrap(axiom.getFirstProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLHasKeyAxiom axiom) {
            return wrap(axiom.getClassExpression());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLDatatypeDefinitionAxiom axiom) {
            return wrap(axiom.getDatatype());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull SWRLRule axiom) {
            return atomSelector.selectOne(axiom.getHead());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLSubAnnotationPropertyOfAxiom axiom) {
            return wrap(axiom.getSubProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLAnnotationPropertyDomainAxiom axiom) {
            return wrap(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Optional<? extends OWLObject> visit(@Nonnull OWLAnnotationPropertyRangeAxiom axiom) {
            return wrap(axiom.getProperty());
        }
    }
}
