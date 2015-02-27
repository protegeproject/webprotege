package edu.stanford.bmir.protege.web.shared.change;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectChangeTimestampComparator_TestCase {

    public static final long T1 = 3L;
    public static final long T2 = 5L;
    private ProjectChangeTimestampComparator comparator;

    @Mock
    private ProjectChange change1;

    @Mock
    private ProjectChange change2;

    @Before
    public void setUp() throws Exception {
        comparator = new ProjectChangeTimestampComparator();
    }

    @Test
    public void shouldCompareTimestamps() {
        when(change1.getTimestamp()).thenReturn(T1);
        when(change2.getTimestamp()).thenReturn(T2);
        int expectedValue = (int)(T1 - T2);
        assertThat(comparator.compare(change1, change2), is(expectedValue));
    }
}
