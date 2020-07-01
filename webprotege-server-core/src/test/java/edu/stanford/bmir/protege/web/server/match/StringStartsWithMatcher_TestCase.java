package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.StringStartsWithCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StringStartsWithMatcher_TestCase {

    public static final boolean DO_NOT_IGNORE_CASE = false;

    public static final boolean IGNORE_CASE = true;

    private StringStartsWithMatcher matcher;

    @Mock
    private StringStartsWithCriteria criteria;

    @Before
    public void setUp() throws Exception {
        matcher = new StringStartsWithMatcher(criteria);
    }

    @Test
    public void shouldMatchStringStart() {
        when(criteria.getValue()).thenReturn("Hello");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello world");
        assertThat(matches, is(true));
    }

    @Test
    public void shouldNotMatchStringStart() {
        when(criteria.getValue()).thenReturn("hello");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello world");
        assertThat(matches, is(false));
    }

    @Test
    public void shouldMatchStringStartIgnoringCase() {
        when(criteria.getValue()).thenReturn("hello");
        when(criteria.isIgnoreCase()).thenReturn(IGNORE_CASE);
        var matches = matcher.matches("Hello world");
        assertThat(matches, is(true));
    }


}