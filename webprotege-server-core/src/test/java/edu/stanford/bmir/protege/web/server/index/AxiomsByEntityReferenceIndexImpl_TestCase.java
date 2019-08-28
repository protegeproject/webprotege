package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
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
 * 2019-08-07
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsByEntityReferenceIndexImpl_TestCase {

    AxiomsByEntityReferenceIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLEntity entity;

    @Mock
    private OWLAxiom axiom;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any())).thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId)).thenReturn(Optional.of(ontology));
        when(ontology.getReferencingAxioms(entity)).thenReturn(Collections.singleton(axiom));
        impl = new AxiomsByEntityReferenceIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldRetrieveAxiomByEntityReferenceForKnownOntology() {
        var axiomStream = impl.getReferencingAxioms(entity, ontologyId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldRetrivedEmptyStreamForUnknownOntology() {
        var axiomStream = impl.getReferencingAxioms(entity, mock(OWLOntologyID.class));
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullOntology() {
        impl.getReferencingAxioms(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldNPEForNullOntologyId() {
        impl.getReferencingAxioms(entity, null);
    }
}
