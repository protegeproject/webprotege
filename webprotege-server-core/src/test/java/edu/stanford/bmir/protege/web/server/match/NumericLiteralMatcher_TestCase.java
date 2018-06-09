package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class NumericLiteralMatcher_TestCase {

    @Mock
    private OWLLiteral literal;

    @Before
    public void setUp() {
        when(literal.getLiteral()).thenReturn("10");
    }

    @Test
    public void shouldBeLessThan() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forLessThan(11);
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotBeLessThan() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forLessThan(9);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldBeLessThanOrEqualTo() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forLessThanOrEqualTo(10);
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotBeLessThanOrEqualTo() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forLessThan(9);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldBeGreaterThan() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forGreaterThan(9);
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotBeGreaterThan() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forGreaterThan(11);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldBeGreaterThanOrEqualTo() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forGreaterThanOrEqualTo(10);
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotBeGreaterThanOrEqualTo() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forGreaterThanOrEqualTo(11);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldBeEqualTo() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forEqualTo(10);
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotBeEqualTo() {
        Matcher<OWLLiteral> matcher = NumericLiteralMatcher.forEqualTo(11);
        assertThat(matcher.matches(literal), is(false));
    }
}
