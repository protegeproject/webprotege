package edu.stanford.bmir.protege.web.shared.metrics;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/04/2014
 */
public class ProfileMetricValueTestCase {


    public static final String NAME = "name";

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfNameIsNull() {
        new ProfileMetricValue(null, false);
    }

    @Test
    public void shouldReturnNonNullValueForBrowserText() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, true);
        assertThat(value.getBrowserText(), is(not(nullValue())));
    }

    @Test
    public void shouldReturnSuppliedName() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, true);
        assertThat(value.getProfileName(), is(equalTo(NAME)));
    }

    @Test
    public void shouldReturnProfileNameForMetricName() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, true);
        assertThat(value.getMetricName(), is(equalTo(NAME)));
    }

    @Test
    public void shouldReturnTrueIfSuppliedTrue() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, true);
        assertThat(value.isInProfile(), is(true));
    }

    @Test
    public void shouldReturnFalseIfSuppliedFalse() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, false);
        assertThat(value.isInProfile(), is(false));
    }

    @Test
    public void shouldNotBeEmptyIfFalse() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, false);
        assertThat(value.isEmpty(), is(false));
    }

    @Test
    public void shouldNotBeEmptyIfTrue() {
        ProfileMetricValue value = new ProfileMetricValue(NAME, true);
        assertThat(value.isEmpty(), is(false));
    }
}
