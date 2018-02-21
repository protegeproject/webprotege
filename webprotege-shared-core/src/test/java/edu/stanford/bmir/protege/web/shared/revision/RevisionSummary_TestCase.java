
package edu.stanford.bmir.protege.web.shared.revision;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RevisionSummary_TestCase {

    private RevisionSummary revisionSummary;
    @Mock
    private RevisionNumber revisionNumber;
    @Mock
    private UserId userId;

    private long timestamp = 1L;

    private int changeCount = 1;

    private String description = "The description";

    @Before
    public void setUp()
        throws Exception
    {
        revisionSummary = new RevisionSummary(revisionNumber, userId, timestamp, changeCount, description);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_revisionNumber_IsNull() {
        new RevisionSummary(null, userId, timestamp, changeCount, description);
    }

    @Test
    public void shouldReturnSupplied_revisionNumber() {
        assertThat(revisionSummary.getRevisionNumber(), is(this.revisionNumber));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_usedId_IsNull() {
        new RevisionSummary(revisionNumber, null, timestamp, changeCount, description);
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(revisionSummary.getTimestamp(), is(this.timestamp));
    }

    @Test
    public void shouldReturnSupplied_changeCount() {
        assertThat(revisionSummary.getChangeCount(), is(this.changeCount));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_description_IsNull() {
        new RevisionSummary(revisionNumber, userId, timestamp, changeCount, null);
    }

    @Test
    public void shouldReturnSupplied_description() {
        assertThat(revisionSummary.getDescription(), is(this.description));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(revisionSummary, is(revisionSummary));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(revisionSummary.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(revisionSummary, is(new RevisionSummary(revisionNumber, userId, timestamp, changeCount, description)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_revisionNumber() {
        assertThat(revisionSummary, is(not(new RevisionSummary(mock(RevisionNumber.class), userId, timestamp, changeCount, description))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_usedId() {
        assertThat(revisionSummary, is(not(new RevisionSummary(revisionNumber, mock(UserId.class), timestamp, changeCount, description))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(revisionSummary, is(not(new RevisionSummary(revisionNumber, userId, 2L, changeCount, description))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_changeCount() {
        assertThat(revisionSummary, is(not(new RevisionSummary(revisionNumber, userId, timestamp, 2, description))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_description() {
        assertThat(revisionSummary, is(not(new RevisionSummary(revisionNumber, userId, timestamp, changeCount, "String-8e6bda86-c114-4708-9f41-c19bb9d5d9c1"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(revisionSummary.hashCode(), is(new RevisionSummary(revisionNumber, userId, timestamp, changeCount, description).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(revisionSummary.toString(), Matchers.startsWith("RevisionSummary"));
    }

    @Test
    public void should_getUserId() {
        assertThat(revisionSummary.getUserId(), is(userId));
    }


}
