package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsSignatureIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
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
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologySignatureByTypeIndexImpl_TestCase {

    private OntologySignatureByTypeIndexImpl impl;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty2;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private OWLDatatype datatype;

    @Mock
    private OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex;

    @Mock
    private OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Before
    public void setUp() {
        impl = new OntologySignatureByTypeIndexImpl(ontologyAxiomsSignatureIndex,
                                                    ontologyAnnotationsSignatureIndex);


        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(any(), any()))
                .thenAnswer(invocation -> Stream.empty());

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(CLASS, ontologyDocumentId))

                .thenAnswer(invocation -> Stream.of(cls));
        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(OBJECT_PROPERTY, ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(objectProperty));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(DATA_PROPERTY, ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(dataProperty));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(ANNOTATION_PROPERTY, ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(annotationProperty));

        when(ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(annotationProperty2));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(NAMED_INDIVIDUAL, ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(individual));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(DATATYPE, ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(datatype));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(ontologyAxiomsSignatureIndex,
                                                    ontologyAnnotationsSignatureIndex));
    }

    @Test
    public void shouldGetClassesInSignature() {
        var signature = impl.getSignature(CLASS, ontologyDocumentId).collect(toSet());
        assertThat(signature, hasItem(cls));
    }


    @Test
    public void shouldGetDatatypesInSignature() {
        var signature = impl.getSignature(DATATYPE, ontologyDocumentId).collect(toSet());
        assertThat(signature, hasItem(datatype));
    }

    @Test
    public void shouldGetObjectPropertiesInSignature() {
        var signature = impl.getSignature(OBJECT_PROPERTY, ontologyDocumentId).collect(toSet());
        assertThat(signature, hasItem(objectProperty));
    }

    @Test
    public void shouldGetDataPropertiesInSignature() {
        var signature = impl.getSignature(DATA_PROPERTY, ontologyDocumentId).collect(toSet());
        assertThat(signature, hasItem(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertiesInSignature() {
        var signature = impl.getSignature(ANNOTATION_PROPERTY, ontologyDocumentId).collect(toSet());
        assertThat(signature, hasItems(annotationProperty, annotationProperty2));
    }


    @Test
    public void shouldGetIndividualsInSignature() {
        var signature = impl.getSignature(NAMED_INDIVIDUAL, ontologyDocumentId).collect(toSet());
        assertThat(signature, hasItem(individual));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var signature = impl.getSignature(CLASS, mock(OntologyDocumentId.class)).collect(toSet());
        assertThat(signature.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullType() {
        impl.getSignature(null, ontologyDocumentId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getSignature(CLASS, null);
    }
}
