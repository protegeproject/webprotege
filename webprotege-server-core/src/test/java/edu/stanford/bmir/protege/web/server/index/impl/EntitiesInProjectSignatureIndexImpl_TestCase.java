package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.impl.EntitiesInProjectSignatureIndexImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private EntitiesInOntologySignatureIndex entitiesInOntologySignatureIndex;

    @Before
    public void setUp() {
        impl = new EntitiesInProjectSignatureIndexImpl(projectOntologiesIndex,
                                                       entitiesInOntologySignatureIndex);
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));
        when(entitiesInOntologySignatureIndex.containsEntityInSignature(entity, ontologyId))
                .thenReturn(true);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(projectOntologiesIndex, entitiesInOntologySignatureIndex));
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
