package edu.stanford.bmir.protege.web.shared;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 08/12/15
 */
public class TimeUtil_TestCase {

    public static final long ONE_SECOND = 1000;

    public static final long ONE_MINUTE = 60 * ONE_SECOND;

    public static final long TWO_MINUTES = 2 * ONE_MINUTE;

    public static final long ONE_DAY = ONE_MINUTE * 60 * 24;

    public static final long TWO_DAYS = 2 * ONE_DAY;

    public static final long THU_SEPTEMBER_FIRST_2011 = 1314917789000l;

    public static final long THU_SEPTEMBER_TWENTY_FIRST_2011 = 1316645789000l;

    public static final long THU_SEPTEMBER_THIRTIETH_2011 = 1317423389000l;

    private final long t0 = 1000;

    @Test
    public void shouldReturnLessThanOneMinuteAgo() {
        long t1 = t0 + ONE_SECOND - 1000;
        String rendering = TimeUtil.getTimeRendering(t0, t1);
        assertThat(rendering, is("Less than one minute ago"));
    }

    @Test
    public void shouldReturnOneMinuteAgo() {
        long t1 = t0 + ONE_MINUTE;
        String rendering = TimeUtil.getTimeRendering(t0, t1);
        assertThat(rendering, is("One minute ago"));
    }

    @Test
    public void shouldReturnTwoMinutesAgo() {
        long t1 = t0 + TWO_MINUTES;
        String rendering = TimeUtil.getTimeRendering(t0, t1);
        assertThat(rendering, is("2 minutes ago"));
    }

    @Test
    public void shouldReturnYesterday() {
        long t1 = t0 + ONE_DAY;
        String rendering = TimeUtil.getTimeRendering(t0, t1);
        assertThat(rendering, startsWith("Yesterday at "));
    }

    @Test
    public void shouldReturnTwoDaysAgo() {
        long t1 = t0 + TWO_DAYS;
        String rendering = TimeUtil.getTimeRendering(t0, t1);
        assertThat(rendering, is("2 days ago"));
    }

    @Test
    public void shouldReturnDateRendering() {
        String rendering = TimeUtil.getTimeRendering(THU_SEPTEMBER_FIRST_2011);
        assertThat(rendering, is("1st September 2011"));
    }

    @Test
    public void shouldReturnDateRendering2() {
        String rendering = TimeUtil.getTimeRendering(THU_SEPTEMBER_FIRST_2011 + ONE_DAY);
        assertThat(rendering, is("2nd September 2011"));
    }

    @Test
    public void shouldReturnDateRendering3() {
        String rendering = TimeUtil.getTimeRendering(THU_SEPTEMBER_FIRST_2011 + ONE_DAY * 2);
        assertThat(rendering, is("3rd September 2011"));
    }

    @Test
    public void shouldReturnDateRendering4() {
        String rendering = TimeUtil.getTimeRendering(THU_SEPTEMBER_FIRST_2011 + ONE_DAY * 3);
        assertThat(rendering, is("4th September 2011"));
    }

    @Test
    public void shouldReturnDateRendering21() {
        String rendering = TimeUtil.getTimeRendering(THU_SEPTEMBER_TWENTY_FIRST_2011);
        assertThat(rendering, is("21st September 2011"));
    }

    @Test
    public void shouldReturnDateRendering30() {
        String rendering = TimeUtil.getTimeRendering(THU_SEPTEMBER_THIRTIETH_2011);
        assertThat(rendering, is("30th September 2011"));
    }
}
