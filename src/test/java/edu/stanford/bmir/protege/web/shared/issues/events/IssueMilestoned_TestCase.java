
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.issues.Milestone;
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
public class IssueMilestoned_TestCase {

    private IssueMilestoned issueMilestoned;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Mock
    private Milestone milestone;

    @Before
    public void setUp() {
        issueMilestoned = new IssueMilestoned(userId, timestamp, milestone);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueMilestoned(null, timestamp, milestone);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_milestone_IsNull() {
        new IssueMilestoned(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_milestone() {
        assertThat(issueMilestoned.getMilestone(), is(this.milestone));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueMilestoned, is(issueMilestoned));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(issueMilestoned.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueMilestoned, is(new IssueMilestoned(userId, timestamp, milestone)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueMilestoned, is(not(new IssueMilestoned(mock(UserId.class), timestamp, milestone))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueMilestoned, is(not(new IssueMilestoned(userId, 2L, milestone))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_milestone() {
        assertThat(issueMilestoned, is(not(new IssueMilestoned(userId, timestamp, mock(Milestone.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueMilestoned.hashCode(), is(new IssueMilestoned(userId, timestamp, milestone).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueMilestoned.toString(), startsWith("IssueMilestoned"));
    }

}
