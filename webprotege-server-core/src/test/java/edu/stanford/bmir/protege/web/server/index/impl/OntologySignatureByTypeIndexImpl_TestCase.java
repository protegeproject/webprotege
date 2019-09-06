package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.impl.OntologyIndex;
import edu.stanford.bmir.protege.web.server.index.impl.OntologySignatureByTypeIndexImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologySignatureByTypeIndexImpl_TestCase {

    private OntologySignatureByTypeIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

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
        impl = new OntologySignatureByTypeIndexImpl(ontologyIndex);

        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());

        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));

        when(ontology.getClassesInSignature())
                .thenReturn(Collections.singleton(cls));

        when(ontology.getObjectPropertiesInSignature())
                .thenReturn(Collections.singleton(objectProperty));

        when(ontology.getDataPropertiesInSignature())
                .thenReturn(Collections.singleton(dataProperty));

        when(ontology.getAnnotationPropertiesInSignature())
                .thenReturn(Collections.singleton(annotationProperty));

        when(ontology.getIndividualsInSignature())
                .thenReturn(Collections.singleton(individual));

        when(ontology.getDatatypesInSignature())
                .thenReturn(Collections.singleton(datatype));
    }

    @Test
    public void shouldGetClassesInSignature() {
        var signature = impl.getSignature(EntityType.CLASS, ontologyId).collect(toSet());
        assertThat(signature, hasItem(cls));
    }


    @Test
    public void shouldGetDatatypesInSignature() {
        var signature = impl.getSignature(EntityType.DATATYPE, ontologyId).collect(toSet());
        assertThat(signature, hasItem(datatype));
    }

    @Test
    public void shouldGetObjectPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.OBJECT_PROPERTY, ontologyId).collect(toSet());
        assertThat(signature, hasItem(objectProperty));
    }

    @Test
    public void shouldGetDataPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.DATA_PROPERTY, ontologyId).collect(toSet());
        assertThat(signature, hasItem(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.ANNOTATION_PROPERTY, ontologyId).collect(toSet());
        assertThat(signature, hasItem(annotationProperty));
    }


    @Test
    public void shouldGetIndividualsInSignature() {
        var signature = impl.getSignature(EntityType.NAMED_INDIVIDUAL, ontologyId).collect(toSet());
        assertThat(signature, hasItem(individual));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var signature = impl.getSignature(EntityType.CLASS, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(signature.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullType() {
        impl.getSignature(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getSignature(EntityType.CLASS, null);
    }
}
