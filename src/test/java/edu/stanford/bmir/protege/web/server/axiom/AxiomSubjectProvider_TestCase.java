package edu.stanford.bmir.protege.web.server.axiom;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider;
import edu.stanford.bmir.protege.web.shared.axiom.OWLObjectSelector;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.semanticweb.owlapi.model.*;

import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomSubjectProvider_TestCase {


    @Mock
    private OWLEntity entity;

    @Mock
    private OWLClassExpression classExpression;

    @Mock
    private Set<OWLClassExpression> classExpressionsSet;

    @Mock
    private OWLObjectPropertyExpression objectPropertyExpression;

    @Mock
    private Set<OWLObjectPropertyExpression> objectPropertyExpressionSet;

    @Mock
    private OWLDataPropertyExpression dataPropertyExpression;

    @Mock
    private Set<OWLDataPropertyExpression> dataPropertyExpressionSet;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLIndividual individual;

    @Mock
    private Set<OWLIndividual> individualSet;

    @Mock
    private OWLObjectSelector<OWLClassExpression> classExpressionSelector;

    @Mock
    private OWLObjectSelector<OWLObjectPropertyExpression> objectPropertyExpressionSelector;

    @Mock
    private OWLObjectSelector<OWLDataPropertyExpression> dataPropertyExpressionSelector;

    @Mock
    private OWLObjectSelector<OWLIndividual> individualSelector;

    @Mock
    private SWRLAtom atom;

    @Mock
    private Set<SWRLAtom> atomSet;

    @Mock
    private OWLObjectSelector<SWRLAtom> atomSelector;

    private AxiomSubjectProvider subjectProvider;

    @Before
    public void setUp() throws Exception {
        subjectProvider = new AxiomSubjectProvider(
                classExpressionSelector,
                objectPropertyExpressionSelector,
                dataPropertyExpressionSelector,
                individualSelector,
                atomSelector
        );

        when(classExpressionSelector.selectOne(classExpressionsSet)).thenReturn(Optional.of(classExpression));
        when(objectPropertyExpressionSelector.selectOne(objectPropertyExpressionSet)).thenReturn(Optional.of(objectPropertyExpression));
        when(dataPropertyExpressionSelector.selectOne(dataPropertyExpressionSet)).thenReturn(Optional.of(dataPropertyExpression));
        when(individualSelector.selectOne(individualSet)).thenReturn(Optional.of(individual));
        when(atomSelector.selectOne(atomSet)).thenReturn(Optional.of(atom));
    }

    /**
     * Mocks the visit method for a given axiom.
     *
     * @param axiom      The axiom.
     * @param axiomClass The type of class of the axiom.
     * @param <A>        The type of axiom.
     */
    private <A extends OWLAxiom> void mockVisit(final OWLAxiom axiom, final Class<A> axiomClass) {
        when(axiom.accept(any(OWLAxiomVisitorEx.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object visitorImplementation = invocationOnMock.getArguments()[0];
                Method method = visitorImplementation.getClass().getMethod("visit", axiomClass);
                method.setAccessible(true);
                return method.invoke(visitorImplementation, axiom);
            }
        });
    }

    private <A extends OWLAxiom> A mockAxiom(Class<A> axiomClass) {
        final A axiom = mock(axiomClass);
        mockVisit(axiom, axiomClass);
        return axiom;
    }

    private Matcher<Optional<? extends OWLObject>> isOptionalOf(OWLObject obj) {
        return Matchers.<Optional<? extends OWLObject>>is(Optional.of(obj));
    }

    private Optional<? extends OWLObject> subjectOf(OWLAxiom axiom) {
        return subjectProvider.getSubject(axiom);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ClassExpressionSelector_IsNull() {
        new AxiomSubjectProvider(
                null,
                objectPropertyExpressionSelector,
                dataPropertyExpressionSelector,
                individualSelector,
                atomSelector
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ObjectPropertyExpressionSelector_IsNull() {
        new AxiomSubjectProvider(
                classExpressionSelector,
                null,
                dataPropertyExpressionSelector,
                individualSelector,
                atomSelector
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_DataPropertyExpressionSelector_IsNull() {
        new AxiomSubjectProvider(
                classExpressionSelector,
                objectPropertyExpressionSelector,
                null,
                individualSelector,
                atomSelector
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_IndividualSelector_IsNull() {
        new AxiomSubjectProvider(
                classExpressionSelector,
                objectPropertyExpressionSelector,
                dataPropertyExpressionSelector,
                null,
                atomSelector
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_AtomSelector_IsNull() {
        new AxiomSubjectProvider(
                classExpressionSelector,
                objectPropertyExpressionSelector,
                dataPropertyExpressionSelector,
                individualSelector,
                null
        );
    }

    @Test
    public void shouldProvide_OWLDeclarationAxiom_Subject() {
        OWLDeclarationAxiom axiom = mockAxiom(OWLDeclarationAxiom.class);
        when(axiom.getEntity()).thenReturn(entity);
        assertThat(subjectOf(axiom), isOptionalOf(entity));
    }

    @Test
    public void shouldProvide_OWLSubClassOfAxiom_Subject() {
        OWLSubClassOfAxiom axiom = mockAxiom(OWLSubClassOfAxiom.class);
        when(axiom.getSubClass()).thenReturn(classExpression);
        assertThat(subjectOf(axiom), isOptionalOf(classExpression));
    }


    @Test
    public void shouldProvide_OWLNegativeObjectPropertyAssertionAxiom_Subject() {
        OWLNegativeObjectPropertyAssertionAxiom axiom = mockAxiom(OWLNegativeObjectPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLAsymmetricObjectPropertyAxiom_Subject() {
        OWLAsymmetricObjectPropertyAxiom axiom = mockAxiom(OWLAsymmetricObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLReflexiveObjectPropertyAxiom_Subject() {
        OWLReflexiveObjectPropertyAxiom axiom = mockAxiom(OWLReflexiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLDisjointClassesAxiom_Subject() {
        OWLDisjointClassesAxiom axiom = mockAxiom(OWLDisjointClassesAxiom.class);
        when(axiom.getClassExpressions()).thenReturn(classExpressionsSet);
        assertThat(subjectOf(axiom), isOptionalOf(classExpression));
    }

    @Test
    public void shouldProvide_OWLDataPropertyDomainAxiom_Subject() {
        OWLDataPropertyDomainAxiom axiom = mockAxiom(OWLDataPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(dataPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(dataPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLObjectPropertyDomainAxiom_Subject() {
        OWLObjectPropertyDomainAxiom axiom = mockAxiom(OWLObjectPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLEquivalentObjectPropertiesAxiom_Subject() {
        OWLEquivalentObjectPropertiesAxiom axiom = mockAxiom(OWLEquivalentObjectPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(objectPropertyExpressionSet);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }


    @Test
    public void shouldProvide_OWLNegativeDataPropertyAssertionAxiom_Subject() {
        OWLNegativeDataPropertyAssertionAxiom axiom = mockAxiom(OWLNegativeDataPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLDifferentIndividualsAxiom_Subject() {
        OWLDifferentIndividualsAxiom axiom = mockAxiom(OWLDifferentIndividualsAxiom.class);
        when(axiom.getIndividuals()).thenReturn(individualSet);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLDisjointDataPropertiesAxiom_Subject() {
        OWLDisjointDataPropertiesAxiom axiom = mockAxiom(OWLDisjointDataPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(dataPropertyExpressionSet);
        assertThat(subjectOf(axiom), isOptionalOf(dataPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLDisjointObjectPropertiesAxiom_Subject() {
        OWLDisjointObjectPropertiesAxiom axiom = mockAxiom(OWLDisjointObjectPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(objectPropertyExpressionSet);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLObjectPropertyRangeAxiom_Subject() {
        OWLObjectPropertyRangeAxiom axiom = mockAxiom(OWLObjectPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLObjectPropertyAssertionAxiom_Subject() {
        OWLObjectPropertyAssertionAxiom axiom = mockAxiom(OWLObjectPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLFunctionalObjectPropertyAxiom_Subject() {
        OWLFunctionalObjectPropertyAxiom axiom = mockAxiom(OWLFunctionalObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLSubObjectPropertyOfAxiom_Subject() {
        OWLSubObjectPropertyOfAxiom axiom = mockAxiom(OWLSubObjectPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLDisjointUnionAxiom_Subject() {
        OWLDisjointUnionAxiom axiom = mockAxiom(OWLDisjointUnionAxiom.class);
        OWLClass cls = mock(OWLClass.class);
        when(axiom.getOWLClass()).thenReturn(cls);
        assertThat(subjectOf(axiom), isOptionalOf(cls));
    }

    @Test
    public void shouldProvide_OWLSymmetricObjectPropertyAxiom_Subject() {
        OWLSymmetricObjectPropertyAxiom axiom = mockAxiom(OWLSymmetricObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLDataPropertyRangeAxiom_Subject() {
        OWLDataPropertyRangeAxiom axiom = mockAxiom(OWLDataPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(dataPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(dataPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLFunctionalDataPropertyAxiom_Subject() {
        OWLFunctionalDataPropertyAxiom axiom = mockAxiom(OWLFunctionalDataPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(dataPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(dataPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLEquivalentDataPropertiesAxiom_Subject() {
        OWLEquivalentDataPropertiesAxiom axiom = mockAxiom(OWLEquivalentDataPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(dataPropertyExpressionSet);
        assertThat(subjectOf(axiom), isOptionalOf(dataPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLClassAssertionAxiom_Subject() {
        OWLClassAssertionAxiom axiom = mockAxiom(OWLClassAssertionAxiom.class);
        when(axiom.getIndividual()).thenReturn(individual);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLEquivalentClassesAxiom_Subject() {
        OWLEquivalentClassesAxiom axiom = mockAxiom(OWLEquivalentClassesAxiom.class);
        when(axiom.getClassExpressions()).thenReturn(classExpressionsSet);
        assertThat(subjectOf(axiom), isOptionalOf(classExpression));
    }

    @Test
    public void shouldProvide_OWLDataPropertyAssertionAxiom_Subject() {
        OWLDataPropertyAssertionAxiom axiom = mockAxiom(OWLDataPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLTransitiveObjectPropertyAxiom_Subject() {
        OWLTransitiveObjectPropertyAxiom axiom = mockAxiom(OWLTransitiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLIrreflexiveObjectPropertyAxiom_Subject() {
        OWLIrreflexiveObjectPropertyAxiom axiom = mockAxiom(OWLIrreflexiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLSubDataPropertyOfAxiom_Subject() {
        OWLSubDataPropertyOfAxiom axiom = mockAxiom(OWLSubDataPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(dataPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(dataPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLInverseFunctionalObjectPropertyAxiom_Subject() {
        OWLInverseFunctionalObjectPropertyAxiom axiom = mockAxiom(OWLInverseFunctionalObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLSameIndividualAxiom_Subject() {
        OWLSameIndividualAxiom axiom = mockAxiom(OWLSameIndividualAxiom.class);
        when(axiom.getIndividuals()).thenReturn(individualSet);
        assertThat(subjectOf(axiom), isOptionalOf(individual));
    }

    @Test
    public void shouldProvide_OWLSubPropertyChainOfAxiom_Subject() {
        OWLSubPropertyChainOfAxiom axiom = mockAxiom(OWLSubPropertyChainOfAxiom.class);
        when(axiom.getSuperProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLInverseObjectPropertiesAxiom_Subject() {
        OWLInverseObjectPropertiesAxiom axiom = mockAxiom(OWLInverseObjectPropertiesAxiom.class);
        when(axiom.getFirstProperty()).thenReturn(objectPropertyExpression);
        assertThat(subjectOf(axiom), isOptionalOf(objectPropertyExpression));
    }

    @Test
    public void shouldProvide_OWLHasKeyAxiom_Subject() {
        OWLHasKeyAxiom axiom = mockAxiom(OWLHasKeyAxiom.class);
        when(axiom.getClassExpression()).thenReturn(classExpression);
        assertThat(subjectOf(axiom), isOptionalOf(classExpression));
    }

    @Test
    public void shouldProvide_OWLDatatypeDefinitionAxiom_Subject() {
        OWLDatatypeDefinitionAxiom axiom = mockAxiom(OWLDatatypeDefinitionAxiom.class);
        OWLDatatype datatype = mock(OWLDatatype.class);
        when(axiom.getDatatype()).thenReturn(datatype);
        assertThat(subjectOf(axiom), isOptionalOf(datatype));
    }

    @Test
    public void shouldProvider_SWRLRule_Subject() {
        SWRLRule axiom = mockAxiom(SWRLRule.class);
        when(axiom.getHead()).thenReturn(atomSet);
        assertThat(subjectOf(axiom), isOptionalOf(atom));
    }

    @Test
    public void shouldProvide_OWLAnnotationAssertionAxiom_Subject() {
        OWLAnnotationAssertionAxiom axiom = mockAxiom(OWLAnnotationAssertionAxiom.class);
        IRI iri = mock(IRI.class);
        when(axiom.getSubject()).thenReturn(iri);
        assertThat(subjectOf(axiom), isOptionalOf(iri));
    }

    @Test
    public void shouldProvide_OWLSubAnnotationPropertyOfAxiom_Subject() {
        OWLSubAnnotationPropertyOfAxiom axiom = mockAxiom(OWLSubAnnotationPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(annotationProperty);
        assertThat(subjectOf(axiom), isOptionalOf(annotationProperty));
    }

    @Test
    public void shouldProvide_OWLAnnotationPropertyDomainAxiom_Subject() {
        OWLAnnotationPropertyDomainAxiom axiom = mockAxiom(OWLAnnotationPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(annotationProperty);
        assertThat(subjectOf(axiom), isOptionalOf(annotationProperty));
    }

    @Test
    public void shouldProvide_OWLAnnotationPropertyRangeAxiom_Subject() {
        OWLAnnotationPropertyRangeAxiom axiom = mockAxiom(OWLAnnotationPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(annotationProperty);
        assertThat(subjectOf(axiom), isOptionalOf(annotationProperty));
    }
}
