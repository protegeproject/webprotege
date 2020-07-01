package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.StringEndsWithCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringStartsWithCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-23
 */
@RunWith(MockitoJUnitRunner.class)
public class StringEndsWithMatcher_TestCase {

    public static final boolean DO_NOT_IGNORE_CASE = false;

    public static final boolean IGNORE_CASE = true;

    private StringEndsWithMatcher matcher;

    @Mock
    private StringEndsWithCriteria criteria;

    @Before
    public void setUp() throws Exception {
        matcher = new StringEndsWithMatcher(criteria);
    }

    @Test
    public void shouldMatchStringEnd() {
        when(criteria.getValue()).thenReturn("world");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello world");
        assertThat(matches, is(true));
    }

    @Test
    public void shouldNotMatchStringEnd() {
        when(criteria.getValue()).thenReturn("World");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello world");
        assertThat(matches, is(false));
    }

    @Test
    public void shouldMatchStringEndIgnoringCase() {
        when(criteria.getValue()).thenReturn("world");
        when(criteria.isIgnoreCase()).thenReturn(IGNORE_CASE);
        var matches = matcher.matches("Hello world");
        assertThat(matches, is(true));
    }
}
