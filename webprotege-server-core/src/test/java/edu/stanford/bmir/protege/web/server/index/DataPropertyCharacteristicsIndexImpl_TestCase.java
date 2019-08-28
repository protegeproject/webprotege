package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
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
public class DataPropertyCharacteristicsIndexImpl_TestCase {

    private DataPropertyCharacteristicsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLDataProperty property;

    @Before
    public void setUp() {
        impl = new DataPropertyCharacteristicsIndexImpl(ontologyIndex);
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));
    }

    @Test
    public void shouldReturnTrueForFunctional() {
        when(ontology.getFunctionalDataPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLFunctionalDataPropertyAxiom.class)));
        assertThat(impl.isFunctional(property, ontologyId), is(true));
    }

    @Test
    public void shouldReturnFalseForFunctional() {
        assertThat(impl.isFunctional(property, ontologyId), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownOntologyId() {
        assertThat(impl.isFunctional(property, mock(OWLOntologyID.class)), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownProperty() {
        assertThat(impl.isFunctional(mock(OWLDataProperty.class), ontologyId), is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfPropertyIsNull() {
        impl.isFunctional(null, ontologyId);
    }


    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.isFunctional(property, null);
    }
}
