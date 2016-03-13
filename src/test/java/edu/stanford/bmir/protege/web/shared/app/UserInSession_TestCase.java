package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/12/14
 */
@RunWith(MockitoJUnitRunner.class)
public class UserInSession_TestCase {


    private UserInSession userInSession;

    private UserInSession otherUserInSession;

    @Mock
    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception {
        userInSession = new UserInSession(userDetails);
        otherUserInSession = new UserInSession(userDetails);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIsUserDetailsIsNull() {
        new UserInSession(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userInSession, is(equalTo(userInSession)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(userInSession, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userInSession, is(equalTo(otherUserInSession)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(userInSession.hashCode(), is(otherUserInSession.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(userInSession.toString(), startsWith("UserInSession"));
    }

    @Test
    public void shouldReturnSuppliedUserDetails() {
        assertThat(userInSession.getUserDetails(), is(userDetails));
    }


}