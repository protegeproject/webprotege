
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class IssueUnlabelled_TestCase {

    private IssueUnlabelled issueUnlabelled;
    @Mock
    private UserId userId;
    private long timestamp = 1L;
    private String label = "The label";

    @Before
    public void setUp()
    {
        issueUnlabelled = new IssueUnlabelled(userId, timestamp, label);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueUnlabelled(null, timestamp, label);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueUnlabelled.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueUnlabelled.getTimestamp(), is(this.timestamp));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_label_IsNull() {
        new IssueUnlabelled(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_label() {
        assertThat(issueUnlabelled.getLabel(), is(this.label));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueUnlabelled, is(issueUnlabelled));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueUnlabelled.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueUnlabelled, is(new IssueUnlabelled(userId, timestamp, label)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueUnlabelled, is(not(new IssueUnlabelled(mock(UserId.class), timestamp, label))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueUnlabelled, is(not(new IssueUnlabelled(userId, 2L, label))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_label() {
        assertThat(issueUnlabelled, is(not(new IssueUnlabelled(userId, timestamp, "String-65c6368f-d20c-42f1-bd84-3ae113d74ac1"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueUnlabelled.hashCode(), is(new IssueUnlabelled(userId, timestamp, label).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueUnlabelled.toString(), startsWith("IssueUnlabelled"));
    }

}
