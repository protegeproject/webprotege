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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@RunWith(MockitoJUnitRunner.class)
public class EntitiesInProjectSignatureIndexImpl_TestCase {

    private EntitiesInProjectSignatureIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLEntity entity;

    @Before
    public void setUp() {
        impl = new EntitiesInProjectSignatureIndexImpl(projectOntologiesIndex,
                                                       ontologyIndex);
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));

        when(ontology.containsEntityInSignature(entity))
                .thenReturn(true);
    }

    @Test
    public void shouldContainEntityInSignature() {
        var contains = impl.containsEntityInSignature(entity);
        assertThat(contains, is(true));
    }

    @Test
    public void shouldNotContainUnknownEntityInSignature() {
        var contains = impl.containsEntityInSignature(mock(OWLEntity.class));
        assertThat(contains, is(false));
    }

    @Test
    public void shouldReturnFalseForEmptySetOfProjectOntologies() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.empty());
        var contains = impl.containsEntityInSignature(entity);
        assertThat(contains, is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfEntityIsNull() {
        impl.containsEntityInSignature(null);
    }

}
