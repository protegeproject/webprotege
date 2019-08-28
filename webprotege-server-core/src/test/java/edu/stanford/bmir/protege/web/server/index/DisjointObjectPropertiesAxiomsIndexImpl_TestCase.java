package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
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
 * 2019-08-24
 */
@RunWith(MockitoJUnitRunner.class)
public class DisjointObjectPropertiesAxiomsIndexImpl_TestCase {

    private DisjointObjectPropertiesAxiomsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLObjectProperty property;

    @Mock
    private OWLDisjointObjectPropertiesAxiom axiom;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyID))
                .thenReturn(Optional.of(ontology));
        when(ontology.getDisjointObjectPropertiesAxioms(property))
                .thenReturn(Collections.singleton(axiom));
        impl = new DisjointObjectPropertiesAxiomsIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldGetDisjointObjectPropertiesAxiomForObjectProperty() {
        var axioms = impl.getDisjointObjectPropertiesAxioms(property, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getDisjointObjectPropertiesAxioms(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownObjectProperty() {
        var axioms = impl.getDisjointObjectPropertiesAxioms(mock(OWLObjectProperty.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getDisjointObjectPropertiesAxioms(property, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullObjectProperty() {
        impl.getDisjointObjectPropertiesAxioms(null, ontologyID);
    }
}
