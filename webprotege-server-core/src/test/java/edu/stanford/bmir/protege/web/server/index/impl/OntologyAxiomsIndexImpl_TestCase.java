package edu.stanford.bmir.protege.web.server.index.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAxiomsIndexImpl_TestCase {

    private OntologyAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private AxiomsByTypeIndexImpl axiomsByTypeIndexImpl;

    @Before
    public void setUp() {
        impl = new OntologyAxiomsIndexImpl(axiomsByTypeIndexImpl);


        when(axiomsByTypeIndexImpl.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(axiomsByTypeIndexImpl.getAxiomsByType(AxiomType.CLASS_ASSERTION, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndexImpl));
    }

    @Test
    public void shouldGetAxioms() {
        var axioms = impl.getAxioms(ontologyId).collect(toSet());
        assertThat(axioms, contains(axiom));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntologyId() {
        var axioms = impl.getAxioms(mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getAxioms(null);
    }

    @Test
    public void shouldContainAxiom() {
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        assertThat(impl.containsAxiom(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldContainAxiomWithoutAnnotationsIgnoreAnnotations() {
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        assertThat(impl.containsAxiomIgnoreAnnotations(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldContainAxiomIgnoreAnnotations() {
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(false);
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        assertThat(impl.containsAxiomIgnoreAnnotations(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldNotContainAxiomInUnknownOntology() {
        assertThat(impl.containsAxiom(axiom, mock(OWLOntologyID.class)), is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIsNullInContainsAxiom() {
        impl.containsAxiom(axiom, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAxiomIsNullInContainsAxiom() {
        impl.containsAxiom(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIsNullInContainsAxiomIgnoreAnnotations() {
        impl.containsAxiomIgnoreAnnotations(axiom, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAxiomIsNullInContainsAxiomIgnoreAnnotations() {
        impl.containsAxiomIgnoreAnnotations(null, ontologyId);
    }
}
