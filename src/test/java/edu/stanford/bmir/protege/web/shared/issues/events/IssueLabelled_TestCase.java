
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
public class IssueLabelled_TestCase {

    private IssueLabelled issueLabelled;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    private String label = "The label";

    @Before
    public void setUp() {
        issueLabelled = new IssueLabelled(userId, timestamp, label);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueLabelled(null, timestamp, label);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_label_IsNull() {
        new IssueLabelled(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_label() {
        assertThat(issueLabelled.getLabel(), is(this.label));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueLabelled, is(issueLabelled));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(issueLabelled.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueLabelled, is(new IssueLabelled(userId, timestamp, label)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueLabelled, is(not(new IssueLabelled(mock(UserId.class), timestamp, label))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueLabelled, is(not(new IssueLabelled(userId, 2L, label))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_label() {
        assertThat(issueLabelled, is(not(new IssueLabelled(userId, timestamp, "String-727db00a-257f-4e33-aba5-8290d93a2dc8"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueLabelled.hashCode(), is(new IssueLabelled(userId, timestamp, label).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueLabelled.toString(), startsWith("IssueLabelled"));
    }

}
