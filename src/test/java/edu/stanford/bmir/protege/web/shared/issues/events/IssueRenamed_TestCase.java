
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class IssueRenamed_TestCase {

    private IssueRenamed issueRenamed;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    private String from = "The from";

    private String to = "The to";

    @Before
    public void setUp() {
        issueRenamed = new IssueRenamed(userId, timestamp, from, to);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueRenamed(null, timestamp, from, to);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_from_IsNull() {
        new IssueRenamed(userId, timestamp, null, to);
    }

    @Test
    public void shouldReturnSupplied_from() {
        assertThat(issueRenamed.getFrom(), is(this.from));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_to_IsNull() {
        new IssueRenamed(userId, timestamp, from, null);
    }

    @Test
    public void shouldReturnSupplied_to() {
        assertThat(issueRenamed.getTo(), is(this.to));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueRenamed, is(issueRenamed));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueRenamed.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueRenamed, is(new IssueRenamed(userId, timestamp, from, to)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueRenamed, is(not(new IssueRenamed(mock(UserId.class), timestamp, from, to))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueRenamed, is(not(new IssueRenamed(userId, 2L, from, to))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_from() {
        assertThat(issueRenamed, is(not(new IssueRenamed(userId, timestamp, "String-b732cb1b-aa8a-4e0f-8ab9-d197f50c2e34", to))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_to() {
        assertThat(issueRenamed, is(not(new IssueRenamed(userId, timestamp, from, "String-b61dab75-71be-4fe5-9e3b-1fc8275ffdce"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueRenamed.hashCode(), is(new IssueRenamed(userId, timestamp, from, to).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueRenamed.toString(), Matchers.startsWith("IssueRenamed"));
    }

}
