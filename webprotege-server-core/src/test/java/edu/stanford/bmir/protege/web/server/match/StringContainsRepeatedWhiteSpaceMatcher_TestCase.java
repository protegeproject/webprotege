package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class StringContainsRepeatedWhiteSpaceMatcher_TestCase {

    private StringContainsRepeatedWhiteSpaceMatcher matcher;

    @Before
    public void setUp() {
        matcher = new StringContainsRepeatedWhiteSpaceMatcher();
    }

    @Test
    public void shouldMatchRepeatedWhiteSpaceAtStart() {
        assertThat(matcher.matches("  A String"), is(true));
    }

    @Test
    public void shouldMatchRepeatedWhiteSpaceAtEnd() {
        assertThat(matcher.matches("A String  "), is(true));
    }

    @Test
    public void shouldMatchRepeatedWhiteInMiddle() {
        assertThat(matcher.matches("A  String"), is(true));
    }

    @Test
    public void shouldNotMatchString() {
        assertThat(matcher.matches("A String"), is(false));
    }
}
