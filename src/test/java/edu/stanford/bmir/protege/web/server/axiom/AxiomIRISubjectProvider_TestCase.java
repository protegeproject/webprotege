package edu.stanford.bmir.protege.web.server.axiom;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomIRISubjectProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.semanticweb.owlapi.model.*;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomIRISubjectProvider_TestCase {

    @Mock
    private Comparator<IRI> iriComparator;

    private AxiomIRISubjectProvider provider;

    @Mock
    private OWLEntity entity;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLDatatype datatype;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLObjectPropertyExpression objectPropertyExpression;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLDataPropertyExpression dataPropertyExpression;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual individual, individualB;

    @Mock
    private OWLAnonymousIndividual anonymousIndividual;

    @Mock
    private OWLClassExpression classExpression;

    @Mock
    private IRI iri, iriB;

    private Optional<IRI> iriOptional;

    private Optional<IRI> absent;


    @Before
    public void setUp() throws Exception {
        when(entity.getIRI()).thenReturn(iri);

        mockEntity(cls);
        mockEntity(objectProperty);
        mockEntity(dataProperty);
        mockEntity(annotationProperty);
        mockEntity(individual);
        mockEntity(individualB);
        mockEntity(datatype);

        iriOptional = Optional.of(iri);
        provider = new AxiomIRISubjectProvider(iriComparator);

        when(iriComparator.compare(iriB, iri)).thenReturn(1);
        when(iriComparator.compare(iri, iriB)).thenReturn(-1);
        absent = Optional.empty();
        anonymousIndividual = mock(OWLAnonymousIndividual.class);
    }

    private <E extends OWLEntity> void mockEntity(E entity) {
        when(entity.getIRI()).thenReturn(iri);
    }

    /**
     * Mocks the visit method for a given axiom.
     * @param axiom The axiom.
     * @param axiomClass The type of class of the axiom.
     * @param <A> The type of axiom.
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

    @Test
    public void shouldReturnSubjectFor_OWLDeclarationAxiom() {
        OWLDeclarationAxiom axiom = mockAxiom(OWLDeclarationAxiom.class);
        when(axiom.getEntity()).thenReturn(entity);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLSubClassOfAxiom() {
        OWLSubClassOfAxiom axiom = mockAxiom(OWLSubClassOfAxiom.class);
        when(axiom.getSubClass()).thenReturn(cls);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLSubClassOfAxiom() {
        OWLSubClassOfAxiom axiom = mockAxiom(OWLSubClassOfAxiom.class);
        when(axiom.getSubClass()).thenReturn(classExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLNegativeObjectPropertyAssertionAxiom() {
        OWLNegativeObjectPropertyAssertionAxiom axiom = mockAxiom(OWLNegativeObjectPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLNegativeObjectPropertyAssertionAxiom() {
        OWLNegativeObjectPropertyAssertionAxiom axiom = mockAxiom(OWLNegativeObjectPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(anonymousIndividual);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLAsymmetricObjectPropertyAxiom() {
        OWLAsymmetricObjectPropertyAxiom axiom = mockAxiom(OWLAsymmetricObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_Absent_OWLAsymmetricObjectPropertyAxiom() {
        OWLAsymmetricObjectPropertyAxiom axiom = mockAxiom(OWLAsymmetricObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLReflexiveObjectPropertyAxiom() {
        OWLReflexiveObjectPropertyAxiom axiom = mockAxiom(OWLReflexiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_Absent_OWLReflexiveObjectPropertyAxiom() {
        OWLReflexiveObjectPropertyAxiom axiom = mockAxiom(OWLReflexiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDisjointClassesAxiom() {
        OWLDisjointClassesAxiom axiom = mockAxiom(OWLDisjointClassesAxiom.class);
        when(axiom.getClassExpressions()).thenReturn(Sets.newHashSet(classExpression, cls));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLDisjointClassesAxiom() {
        OWLDisjointClassesAxiom axiom = mockAxiom(OWLDisjointClassesAxiom.class);
        when(axiom.getClassExpressions()).thenReturn(Sets.newHashSet(classExpression));
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDataPropertyDomainAxiom() {
        OWLDataPropertyDomainAxiom axiom = mockAxiom(OWLDataPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(dataProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLDataPropertyDomainAxiom() {
        OWLDataPropertyDomainAxiom axiom = mockAxiom(OWLDataPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(mock(OWLDataPropertyExpression.class));
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLObjectPropertyDomainAxiom() {
        OWLObjectPropertyDomainAxiom axiom = mockAxiom(OWLObjectPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLObjectPropertyDomainAxiom() {
        OWLObjectPropertyDomainAxiom axiom = mockAxiom(OWLObjectPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLEquivalentObjectPropertiesAxiom() {
        OWLEquivalentObjectPropertiesAxiom axiom = mockAxiom(OWLEquivalentObjectPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(objectProperty, objectPropertyExpression));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLEquivalentObjectPropertiesAxiom() {
        OWLEquivalentObjectPropertiesAxiom axiom = mockAxiom(OWLEquivalentObjectPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(objectPropertyExpression));
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLNegativeDataPropertyAssertionAxiom() {
        OWLNegativeDataPropertyAssertionAxiom axiom = mockAxiom(OWLNegativeDataPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLNegativeDataPropertyAssertionAxiom() {
        OWLNegativeDataPropertyAssertionAxiom axiom = mockAxiom(OWLNegativeDataPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(anonymousIndividual);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDifferentIndividualsAxiom() {
        OWLDifferentIndividualsAxiom axiom = mockAxiom(OWLDifferentIndividualsAxiom.class);
        when(axiom.getIndividuals()).thenReturn(Sets.newHashSet(individual, individualB));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLDifferentIndividualsAxiom() {
        OWLDifferentIndividualsAxiom axiom = mockAxiom(OWLDifferentIndividualsAxiom.class);
        when(axiom.getIndividuals()).thenReturn(Sets.newHashSet(anonymousIndividual));
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDisjointDataPropertiesAxiom() {
        OWLDisjointDataPropertiesAxiom axiom = mockAxiom(OWLDisjointDataPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(dataProperty));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLDisjointDataPropertiesAxiom() {
        OWLDisjointDataPropertiesAxiom axiom = mockAxiom(OWLDisjointDataPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(dataPropertyExpression));
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDisjointObjectPropertiesAxiom() {
        OWLDisjointObjectPropertiesAxiom axiom = mockAxiom(OWLDisjointObjectPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(objectProperty));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLDisjointObjectPropertiesAxiom() {
        OWLDisjointObjectPropertiesAxiom axiom = mockAxiom(OWLDisjointObjectPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(objectPropertyExpression));
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLObjectPropertyRangeAxiom() {
        OWLObjectPropertyRangeAxiom axiom = mockAxiom(OWLObjectPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLObjectPropertyRangeAxiom() {
        OWLObjectPropertyRangeAxiom axiom = mockAxiom(OWLObjectPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLObjectPropertyAssertionAxiom() {
        OWLObjectPropertyAssertionAxiom axiom = mockAxiom(OWLObjectPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturn_Absent_SubjectFor_OWLObjectPropertyAssertionAxiom() {
        OWLObjectPropertyAssertionAxiom axiom = mockAxiom(OWLObjectPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(anonymousIndividual);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLFunctionalObjectPropertyAxiom() {
        OWLFunctionalObjectPropertyAxiom axiom = mockAxiom(OWLFunctionalObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLFunctionalObjectPropertyAxiom() {
        OWLFunctionalObjectPropertyAxiom axiom = mockAxiom(OWLFunctionalObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLSubObjectPropertyOfAxiom() {
        OWLSubObjectPropertyOfAxiom axiom = mockAxiom(OWLSubObjectPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLSubObjectPropertyOfAxiom() {
        OWLSubObjectPropertyOfAxiom axiom = mockAxiom(OWLSubObjectPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLDisjointUnionAxiom() {
        OWLDisjointUnionAxiom axiom = mockAxiom(OWLDisjointUnionAxiom.class);
        when(axiom.getOWLClass()).thenReturn(cls);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturnSubjectFor_OWLSymmetricObjectPropertyAxiom() {
        OWLSymmetricObjectPropertyAxiom axiom = mockAxiom(OWLSymmetricObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturn_Absent_SubjectFor_OWLSymmetricObjectPropertyAxiom() {
        OWLSymmetricObjectPropertyAxiom axiom = mockAxiom(OWLSymmetricObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDataPropertyRangeAxiom() {
        OWLDataPropertyRangeAxiom axiom = mockAxiom(OWLDataPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(dataProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturnSubjectFor_OWLFunctionalDataPropertyAxiom() {
        OWLFunctionalDataPropertyAxiom axiom = mockAxiom(OWLFunctionalDataPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(dataProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturnSubjectFor_OWLEquivalentDataPropertiesAxiom() {
        OWLEquivalentDataPropertiesAxiom axiom = mockAxiom(OWLEquivalentDataPropertiesAxiom.class);
        when(axiom.getProperties()).thenReturn(Sets.newHashSet(dataProperty, dataPropertyExpression));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLClassAssertionAxiom() {
        OWLClassAssertionAxiom axiom = mockAxiom(OWLClassAssertionAxiom.class);
        when(axiom.getIndividual()).thenReturn(individual);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLClassAssertionAxiom() {
        OWLClassAssertionAxiom axiom = mockAxiom(OWLClassAssertionAxiom.class);
        when(axiom.getIndividual()).thenReturn(anonymousIndividual);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLEquivalentClassesAxiom() {
        OWLEquivalentClassesAxiom axiom = mockAxiom(OWLEquivalentClassesAxiom.class);
        when(axiom.getClassExpressions()).thenReturn(Sets.newHashSet(classExpression, cls));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturn_Absent_SubjectFor_OWLEquivalentClassesAxiom() {
        OWLEquivalentClassesAxiom axiom = mockAxiom(OWLEquivalentClassesAxiom.class);
        when(axiom.getClassExpressions()).thenReturn(Sets.newHashSet(classExpression));
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLDataPropertyAssertionAxiom() {
        OWLDataPropertyAssertionAxiom axiom = mockAxiom(OWLDataPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(individual);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLDataPropertyAssertionAxiom() {
        OWLDataPropertyAssertionAxiom axiom = mockAxiom(OWLDataPropertyAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(anonymousIndividual);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLTransitiveObjectPropertyAxiom() {
        OWLTransitiveObjectPropertyAxiom axiom = mockAxiom(OWLTransitiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturn_Absent_SubjectFor_OWLTransitiveObjectPropertyAxiom() {
        OWLTransitiveObjectPropertyAxiom axiom = mockAxiom(OWLTransitiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLIrreflexiveObjectPropertyAxiom() {
        OWLIrreflexiveObjectPropertyAxiom axiom = mockAxiom(OWLIrreflexiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLIrreflexiveObjectPropertyAxiom() {
        OWLIrreflexiveObjectPropertyAxiom axiom = mockAxiom(OWLIrreflexiveObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLSubDataPropertyOfAxiom() {
        OWLSubDataPropertyOfAxiom axiom = mockAxiom(OWLSubDataPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(dataProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLSubDataPropertyOfAxiom() {
        OWLSubDataPropertyOfAxiom axiom = mockAxiom(OWLSubDataPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(dataPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLInverseFunctionalObjectPropertyAxiom() {
        OWLInverseFunctionalObjectPropertyAxiom axiom = mockAxiom(OWLInverseFunctionalObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLInverseFunctionalObjectPropertyAxiom() {
        OWLInverseFunctionalObjectPropertyAxiom axiom = mockAxiom(OWLInverseFunctionalObjectPropertyAxiom.class);
        when(axiom.getProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLSameIndividualAxiom() {
        OWLSameIndividualAxiom axiom = mockAxiom(OWLSameIndividualAxiom.class);
        when(axiom.getIndividuals()).thenReturn(Sets.newHashSet(individual, individualB));
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }
    @Test
    public void shouldReturn_Absent_SubjectFor_OWLSameIndividualAxiom() {
        OWLSameIndividualAxiom axiom = mockAxiom(OWLSameIndividualAxiom.class);
        when(axiom.getIndividuals()).thenReturn(Sets.newHashSet(anonymousIndividual));
        assertThat(provider.getSubject(axiom), is(absent));
    }

    @Test
    public void shouldReturnSubjectFor_OWLSubPropertyChainOfAxiom() {
        OWLSubPropertyChainOfAxiom axiom = mockAxiom(OWLSubPropertyChainOfAxiom.class);
        when(axiom.getSuperProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturn_Absent_SubjectFor_OWLSubPropertyChainOfAxiom() {
        OWLSubPropertyChainOfAxiom axiom = mockAxiom(OWLSubPropertyChainOfAxiom.class);
        when(axiom.getSuperProperty()).thenReturn(objectPropertyExpression);
        assertThat(provider.getSubject(axiom), is(absent));
    }


    @Test
    public void shouldReturnSubjectFor_OWLInverseObjectPropertiesAxiom() {
        OWLInverseObjectPropertiesAxiom axiom = mockAxiom(OWLInverseObjectPropertiesAxiom.class);
        when(axiom.getFirstProperty()).thenReturn(objectProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    @Test
    public void shouldReturnSubjectFor_OWLHasKeyAxiom() {
        OWLHasKeyAxiom axiom = mockAxiom(OWLHasKeyAxiom.class);
        when(axiom.getClassExpression()).thenReturn(cls);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLDatatypeDefinitionAxiom() {
        OWLDatatypeDefinitionAxiom axiom = mockAxiom(OWLDatatypeDefinitionAxiom.class);
        when(axiom.getDatatype()).thenReturn(datatype);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }


    public void visit(SWRLRule axiom) {
        
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLAnnotationAssertionAxiom() {
        OWLAnnotationAssertionAxiom axiom = mockAxiom(OWLAnnotationAssertionAxiom.class);
        when(axiom.getSubject()).thenReturn(iri);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLSubAnnotationPropertyOfAxiom() {
        OWLSubAnnotationPropertyOfAxiom axiom = mockAxiom(OWLSubAnnotationPropertyOfAxiom.class);
        when(axiom.getSubProperty()).thenReturn(annotationProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLAnnotationPropertyDomainAxiom() {
        OWLAnnotationPropertyDomainAxiom axiom = mockAxiom(OWLAnnotationPropertyDomainAxiom.class);
        when(axiom.getProperty()).thenReturn(annotationProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }

    @Test
    public void shouldReturnSubjectFor_OWLAnnotationPropertyRangeAxiom() {
        OWLAnnotationPropertyRangeAxiom axiom = mockAxiom(OWLAnnotationPropertyRangeAxiom.class);
        when(axiom.getProperty()).thenReturn(annotationProperty);
        assertThat(provider.getSubject(axiom), is(iriOptional));
    }
}
