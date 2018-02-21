package edu.stanford.bmir.protege.web.shared.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticateUserResult_TestCase {


    private AuthenticateUserResult action;

    private AuthenticateUserResult otherAction;

    private AuthenticationResponse response = AuthenticationResponse.SUCCESS;


    @Before
    public void setUp() throws Exception {
        action = new AuthenticateUserResult(response);
        otherAction = new AuthenticateUserResult(response);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new AuthenticateUserResult(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(equalTo(action)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(equalTo(otherAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(action.hashCode(), is(otherAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(action.toString(), startsWith("AuthenticateUserResult"));
    }

    @Test
    public void shouldReturnSuppliedResponse() {
        assertThat(action.getResponse(), is(response));
    }
}