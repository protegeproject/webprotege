package edu.stanford.bmir.protege.web.shared.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.BooleanMetricValue;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class BooleanMetricValueTestCase {

    private String testname;

    @Before
    public void setUp() throws Exception {
        testname = "testname";
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfNameIsNull() {
        new BooleanMetricValue(null, false);
    }

    @Test
    public void shouldReturnSpecifiedName() {
        BooleanMetricValue value = new BooleanMetricValue(testname, true);
        assertThat(value.getMetricName(), is(equalTo(testname)));
    }

    @Test
    public void shouldReturnTrueAsYes() {
        String testname = "testname";
        BooleanMetricValue value = new BooleanMetricValue(testname, true);
        assertThat(value.getBrowserText(), is(equalTo("Yes")));
    }


    @Test
    public void shouldReturnFalseAsNo() {
        String testname = "testname";
        BooleanMetricValue value = new BooleanMetricValue(testname, false);
        assertThat(value.getBrowserText(), is(equalTo("No")));
    }

}
