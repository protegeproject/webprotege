package edu.stanford.bmir.protege.web.shared.metrics;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class GetMetricsResultTestCase {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullList() {
        new GetMetricsResult(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnSuppliedValue() {
        ImmutableList list = mock(ImmutableList.class);
        GetMetricsResult result = new GetMetricsResult(list);
        assertThat(result.getMetricValues(), is(list));
    }
}
