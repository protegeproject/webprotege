package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectAnnotationAssertionAxiomsBySubjectIndexImpl_TestCase {

    private ProjectAnnotationAssertionAxiomsBySubjectIndexImpl impl;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertionAxiomA, annotationAssertionAxiomB;

    @Mock
    private OWLAnnotationSubject subject;

    @Mock
    private ProjectOntologiesIndex ontologiesIndex;

    @Mock
    private AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Before
    public void setUp() throws Exception {
        when(ontologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(annotationAssertionsIndex.getAxiomsForSubject(any(), any()))
                .thenReturn(Stream.empty());
        when(annotationAssertionsIndex.getAxiomsForSubject(subject, ontologyId))
                .thenReturn(Stream.of(annotationAssertionAxiomA, annotationAssertionAxiomB));
        impl = new ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(ontologiesIndex, annotationAssertionsIndex);
    }

    @Test
    public void shouldReturnAssertionsForKnownSubject() {
        Set<OWLAnnotationAssertionAxiom> result = impl.getAnnotationAssertionAxioms(subject).collect(Collectors.toSet());
        assertThat(result, containsInAnyOrder(annotationAssertionAxiomA, annotationAssertionAxiomB));
    }


    @Test
    public void shouldReturnEmptySetForUnknownSubject() {
        Set<OWLAnnotationAssertionAxiom> result = impl.getAnnotationAssertionAxioms(mock(OWLAnnotationSubject.class)).collect(Collectors.toSet());
        assertThat(result.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfSubjectIsNull() {
        impl.getAnnotationAssertionAxioms(null);
    }
}
