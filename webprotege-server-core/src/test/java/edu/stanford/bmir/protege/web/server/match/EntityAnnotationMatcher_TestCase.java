package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityAnnotationMatcher_TestCase {

    private EntityAnnotationMatcher matcher;

    @Mock
    private HasAnnotationAssertionAxioms axiomProvider;

    @Mock
    private Matcher<OWLAnnotation> annotationMatcher;

    @Mock
    private OWLAnnotationAssertionAxiom axA, axB;

    @Mock
    private OWLAnnotation annotationA, annotationB;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI iri;

    @Before
    public void setUp() {
        matcher = new EntityAnnotationMatcher(axiomProvider,
                                              annotationMatcher,
                                              AnnotationPresence.PRESENT);

        Set<OWLAnnotationAssertionAxiom> axioms = Sets.newHashSet(axA, axB);

        when(axA.getAnnotation()).thenReturn(annotationA);
        when(axB.getAnnotation()).thenReturn(annotationB);

        when(entity.getIRI()).thenReturn(iri);
        when(axiomProvider.getAnnotationAssertionAxioms(iri)).thenReturn(axioms);
    }

    @Test
    public void shouldNotMatch() {
        assertThat(matcher.matches(entity), is(false));
    }

    @Test
    public void shouldMatchAtLeastOne() {
        when(annotationMatcher.matches(annotationA)).thenReturn(true);
        assertThat(matcher.matches(entity), is(true));
    }

    @Test
    public void shouldMatchAll() {
        when(annotationMatcher.matches(annotationA)).thenReturn(true);
        when(annotationMatcher.matches(annotationB)).thenReturn(true);
        assertThat(matcher.matches(entity), is(true));
    }
}
