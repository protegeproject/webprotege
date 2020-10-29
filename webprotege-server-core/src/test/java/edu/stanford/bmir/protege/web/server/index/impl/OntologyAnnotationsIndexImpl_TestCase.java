package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddOntologyAnnotationChange;
import edu.stanford.bmir.protege.web.server.change.RemoveOntologyAnnotationChange;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Annotation;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAnnotationsIndexImpl_TestCase {

    private OntologyAnnotationsIndexImpl impl;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLAnnotationValue annotationValue;

    private OWLAnnotation ontologyAnnotation;

    private OWLAnnotation annotationAnnotation;

    private OWLAnnotationProperty property, otherProperty;

    @Before
    public void setUp() {
        property = AnnotationProperty(mock(IRI.class));
        otherProperty = AnnotationProperty(mock(IRI.class));
        annotationAnnotation = Annotation(otherProperty, annotationValue);
        ontologyAnnotation = Annotation(property,
                                        annotationValue)
                .getAnnotatedAnnotation(Collections.singleton(annotationAnnotation));
        impl = new OntologyAnnotationsIndexImpl();
        impl.applyChanges(ImmutableList.of(AddOntologyAnnotationChange.of(ontologyDocumentId, ontologyAnnotation)));
    }

    @Test
    public void shouldContainAnnotation() {
        assertThat(impl.containsAnnotation(ontologyAnnotation, ontologyDocumentId), is(true));
    }

    @Test
    public void shouldContainEntityInSignature() {
        assertThat(impl.containsEntityInOntologyAnnotationsSignature(property, ontologyDocumentId), is(true));
    }

    @Test
    public void shouldContainAnnotationOnAnnotationInSignature() {
        assertThat(impl.containsEntityInOntologyAnnotationsSignature(otherProperty, ontologyDocumentId), is(true));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(mock(OntologyDocumentId.class));
        assertThat(ontologyAnnotationsStream.count(), is(0L));
    }

    @Test
    public void shouldGetOntologyAnnotations() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(ontologyDocumentId);
        var ontologyAnnotations = ontologyAnnotationsStream.collect(toSet());
        assertThat(ontologyAnnotations, contains(ontologyAnnotation));
    }

    @Test
    public void shouldGetOntologyAnnotationsSignature() {
        var signature = impl.getOntologyAnnotationsSignature(ontologyDocumentId)
                            .collect(toSet());
        assertThat(signature, containsInAnyOrder(property, otherProperty));
    }

    @Test
    public void shouldNotContainEntityInSignature() {
        assertThat(impl.containsEntityInOntologyAnnotationsSignature(mock(OWLEntity.class), ontologyDocumentId), is(false));
    }

    @Test
    public void shouldRemoveAnnotation() {
        impl.applyChanges(ImmutableList.of(RemoveOntologyAnnotationChange.of(ontologyDocumentId, ontologyAnnotation)));
        assertThat(impl.getOntologyAnnotations(ontologyDocumentId).count(), is(0L));
    }
}
