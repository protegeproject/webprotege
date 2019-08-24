package edu.stanford.bmir.protege.web.server.index;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@RunWith(MockitoJUnitRunner.class)
public class EntitiesInOntologySignatureIndexImpl_TestCase {

    private EntitiesInOntologySignatureIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Before
    public void setUp() {
        impl = new EntitiesInOntologySignatureIndexImpl(ontologyIndex);
        when(ontologyIndex.getOntology(any()))
                .thenAnswer(invocation -> Optional.empty());
        when(ontologyIndex.getOntology(ontologyId))
                .thenAnswer(invocation -> Optional.of(ontology));
        when(ontology.containsEntityInSignature(entity))
                .thenReturn(true);
    }

    @Test
    public void shouldContainEntityInSignature() {
        var contains = impl.containsEntityInSignature(entity, ontologyId);
        assertThat(contains, is(true));
    }

    @Test
    public void shouldNotContainUnknownEntityInSignature() {
        var contains = impl.containsEntityInSignature(mock(OWLEntity.class), ontologyId);
        assertThat(contains, is(false));
    }

    @Test
    public void shouldNotContainEntityInUnknownOntologySignature() {
        var contains = impl.containsEntityInSignature(entity, mock(OWLOntologyID.class));
        assertThat(contains, is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.containsEntityInSignature(entity, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfEntityIsNull() {
        impl.containsEntityInSignature(null, ontologyId);
    }
}
