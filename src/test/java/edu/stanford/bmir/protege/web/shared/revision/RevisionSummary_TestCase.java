
package edu.stanford.bmir.protege.web.shared.revision;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RevisionSummary_TestCase {

    private RevisionSummary revisionSummary;

    @Mock
    private RevisionNumber revisionNumber;

    @Mock
    private UserId usedId;

    private long timestamp = 33;

    private int changeCount = 5;

    @Before
    public void setUp() throws Exception {
        revisionSummary = new RevisionSummary(revisionNumber, usedId, timestamp, changeCount);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_revisionNumber_IsNull() {
        new RevisionSummary(null, usedId, timestamp, changeCount);
    }

    @Test
    public void shouldReturnSupplied_revisionNumber() {
        MatcherAssert.assertThat(revisionSummary.getRevisionNumber(), Matchers.is(this.revisionNumber));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_usedId_IsNull() {
        new RevisionSummary(revisionNumber, null, timestamp, changeCount);
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        MatcherAssert.assertThat(revisionSummary.getTimestamp(), Matchers.is(this.timestamp));
    }

    @Test
    public void shouldReturnSupplied_changeCount() {
        MatcherAssert.assertThat(revisionSummary.getChangeCount(), Matchers.is(this.changeCount));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(revisionSummary, Matchers.is(revisionSummary));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(revisionSummary.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(revisionSummary, Matchers.is(new RevisionSummary(revisionNumber, usedId, timestamp, changeCount)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_revisionNumber() {
        MatcherAssert.assertThat(revisionSummary, Matchers.is(Matchers.not(new RevisionSummary(Mockito.mock(RevisionNumber.class), usedId, timestamp, changeCount))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_usedId() {
        MatcherAssert.assertThat(revisionSummary, Matchers.is(Matchers.not(new RevisionSummary(revisionNumber, Mockito.mock(UserId.class), timestamp, changeCount))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        MatcherAssert.assertThat(revisionSummary, Matchers.is(Matchers.not(new RevisionSummary(revisionNumber, usedId, 2L, changeCount))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_changeCount() {
        MatcherAssert.assertThat(revisionSummary, Matchers.is(Matchers.not(new RevisionSummary(revisionNumber, usedId, timestamp, 2))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(revisionSummary.hashCode(), Matchers.is(new RevisionSummary(revisionNumber, usedId, timestamp, changeCount).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(revisionSummary.toString(), Matchers.startsWith("RevisionSummary"));
    }

    @Test
    public void should_getUserId() {
        MatcherAssert.assertThat(revisionSummary.getUserId(), Matchers.is(usedId));
    }

}
