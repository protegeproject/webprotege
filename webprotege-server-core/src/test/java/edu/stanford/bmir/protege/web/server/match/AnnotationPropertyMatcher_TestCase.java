package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationPropertyMatcher_TestCase {

    private AnnotationPropertyMatcher matcher;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private IRI iri;

    @Mock
    private Matcher<IRI> iriMatcher;

    @Before
    public void setUp() {
        matcher = new AnnotationPropertyMatcher(iriMatcher);
        when(property.getIRI()).thenReturn(iri);
    }

    @Test
    public void shouldMatchAnnotationProperty() {
        when(iriMatcher.matches(iri)).thenReturn(true);
        assertThat(matcher.matches(property), is(true));
    }

    @Test
    public void shouldNotMatchAnnotationProperty() {
        when(iriMatcher.matches(iri)).thenReturn(false);
        assertThat(matcher.matches(property), is(false));
    }
}
