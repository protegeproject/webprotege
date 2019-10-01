package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class IriAnnotationValueMatcher_TestCase {

    private IriAnnotationValueMatcher matcher;

    @Mock
    private Matcher<IRI> iriMatcher;

    @Mock
    private IRI iri;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLAnonymousIndividual individual;

    @Before
    public void setUp() {
        matcher = new IriAnnotationValueMatcher(iriMatcher);
    }

    @Test
    public void shouldNotMatchLiteral() {
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldNotMatchAnonymousIndividual() {
        assertThat(matcher.matches(individual), is(false));
    }

    @Test
    public void shouldNotMatchIri() {
        assertThat(matcher.matches(iri), is(false));
    }

    @Test
    public void shouldMatchIri() {
        when(iriMatcher.matches(iri)).thenReturn(true);
        assertThat(matcher.matches(iri), is(true));
    }

}
