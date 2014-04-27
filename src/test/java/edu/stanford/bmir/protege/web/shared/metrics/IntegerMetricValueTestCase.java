package edu.stanford.bmir.protege.web.shared.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.IntegerMetricValue;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class IntegerMetricValueTestCase {

    public static final int INT_VALUE = 3;

    private String testname;

    private IntegerMetricValue value;

    @Before
    public void setUp() throws Exception {
        testname = "testname";
        value = new IntegerMetricValue(testname, INT_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfNameIsNull() {
        new IntegerMetricValue(null, INT_VALUE);
    }

    @Test
    public void shouldReturnSpecifiedName() {
        assertThat(value.getMetricName(), is(equalTo(testname)));
    }

    @Test
    public void shouldReturnIntegerLiteralAsDisplayText() {
        assertThat(value.getBrowserText(), is(equalTo(Integer.toString(INT_VALUE))));
    }
}
