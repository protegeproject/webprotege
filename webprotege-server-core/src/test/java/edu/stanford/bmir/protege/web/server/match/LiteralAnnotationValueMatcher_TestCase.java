package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class LiteralAnnotationValueMatcher_TestCase {

    private LiteralAnnotationValueMatcher matcher;

    @Mock
    private Matcher<OWLLiteral> literalMatcher;

    @Mock
    private OWLLiteral literal;

    @Mock
    private IRI iri;

    @Mock
    private OWLAnonymousIndividual individual;

    @Before
    public void setUp() throws Exception {
        matcher = new LiteralAnnotationValueMatcher(literalMatcher);

        when(literalMatcher.matches(literal)).thenReturn(true);

    }

    @Test
    public void shouldMatchLiteral() {
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotMatchLiteral() {
        when(literalMatcher.matches(literal)).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldNotMatchIri() {
        assertThat(matcher.matches(iri), is(false));
    }

    @Test
    public void shouldNotMatchAnonymousIndividual() {
        assertThat(matcher.matches(individual), is(false));
    }
}
