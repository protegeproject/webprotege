package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class AuthenticateUserAction_TestCase {


    private AuthenticateUserAction action;

    private AuthenticateUserAction otherAction;

    @Mock
    private UserId userId;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private ChapResponse chapSessionResponse;


    @Before
    public void setUp() throws Exception {
        action = new AuthenticateUserAction(userId, chapSessionId, chapSessionResponse);
        otherAction = new AuthenticateUserAction(userId, chapSessionId, chapSessionResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new AuthenticateUserAction(null, chapSessionId, chapSessionResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ChapSessionId_IsNull() {
        new AuthenticateUserAction(userId, null, chapSessionResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ChapSessionResponse_IsNull() {
        new AuthenticateUserAction(userId, chapSessionId, null);
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
        assertThat(action.toString(), startsWith("AuthenticateUserAction"));
    }

    @Test
    public void shouldReturnSuppliedUserId() {
        assertThat(action.getUserId(), is(userId));
    }

    @Test
    public void shouldReturnSuppliedChapSessionId() {
        assertThat(action.getChapSessionId(), is(chapSessionId));
    }

    @Test
    public void shouldReturnSuppliedChapSessionResponse() {
        assertThat(action.getChapResponse(), is(chapSessionResponse));
    }
}