package edu.stanford.bmir.protege.web.server.index;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;

import java.util.Collections;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAnnotationsIndexImpl_TestCase {

    private OntologyAnnotationsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any())).thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId)).thenReturn(Optional.of(ontology));
        when(ontology.getAnnotations()).thenReturn(Collections.singleton(ontologyAnnotation));
        impl = new OntologyAnnotationsIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldGetOntologyAnnotations() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(ontologyId);
        var ontologyAnnotations = ontologyAnnotationsStream.collect(toSet());
        assertThat(ontologyAnnotations, Matchers.contains(ontologyAnnotation));
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void shouldThrowUnknownOntologyException() {
        impl.getOntologyAnnotations(mock(OWLOntologyID.class));
    }
}
