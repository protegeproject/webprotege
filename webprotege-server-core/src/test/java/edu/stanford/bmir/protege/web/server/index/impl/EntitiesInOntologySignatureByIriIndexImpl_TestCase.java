package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.EntitiesInOntologySignatureByIriIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-18
 */
@RunWith(MockitoJUnitRunner.class)
public class EntitiesInOntologySignatureByIriIndexImpl_TestCase {

    private EntitiesInOntologySignatureByIriIndexImpl impl;

    @Mock
    private AxiomsByEntityReferenceIndexImpl axiomByEntityReference;

    @Mock
    private IRI iri;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Before
    public void setUp() {
        impl = new EntitiesInOntologySignatureByIriIndexImpl(axiomByEntityReference);
//        when(impl.getEntitiesInSignature(any(), any()))
//                .thenAnswer(inv -> Stream.empty());
        when(impl.getEntitiesInSignature(iri, ontologyId))
                .thenAnswer(inv -> Stream.of(entity));
    }

    @Test
    public void shouldGetEntitiesInSignature() {
        var sig = impl.getEntitiesInSignature(iri, ontologyId).collect(toSet());
        assertThat(sig, contains(entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullIri() {
        impl.getEntitiesInSignature(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getEntitiesInSignature(iri, null);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomByEntityReference));
    }
}
