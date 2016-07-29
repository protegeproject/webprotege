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
 * 23/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class SignInDetails_TestCase {


    public static final String THE_USER_NAME = "The User Name";

    public static final String THE_PASSWORD = "The Password";

    private SignInDetails signInDetails;

    private SignInDetails otherSignInDetails;


    @Before
    public void setUp() throws Exception {
        signInDetails = new SignInDetails(THE_USER_NAME, THE_PASSWORD);
        otherSignInDetails = new SignInDetails(THE_USER_NAME, THE_PASSWORD);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_UserName_IsNull() {
        new SignInDetails(null, THE_PASSWORD);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Password_IsNull() {
        new SignInDetails(THE_USER_NAME, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(signInDetails, is(equalTo(signInDetails)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(signInDetails, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(signInDetails, is(equalTo(otherSignInDetails)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(signInDetails.hashCode(), is(otherSignInDetails.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(signInDetails.toString(), startsWith("SignInDetails"));
    }

    @Test
    public void shouldReturnSuppliedUserName() {
        assertThat(signInDetails.getUserName(), is(THE_USER_NAME));
    }

    @Test
    public void shouldReturnSuppliedPassword() {
        assertThat(signInDetails.getClearTextPassword(), is(THE_PASSWORD));
    }
}