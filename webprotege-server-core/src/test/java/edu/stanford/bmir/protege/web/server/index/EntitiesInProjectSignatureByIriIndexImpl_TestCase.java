package edu.stanford.bmir.protege.web.server.index;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
@RunWith(MockitoJUnitRunner.class)
public class EntitiesInProjectSignatureByIriIndexImpl_TestCase {

    private EntitiesInProjectSignatureByIriIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private IRI iri;

    @Mock
    private OWLOntology ontA, ontB;

    @Mock
    private OWLOntologyID ontAId, ontBId;

    @Mock
    private OWLClass entityCls;

    @Mock
    private OWLNamedIndividual entityIndividual;

    @Before
    public void setUp() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontAId, ontBId));
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());

        when(ontologyIndex.getOntology(ontAId))
                .thenReturn(Optional.of(ontA));
        when(ontologyIndex.getOntology(ontBId))
                .thenReturn(Optional.of(ontB));

        when(ontA.getEntitiesInSignature(iri))
                .thenAnswer(inv -> Stream.of(entityCls, entityIndividual));

        when(ontB.getEntitiesInSignature(iri))
                .thenAnswer(inv -> Stream.of(entityCls));

        impl = new EntitiesInProjectSignatureByIriIndexImpl(projectOntologiesIndex,
                                                            ontologyIndex);
    }

    @Test
    public void shouldReturnDistinctEntitiesInSignature() {
        var entities = impl.getEntityInSignature(iri).collect(Collectors.toList());
        assertThat(entities, Matchers.containsInAnyOrder(entityCls, entityIndividual));
    }

    @Test
    public void shouldReturnEmptyStreamForUnknownIri() {
        var entities = impl.getEntityInSignature(mock(IRI.class)).collect(toSet());
        assertThat(entities.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfIriIsNull() {
        impl.getEntityInSignature(null);
    }
}
