package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsSignatureIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
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

    @Mock
    private OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Mock
    private OWLAnnotationProperty otherEntity;

    @Mock
    private IRI otherIri;

    @Before
    public void setUp() {
        impl = new EntitiesInOntologySignatureByIriIndexImpl(axiomByEntityReference, ontologyAnnotationsSignatureIndex);
        when(axiomByEntityReference.getEntitiesInSignatureWithIri(any(), any()))
                .thenAnswer(inv -> Stream.empty());
        when(axiomByEntityReference.getEntitiesInSignatureWithIri(iri, ontologyId))
                .thenAnswer(inv -> Stream.of(entity));
        when(otherEntity.getIRI())
                .thenReturn(otherIri);
        when(ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(any()))
                .thenAnswer(inv -> Stream.empty());
        when(ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(ontologyId))
                .thenAnswer(inv -> Stream.of(otherEntity));
    }

    @Test
    public void shouldGetEntitiesInAxiomSignature() {
        var sig = impl.getEntitiesInSignature(iri, ontologyId).collect(toSet());
        assertThat(sig, contains(entity));
    }

    @Test
    public void shouldGetEntitiesInOntologyAnnotationsSignature() {
        var sig = impl.getEntitiesInSignature(otherIri, ontologyId).collect(toSet());
        assertThat(sig, contains(otherEntity));
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
