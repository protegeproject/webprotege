
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class IssueClosed_TestCase {

    private IssueClosed issueClosed;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Before
    public void setUp() {
        issueClosed = new IssueClosed(userId, timestamp);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueClosed(null, timestamp);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueClosed, is(issueClosed));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(issueClosed.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueClosed, is(new IssueClosed(userId, timestamp)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueClosed, is(not(new IssueClosed(mock(UserId.class), timestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueClosed, is(not(new IssueClosed(userId, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueClosed.hashCode(), is(new IssueClosed(userId, timestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueClosed.toString(), startsWith("IssueClosed"));
    }

}
