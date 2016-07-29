
package edu.stanford.bmir.protege.web.shared.place;

import edu.stanford.bmir.protege.web.client.login.LoginPresenter;
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

@RunWith(MockitoJUnitRunner.class)
public class LoginActivity_TestCase {

    private LoginActivity loginActivity;
    @Mock
    private LoginPresenter presenter;

    @Before
    public void setUp()
        throws Exception
    {
        loginActivity = new LoginActivity(presenter);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_presenter_IsNull() {
        new LoginActivity(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(loginActivity, is(loginActivity));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(loginActivity.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(loginActivity, is(new LoginActivity(presenter)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_presenter() {
        assertThat(loginActivity, is(not(new LoginActivity(Mockito.mock(LoginPresenter.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(loginActivity.hashCode(), is(new LoginActivity(presenter).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(loginActivity.toString(), Matchers.startsWith("LoginActivity"));
    }
}
