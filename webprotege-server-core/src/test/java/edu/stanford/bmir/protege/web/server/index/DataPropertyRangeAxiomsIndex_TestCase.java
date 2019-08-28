package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
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
public class DataPropertyRangeAxiomsIndex_TestCase {

    private DataPropertyRangeAxiomsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLDataProperty property;

    @Mock
    private OWLDataPropertyRangeAxiom axiom;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyID))
                .thenReturn(Optional.of(ontology));
        when(ontology.getDataPropertyRangeAxioms(property))
                .thenReturn(Collections.singleton(axiom));
        impl = new DataPropertyRangeAxiomsIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldGetDataPropertyRangeAxiomForProperty() {
        var axioms = impl.getDataPropertyRangeAxioms(property, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getDataPropertyRangeAxioms(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getDataPropertyRangeAxioms(mock(OWLDataProperty.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getDataPropertyRangeAxioms(property, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullProperty() {
        impl.getDataPropertyRangeAxioms(null, ontologyID);
    }
}
