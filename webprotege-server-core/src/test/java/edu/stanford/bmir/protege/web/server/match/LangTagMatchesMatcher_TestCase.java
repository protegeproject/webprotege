package edu.stanford.bmir.protege.web.server.match;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class LangTagMatchesMatcher_TestCase {


    @Test
    public void shouldMatchTag() {
        assertThat(tagMatchesPattern("en", "en"), is(true));
    }

    @Test
    public void shouldMatchTagDifferingByCase() {
        assertThat(tagMatchesPattern("EN", "en"), is(true));
    }

    @Test
    public void shouldNotMatchTagRequiringRegion() {
        assertThat(tagMatchesPattern("en", "en-US"), is(false));
    }

    @Test
    public void shouldNotMatchEmptyTag() {
        assertThat(tagMatchesPattern("", "en"), is(false));
    }

    @Test
    public void shouldNotMatchMalformedTag() {
        assertThat(tagMatchesPattern("a b c", "en"), is(false));
    }

    @Test
    public void shouldNotMatchEmptyPattern() {
        assertThat(tagMatchesPattern("en", ""), is(false));
    }

    @Test
    public void shouldNotMatchMalformedPattern() {
        assertThat(tagMatchesPattern("en", "x y z"), is(false));
    }

    @Test
    public void shouldMatchWildCardPattern() {
        assertThat(tagMatchesPattern("en", "*"), is(true));
    }

    @Test
    public void shouldNotMatchWildCardPatternWhenEmpty() {
        assertThat(tagMatchesPattern("", "*"), is(false));
    }

    @Test
    public void shouldMatchRegionWildCardPattern() {
        assertThat(tagMatchesPattern("en-US", "*-US"), is(true));
    }

    @Test
    public void shouldMatchTagWithRegion() {
        assertThat(tagMatchesPattern("en-US", "en"), is(true));
    }

    @Test
    public void shouldNotMatchTag() {
        assertThat(tagMatchesPattern("fr", "en"), is(false));
    }

    private static boolean tagMatchesPattern(String tag, String pattern) {
        return LangTagMatchesMatcher.fromPattern(pattern).matches(tag);
    }
}
