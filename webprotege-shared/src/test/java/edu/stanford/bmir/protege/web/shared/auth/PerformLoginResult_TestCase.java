package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.app.UserInSession;
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
 * 18/02/15
 */
@SuppressWarnings("ConstantConditions")
@RunWith(MockitoJUnitRunner.class)
public class PerformLoginResult_TestCase {


    private PerformLoginResult result;

    private PerformLoginResult otherResult;

    @Mock
    private UserInSession userInSession;

    private AuthenticationResponse response = AuthenticationResponse.SUCCESS;

    @Before
    public void setUp() throws Exception {
        result = new PerformLoginResult(response, userInSession);
        otherResult = new PerformLoginResult(response, userInSession);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new PerformLoginResult(null, userInSession);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfUserDetailsIsNull() {
        new PerformLoginResult(response, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(equalTo(otherResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(result.hashCode(), is(otherResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(result.toString(), startsWith("PerformLoginResult"));
    }

    @Test
    public void shouldReturnSuppliedResponse() {
        assertThat(result.getResponse(), is(response));
    }
}