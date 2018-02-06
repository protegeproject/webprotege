package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class HasAnnotationAssertionAxiomsImpl_TestCase {

    private HasAnnotationAssertionAxiomsImpl impl;

    @Mock
    private OWLOntology rootOntology, importedOntology;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertionAxiomA, annotationAssertionAxiomB;

    @Mock
    private OWLAnnotationSubject subject;

    @Before
    public void setUp() throws Exception {
        when(rootOntology.getImportsClosure()).thenReturn(Sets.newHashSet(rootOntology, importedOntology));
        when(rootOntology.getAnnotationAssertionAxioms(subject)).thenReturn(Sets.newHashSet(annotationAssertionAxiomA));
        when(importedOntology.getAnnotationAssertionAxioms(subject)).thenReturn(Sets.newHashSet(annotationAssertionAxiomB));
        impl = new HasAnnotationAssertionAxiomsImpl(rootOntology);
    }

    @Test
    public void shouldReturnAssertionsInImportsClosure() {
        Set<OWLAnnotationAssertionAxiom> result = impl.getAnnotationAssertionAxioms(subject);
        assertThat(result, containsInAnyOrder(annotationAssertionAxiomA, annotationAssertionAxiomB));
    }


    @Test
    public void shouldReturnEmptySet() {
        Set<OWLAnnotationAssertionAxiom> result = impl.getAnnotationAssertionAxioms(mock(OWLAnnotationSubject.class));
        assertThat(result.isEmpty(), is(true));
    }
}
