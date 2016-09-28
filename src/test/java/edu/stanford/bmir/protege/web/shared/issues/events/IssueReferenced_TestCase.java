
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
public class IssueReferenced_TestCase {

    private IssueReferenced issueReferenced;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    private int issueNumber = 1;

    private int referencedByIssueNumber = 2;

    @Before
    public void setUp()
            throws Exception {
        issueReferenced = new IssueReferenced(userId, timestamp, issueNumber, referencedByIssueNumber);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueReferenced(null, timestamp, issueNumber, referencedByIssueNumber);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueReferenced.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueReferenced.getTimestamp(), is(this.timestamp));
    }

    @Test
    public void shouldReturnSupplied_issueNumber() {
        assertThat(issueReferenced.getIssueNumber(), is(this.issueNumber));
    }

    @Test
    public void shouldReturnSupplied_referencedByIssueNumber() {
        assertThat(issueReferenced.getReferencedByIssueNumber(), is(this.referencedByIssueNumber));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueReferenced, is(issueReferenced));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueReferenced.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueReferenced, is(new IssueReferenced(userId, timestamp, issueNumber, referencedByIssueNumber)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueReferenced, is(not(new IssueReferenced(mock(UserId.class), timestamp, issueNumber, referencedByIssueNumber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueReferenced, is(not(new IssueReferenced(userId, 2L, issueNumber, referencedByIssueNumber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_issueNumber() {
        assertThat(issueReferenced, is(not(new IssueReferenced(userId, timestamp, 3, referencedByIssueNumber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_referencedByIssueNumber() {
        assertThat(issueReferenced, is(not(new IssueReferenced(userId, timestamp, issueNumber, 4))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueReferenced.hashCode(), is(new IssueReferenced(userId, timestamp, issueNumber, referencedByIssueNumber).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueReferenced.toString(), startsWith("IssueReferenced"));
    }

}
