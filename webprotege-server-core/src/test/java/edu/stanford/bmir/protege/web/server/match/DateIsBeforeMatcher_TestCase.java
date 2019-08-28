package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class DateIsBeforeMatcher_TestCase {

    private DateIsBeforeMatcher matcher;

    @Before
    public void setUp() {
        matcher = new DateIsBeforeMatcher(LocalDate.of(2018, 9, 1));
    }

    @Test
    public void shouldNotMatchMalformedDateTime() {
        assertThat(matcher.matches("2018-01-01"), is(false));
    }

    @Test
    public void shouldNotMatchDateTimeOnDate() {
        assertThat(matcher.matches("2018-09-01T10:10:00Z"), is(false));
    }

    @Test
    public void shouldNotMatchDateTimeAfterDate() {
        assertThat(matcher.matches("2018-09-10T10:10:00Z"), is(false));
    }

    @Test
    public void shouldMatchDateTimeBeforeDate() {
        assertThat(matcher.matches("2018-01-01T00:00:00Z"), is(true));
    }

    @Test
    public void shouldMatchDateRegardlessOfTimeZone() {
        assertThat(matcher.matches("2018-08-31T23:59:00+01:00"), is(true));
    }
}
