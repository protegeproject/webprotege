package edu.stanford.bmir.protege.web.shared.user;

import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
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
public class CreateUserAccountAction_TestCase {


    private CreateUserAccountAction action;

    private CreateUserAccountAction otherAction;

    @Mock
    private UserId userId;

    @Mock
    private EmailAddress emailAddress;

    @Mock
    private SaltedPasswordDigest saltedPasswordDigest;

    @Mock
    private Salt salt;


    @Before
    public void setUp() throws Exception {
        action = new CreateUserAccountAction(userId, emailAddress, saltedPasswordDigest, salt);
        otherAction = new CreateUserAccountAction(userId, emailAddress, saltedPasswordDigest, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserId_IsNull() {
        new CreateUserAccountAction(null, emailAddress, saltedPasswordDigest, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_EmailAddress_IsNull() {
        new CreateUserAccountAction(userId, null, saltedPasswordDigest, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_SaltedPasswordDigest_IsNull() {
        new CreateUserAccountAction(userId, emailAddress, null, salt);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Salt_IsNull() {
        new CreateUserAccountAction(userId, emailAddress, saltedPasswordDigest, null);
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
        assertThat(action.toString(), startsWith("CreateUserAccountAction"));
    }

    @Test
    public void shouldReturnSupplied_UserId() {
        assertThat(action.getUserId(), is(userId));
    }

    @Test
    public void shouldReturnSupplied_EmailAddress() {
        assertThat(action.getEmailAddress(), is(emailAddress));
    }

    @Test
    public void shouldReturnSupplied_SaltedPasswordDigest() {
        assertThat(action.getPasswordDigest(), is(saltedPasswordDigest));
    }

    @Test
    public void shouldReturnSupplied_Salt() {
        assertThat(action.getSalt(), is(salt));
    }
}