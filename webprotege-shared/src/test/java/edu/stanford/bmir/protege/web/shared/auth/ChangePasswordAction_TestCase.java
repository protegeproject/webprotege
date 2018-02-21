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
public class ChangePasswordAction_TestCase {


    private ChangePasswordAction action;

    private ChangePasswordAction otherAction;

    @Mock
    private UserId userId;

    @Mock
    private ChapSessionId chapSessionId;

    @Mock
    private ChapResponse chapSessionResponse;

    @Mock
    private SaltedPasswordDigest saltedPasswordDigest;

    @Mock
    private Salt newSalt;


    @Before
    public void setUp() throws Exception {
        action = new ChangePasswordAction(userId, chapSessionId, chapSessionResponse, saltedPasswordDigest, newSalt);
        otherAction = new ChangePasswordAction(userId, chapSessionId, chapSessionResponse, saltedPasswordDigest, newSalt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new ChangePasswordAction(null, chapSessionId, chapSessionResponse, saltedPasswordDigest, newSalt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ChapSessionId_IsNull() {
        new ChangePasswordAction(userId, null, chapSessionResponse, saltedPasswordDigest, newSalt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ChapSessionResponse_IsNull() {
        new ChangePasswordAction(userId, chapSessionId, null, saltedPasswordDigest, newSalt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_SaltedPasswordDigest_IsNull() {
        new ChangePasswordAction(userId, chapSessionId, chapSessionResponse, null, newSalt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_NewSalt_IsNull() {
        new ChangePasswordAction(userId, chapSessionId, chapSessionResponse, saltedPasswordDigest, null);
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
        assertThat(action.toString(), startsWith("ChangePasswordAction"));
    }

    @Test
    public void shouldReturnSupplied_UserId() {
        assertThat(action.getUserId(), is(userId));
    }

    @Test
    public void shouldReturnSupplied_ChapSessionId() {
        assertThat(action.getChapSessionId(), is(chapSessionId));
    }

    @Test
    public void shouldReturnSupplied_ChapSessionResponse() {
        assertThat(action.getChapResponse(), is(chapSessionResponse));
    }

    @Test
    public void shouldReturnSupplied_SaltedDigestedPassword() {
        assertThat(action.getNewPassword(), is(saltedPasswordDigest));
    }

    @Test
    public void shouldReturnSupplied_NewSalt() {
        assertThat(action.getNewSalt(), is(newSalt));
    }
}