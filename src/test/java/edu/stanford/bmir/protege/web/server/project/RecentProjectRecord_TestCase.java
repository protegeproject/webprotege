
package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RecentProjectRecord_TestCase {

    private RecentProjectRecord recentProjectRecord;

    @Mock
    private ProjectId projectId;

    private long timestamp = 1L;

    @Before
    public void setUp() {
        recentProjectRecord = new RecentProjectRecord(projectId, timestamp);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new RecentProjectRecord(null, timestamp);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(recentProjectRecord.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(recentProjectRecord.getTimestamp(), is(this.timestamp));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(recentProjectRecord, is(recentProjectRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(recentProjectRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(recentProjectRecord, is(new RecentProjectRecord(projectId, timestamp)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(recentProjectRecord, is(not(new RecentProjectRecord(mock(ProjectId.class), timestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(recentProjectRecord, is(not(new RecentProjectRecord(projectId, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(recentProjectRecord.hashCode(), is(new RecentProjectRecord(projectId, timestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(recentProjectRecord.toString(), startsWith("RecentProjectRecord"));
    }

    @Test
    public void shouldComeBefore() {
        RecentProjectRecord other = new RecentProjectRecord(mock(ProjectId.class), 0L);
        assertThat(recentProjectRecord.compareTo(other), is(lessThan(0)));
    }

    @Test
    public void shouldComeAfter() {
        RecentProjectRecord other = new RecentProjectRecord(mock(ProjectId.class), 2L);
        assertThat(recentProjectRecord.compareTo(other), is(greaterThan(0)));
    }
}
