package edu.stanford.bmir.protege.web.server.index.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologySignatureIndexImpl_TestCase {

    private OntologySignatureIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceImpl;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private OWLDatatype datatype;


    @Before
    public void setUp() {
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.CLASS, ontologyId))
                .thenAnswer(invocation -> Stream.of(cls));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.OBJECT_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(objectProperty));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(dataProperty));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.ANNOTATION_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(annotationProperty));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.NAMED_INDIVIDUAL, ontologyId))
                .thenAnswer(invocation -> Stream.of(individual));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.DATATYPE, ontologyId))
                .thenAnswer(invocation -> Stream.of(datatype));
        impl = new OntologySignatureIndexImpl(axiomsByEntityReferenceImpl);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByEntityReferenceImpl));
    }

    @Test
    public void shouldGetSignatureOfKnownOntology() {
        var signature = impl.getEntitiesInSignature(ontologyId).collect(toSet());
        assertThat(signature, hasItems(cls, objectProperty, dataProperty, annotationProperty, individual, datatype));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var signature = impl.getEntitiesInSignature(mock(OWLOntologyID.class)).collect(toSet());
        assertThat(signature.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getEntitiesInSignature(null);
    }
}
