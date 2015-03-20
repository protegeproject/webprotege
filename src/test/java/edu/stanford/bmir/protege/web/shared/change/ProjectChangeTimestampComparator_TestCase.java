
package edu.stanford.bmir.protege.web.shared.change;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ProjectChangeTimestampComparator_TestCase {

    private ProjectChangeTimestampComparator comparator;

    @Mock
    private ProjectChange projectChange1;
    @Mock
    private ProjectChange projectChange2;

    @Before
    public void setUp() {
        comparator = new ProjectChangeTimestampComparator();
    }

    @Test
    public void shouldPlaceSmallerBeforeLarger() {
        when(projectChange1.getTimestamp()).thenReturn(5l);
        when(projectChange2.getTimestamp()).thenReturn(Long.MAX_VALUE - 5);
        assertThat(comparator.compare(projectChange1, projectChange2), is(lessThan(0)));
    }

    @Test
    public void shouldPlaceLargerAfterSmaller() {
        when(projectChange1.getTimestamp()).thenReturn(Long.MAX_VALUE - 5);
        when(projectChange2.getTimestamp()).thenReturn(5l);
        assertThat(comparator.compare(projectChange1, projectChange2), is(greaterThan(0)));
    }

    @Test
    public void shouldCompareEqual() {
        when(projectChange1.getTimestamp()).thenReturn(5l);
        when(projectChange2.getTimestamp()).thenReturn(5l);
        assertThat(comparator.compare(projectChange1, projectChange2), is(0));
    }
}
