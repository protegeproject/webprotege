package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.impl.ClassAssertionAxiomsByIndividualIndexImpl;
import edu.stanford.bmir.protege.web.server.index.impl.OntologyIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassAssertionAxiomsByIndividualIndexImpl_TestCase {

    private ClassAssertionAxiomsByIndividualIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLIndividual individual;

    @Mock
    private OWLClassAssertionAxiom axiom;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyID))
                .thenReturn(Optional.of(ontology));
        when(ontology.getClassAssertionAxioms(individual))
                .thenReturn(Collections.singleton(axiom));
        impl = new ClassAssertionAxiomsByIndividualIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldGetClassAssertionAxiomForIndividual() {
        var axioms = impl.getClassAssertionAxioms(individual, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getClassAssertionAxioms(individual, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownIndividual() {
        var axioms = impl.getClassAssertionAxioms(mock(OWLIndividual.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getClassAssertionAxioms(individual, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullIndividual() {
        impl.getClassAssertionAxioms(null, ontologyID);
    }
}
