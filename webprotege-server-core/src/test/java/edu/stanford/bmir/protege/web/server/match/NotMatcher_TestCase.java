package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class NotMatcher_TestCase<T> {

    private NotMatcher<T> matcher;

    @Mock
    private Matcher<T> innerMatcher;

    @Mock
    private T value;

    @Before
    public void setUp() {
        matcher = new NotMatcher<>(innerMatcher);
    }

    @Test
    public void shouldMatch() {
        when(innerMatcher.matches(value)).thenReturn(false);
        assertThat(matcher.matches(value), is(true));
    }

    @Test
    public void shouldNotMatch() {
        when(innerMatcher.matches(value)).thenReturn(true);
        assertThat(matcher.matches(value), is(false));
    }
}
