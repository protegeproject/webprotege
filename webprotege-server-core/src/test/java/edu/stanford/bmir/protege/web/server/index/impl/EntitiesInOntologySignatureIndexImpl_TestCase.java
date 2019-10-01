package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsSignatureIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex;

    @Mock
    private OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Before
    public void setUp() {
        impl = new EntitiesInOntologySignatureIndexImpl(ontologyAxiomsSignatureIndex, ontologyAnnotationsSignatureIndex);
        when(ontologyAxiomsSignatureIndex.containsEntityInOntologyAxiomsSignature(entity, ontologyId))
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
