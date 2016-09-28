
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
public class IssueLocked_TestCase {

    private IssueLocked issueLocked;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Before
    public void setUp() {
        issueLocked = new IssueLocked(userId, timestamp);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueLocked(null, timestamp);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueLocked.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueLocked.getTimestamp(), is(this.timestamp));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueLocked, is(issueLocked));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueLocked.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueLocked, is(new IssueLocked(userId, timestamp)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueLocked, is(not(new IssueLocked(mock(UserId.class), timestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueLocked, is(not(new IssueLocked(userId, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueLocked.hashCode(), is(new IssueLocked(userId, timestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueLocked.toString(), startsWith("IssueLocked"));
    }

}
