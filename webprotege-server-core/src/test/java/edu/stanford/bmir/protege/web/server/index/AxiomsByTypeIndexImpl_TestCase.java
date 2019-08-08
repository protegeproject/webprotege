package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

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
public class AxiomsByTypeIndexImpl_TestCase {

    private AxiomsByTypeIndexImpl impl;

    @Mock
    private OntologyIndex projectOntologyIndex;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLSubClassOfAxiom axiom;

    @Before
    public void setUp() {
        impl = new AxiomsByTypeIndexImpl(projectOntologyIndex);
        when(projectOntologyIndex.getOntology(any())).thenReturn(Optional.empty());
        when(projectOntologyIndex.getOntology(ontologyId)).thenReturn(Optional.of(ontology));
        when(ontology.getAxioms(any(AxiomType.class))).thenReturn(Collections.emptySet());
        when(ontology.getAxioms(AxiomType.SUBCLASS_OF)).thenReturn(Collections.singleton(axiom));
    }

    @Test
    public void shouldGetSubClassOfAxiom() {
        var axiomsStream = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId);
        var axioms = axiomsStream.collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldReturnEmptyStreamIfForUnknownOntologyId() {
        var axiomsStream = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, mock(OWLOntologyID.class));
        var axioms = axiomsStream.collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAxiomTypeIsNull() {
        impl.getAxiomsByType(null, ontologyId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getAxiomsByType(AxiomType.SUBCLASS_OF, null);
    }
}
