package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
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
public class OrMatcher_TestCase<T> {

    private OrMatcher<T> matcher;

    @Mock
    private Matcher<T> matcherA, matcherB;

    @Mock
    private T value;

    private ImmutableList<Matcher<T>> matchers;

    @Before
    public void setUp() {
        matchers = ImmutableList.of(matcherA, matcherB);
        matcher = new OrMatcher<>(matchers);
    }

    @Test
    public void shouldNotMatchValue() {
        assertThat(matcher.matches(value), is(false));
    }

    @Test
    public void shouldMatchAtLeastOne() {
        when(matcherA.matches(value)).thenReturn(true);
        assertThat(matcher.matches(value), is(true));
    }

    @Test
    public void shouldMatchAll() {
        when(matcherA.matches(value)).thenReturn(true);
        assertThat(matcher.matches(value), is(true));
    }
}
