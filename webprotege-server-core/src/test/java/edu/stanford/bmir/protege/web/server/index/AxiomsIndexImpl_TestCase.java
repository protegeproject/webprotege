package edu.stanford.bmir.protege.web.server.index;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsIndexImpl_TestCase {

    private AxiomsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAxiom axiom;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));

        when(ontology.containsAxiom(axiom))
                .thenReturn(false);
        when(ontology.containsAxiomIgnoreAnnotations(axiom))
                .thenReturn(false);

        impl = new AxiomsIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldContainAxiom() {
        when(ontology.containsAxiom(axiom))
                .thenReturn(true);
        assertThat(impl.containsAxiom(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldContainAxiomWithoutAnnotationsIgnoreAnnotations() {
        when(ontology.containsAxiom(axiom))
                .thenReturn(true);
        assertThat(impl.containsAxiomIgnoreAnnotations(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldContainAxiomIgnoreAnnotations() {
        when(ontology.containsAxiom(axiom))
                .thenReturn(false);
        when(ontology.containsAxiomIgnoreAnnotations(axiom))
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
