
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.issues.Milestone;
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
public class IssueDemilestoned_TestCase {

    private IssueDemilestoned issueDemilestoned;
    @Mock
    private UserId userId;
    private long timestamp = 1L;
    @Mock
    private Milestone milestone;

    @Before
    public void setUp() {
        issueDemilestoned = new IssueDemilestoned(userId, timestamp, milestone);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueDemilestoned(null, timestamp, milestone);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_milestone_IsNull() {
        new IssueDemilestoned(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_milestone() {
        assertThat(issueDemilestoned.getMilestone(), is(this.milestone));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueDemilestoned, is(issueDemilestoned));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(issueDemilestoned.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueDemilestoned, is(new IssueDemilestoned(userId, timestamp, milestone)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueDemilestoned, is(not(new IssueDemilestoned(mock(UserId.class), timestamp, milestone))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueDemilestoned, is(not(new IssueDemilestoned(userId, 2L, milestone))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_milestone() {
        assertThat(issueDemilestoned, is(not(new IssueDemilestoned(userId, timestamp, mock(Milestone.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueDemilestoned.hashCode(), is(new IssueDemilestoned(userId, timestamp, milestone).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueDemilestoned.toString(), startsWith("IssueDemilestoned"));
    }

}
