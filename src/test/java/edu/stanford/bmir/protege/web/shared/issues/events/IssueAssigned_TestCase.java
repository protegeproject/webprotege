
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class IssueAssigned_TestCase {

    private IssueAssigned issueAssigned;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Mock
    private UserId assignee;

    @Before
    public void setUp() {
        issueAssigned = new IssueAssigned(userId, timestamp, assignee);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueAssigned(null, timestamp, assignee);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_assignee_IsNull() {
        new IssueAssigned(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_assignee() {
        assertThat(issueAssigned.getAssignee(), is(this.assignee));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueAssigned, is(issueAssigned));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(issueAssigned.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueAssigned, is(new IssueAssigned(userId, timestamp, assignee)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueAssigned, is(not(new IssueAssigned(mock(UserId.class), timestamp, assignee))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueAssigned, is(not(new IssueAssigned(userId, 2L, assignee))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_assignee() {
        assertThat(issueAssigned, is(not(new IssueAssigned(userId, timestamp, mock(UserId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueAssigned.hashCode(), is(new IssueAssigned(userId, timestamp, assignee).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueAssigned.toString(), startsWith("IssueAssigned"));
    }

}
