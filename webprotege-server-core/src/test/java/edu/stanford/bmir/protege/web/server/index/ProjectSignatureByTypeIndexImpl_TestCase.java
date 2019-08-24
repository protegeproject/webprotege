package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectSignatureByTypeIndexImpl_TestCase {

    private ProjectSignatureByTypeIndexImpl impl;


    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLDatatype datatype;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual individual;

    @Before
    public void setUp() {
        impl = new ProjectSignatureByTypeIndexImpl(projectOntologiesIndex, ontologySignatureByTypeIndex);

        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(ontologySignatureByTypeIndex.getSignature(any(), any()))
                .thenReturn(Stream.empty());
        when(ontologySignatureByTypeIndex.getSignature(EntityType.CLASS, ontologyId))
                .thenReturn(Stream.of(cls));
        when(ontologySignatureByTypeIndex.getSignature(EntityType.DATATYPE, ontologyId))
                .thenReturn(Stream.of(datatype));
        when(ontologySignatureByTypeIndex.getSignature(EntityType.OBJECT_PROPERTY, ontologyId))
                .thenReturn(Stream.of(objectProperty));
        when(ontologySignatureByTypeIndex.getSignature(EntityType.DATA_PROPERTY, ontologyId))
                .thenReturn(Stream.of(dataProperty));
        when(ontologySignatureByTypeIndex.getSignature(EntityType.ANNOTATION_PROPERTY, ontologyId))
                .thenReturn(Stream.of(annotationProperty));
        when(ontologySignatureByTypeIndex.getSignature(EntityType.NAMED_INDIVIDUAL, ontologyId))
                .thenReturn(Stream.of(individual));
    }

    @Test
    public void shouldGetClassesInSignature() {
        var signature = impl.getSignature(EntityType.CLASS).collect(toSet());
        assertThat(signature, hasItem(cls));
    }

    @Test
    public void shouldGetDatatypesInSignature() {
        var signature = impl.getSignature(EntityType.DATATYPE).collect(toSet());
        assertThat(signature, hasItem(datatype));
    }

    @Test
    public void shouldGetObjectPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.OBJECT_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(objectProperty));
    }

    @Test
    public void shouldGetDataPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.DATA_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.ANNOTATION_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(annotationProperty));
    }

    @Test
    public void shouldGetIndividualsPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.NAMED_INDIVIDUAL).collect(toSet());
        assertThat(signature, hasItem(individual));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getSignature(null);
    }
}
