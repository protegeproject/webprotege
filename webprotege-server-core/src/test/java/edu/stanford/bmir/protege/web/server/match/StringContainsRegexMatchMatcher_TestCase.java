package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class StringContainsRegexMatchMatcher_TestCase {

    private StringContainsRegexMatchMatcher matcher;

    @Before
    public void setUp() {
        matcher = new StringContainsRegexMatchMatcher(Pattern.compile("[A-Z]"));
    }

    @Test
    public void shouldMatchValue() {
        assertThat(matcher.matches("A"), is(true));
    }

    @Test
    public void shouldNotMatchValue() {
        assertThat(matcher.matches("0"), is(false));
    }
}
