
package edu.stanford.bmir.protege.web.shared.user;

import edu.stanford.bmir.protege.web.shared.app.UserInSession;
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

@RunWith(MockitoJUnitRunner.class)
public class LogOutUserResult_TestCase {

    private LogOutUserResult logOutUserResult;
    @Mock
    private UserInSession userInSession;

    @Before
    public void setUp() {
        logOutUserResult = new LogOutUserResult(userInSession);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userInSession_IsNull() {
        new LogOutUserResult(null);
    }

    @Test
    public void shouldReturnSupplied_userInSession() {
        assertThat(logOutUserResult.getUserInSession(), is(this.userInSession));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(logOutUserResult, is(logOutUserResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(logOutUserResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(logOutUserResult, is(new LogOutUserResult(userInSession)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userInSession() {
        assertThat(logOutUserResult, is(not(new LogOutUserResult(Mockito.mock(UserInSession.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(logOutUserResult.hashCode(), is(new LogOutUserResult(userInSession).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(logOutUserResult.toString(), startsWith("LogOutUserResult"));
    }

}
