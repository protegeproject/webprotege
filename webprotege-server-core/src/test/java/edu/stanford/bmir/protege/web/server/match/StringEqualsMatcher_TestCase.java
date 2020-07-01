package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.StringEqualsCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StringEqualsMatcher_TestCase {

    public static final boolean DO_NOT_IGNORE_CASE = false;

    public static final boolean IGNORE_CASE = true;

    private StringEqualsMatcher matcher;

    @Mock
    private StringEqualsCriteria criteria;

    @Before
    public void setUp() throws Exception {
        matcher = new StringEqualsMatcher(criteria);
    }

    @Test
    public void shouldMatch() {
        when(criteria.getValue()).thenReturn("Hello World");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello World");
        assertThat(matches, is(true));
    }

    @Test
    public void shouldNotMatch() {
        when(criteria.getValue()).thenReturn("hello world");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello World");
        assertThat(matches, is(false));
    }

    @Test
    public void shouldMatchIgnoringCase() {
        when(criteria.getValue()).thenReturn("hello world");
        when(criteria.isIgnoreCase()).thenReturn(IGNORE_CASE);
        var matches = matcher.matches("Hello World");
        assertThat(matches, is(true));
    }
}