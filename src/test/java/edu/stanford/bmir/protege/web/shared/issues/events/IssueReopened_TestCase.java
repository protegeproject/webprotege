
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class IssueReopened_TestCase {

    private IssueReopened issueReopened;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Before
    public void setUp() {
        issueReopened = new IssueReopened(userId, timestamp);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueReopened(null, timestamp);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueReopened.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueReopened.getTimestamp(), is(this.timestamp));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueReopened, is(issueReopened));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueReopened.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueReopened, is(new IssueReopened(userId, timestamp)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueReopened, is(not(new IssueReopened(mock(UserId.class), timestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueReopened, is(not(new IssueReopened(userId, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueReopened.hashCode(), is(new IssueReopened(userId, timestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueReopened.toString(), startsWith("IssueReopened"));
    }

}
