package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.DataPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.impl.PropertyAssertionAxiomsBySubjectIndexImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
@RunWith(MockitoJUnitRunner.class)
public class PropertyAssertionAxiomsBySubjectIndexImpl_TestCase {

    private PropertyAssertionAxiomsBySubjectIndexImpl impl;

    @Mock
    private AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject;

    @Mock
    private ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubject;

    @Mock
    private DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubject;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertion;

    @Mock
    private OWLObjectPropertyAssertionAxiom objectPropertyAssertion;

    @Mock
    private OWLDataPropertyAssertionAxiom dataPropertyAssertion;

    @Mock
    private IRI iri;

    @Before
    public void setUp() {
        when(individual.getIRI()).thenReturn(iri);

        when(annotationAssertionAxiomsBySubject.getAxiomsForSubject(any(), any()))
                .thenReturn(Stream.empty());
        when(annotationAssertionAxiomsBySubject.getAxiomsForSubject(iri, ontologyId))
                .thenReturn(Stream.of(annotationAssertion));

        when(objectPropertyAssertionAxiomsBySubject.getObjectPropertyAssertions(any(), any()))
                .thenReturn(Stream.empty());
        when(objectPropertyAssertionAxiomsBySubject.getObjectPropertyAssertions(individual, ontologyId))
                .thenReturn(Stream.of(objectPropertyAssertion));

        when(dataPropertyAssertionAxiomsBySubject.getDataPropertyAssertions(any(), any()))
                .thenReturn(Stream.empty());

        when(dataPropertyAssertionAxiomsBySubject.getDataPropertyAssertions(individual, ontologyId))
                .thenReturn(Stream.of(dataPropertyAssertion));
        impl = new PropertyAssertionAxiomsBySubjectIndexImpl(annotationAssertionAxiomsBySubject,
                                                             objectPropertyAssertionAxiomsBySubject,
                                                             dataPropertyAssertionAxiomsBySubject);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(annotationAssertionAxiomsBySubject,
                                                              dataPropertyAssertionAxiomsBySubject,
                                                              objectPropertyAssertionAxiomsBySubject));
    }

    @Test
    public void shouldGetAssertionsForNamedIndividual() {
        var axioms = impl.getPropertyAssertions(individual, ontologyId).collect(toSet());
        assertThat(axioms, containsInAnyOrder(annotationAssertion, objectPropertyAssertion, dataPropertyAssertion));
    }

    @Test
    public void shouldGetEmptyForUnknownSubject() {
        var axioms = impl.getPropertyAssertions(mock(OWLNamedIndividual.class), ontologyId).collect(toSet());
        assertThat(axioms, is(empty()));
    }

    @Test
    public void shouldGetEmptyForUnknownOntologyId() {
        var axioms = impl.getPropertyAssertions(individual, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms, is(empty()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfSubjectIsNull() {
        impl.getPropertyAssertions(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getPropertyAssertions(individual, null);
    }
}
