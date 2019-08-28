package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
 * 2019-08-09
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationAssertionAxiomsBySubjectIndexImpl_TestCase {

    private AnnotationAssertionAxiomsBySubjectIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAnnotationSubject subject;

    @Mock
    private OWLAnnotationAssertionAxiom axiom;

    @Before
    public void setUp() {
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));

        when(ontology.getAnnotationAssertionAxioms(any()))
                .thenReturn(Collections.emptySet());
        when(ontology.getAnnotationAssertionAxioms(subject))
                .thenReturn(Collections.singleton(axiom));

        impl = new AnnotationAssertionAxiomsBySubjectIndexImpl(ontologyIndex);
    }

    @Test
    public void shouldGetAnnotationAssertionBySubject() {
        var axioms = impl.getAxiomsForSubject(subject, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldReturnEmptyStreamForUnknownOntologyId() {
        var axioms = impl.getAxiomsForSubject(subject, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEmptyStreamForUnknownSubject() {
        var axioms = impl.getAxiomsForSubject(mock(OWLAnnotationSubject.class), ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfSubjectIsNull() {
        impl.getAxiomsForSubject(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getAxiomsForSubject(subject, null);
    }
}
