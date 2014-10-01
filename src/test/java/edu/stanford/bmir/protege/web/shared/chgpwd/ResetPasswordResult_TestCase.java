package edu.stanford.bmir.protege.web.shared.chgpwd;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordResult_TestCase {

    private ResetPasswordResultCode code;

    private ResetPasswordResult result;

    @Before
    public void setUp() throws Exception {
        code = ResetPasswordResultCode.SUCCESS;
        result = new ResetPasswordResult(code);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ResetPasswordResult(null);
    }

    @Test
    public void shouldBeEqualToOther() {
        ResetPasswordResult other = new ResetPasswordResult(code);
        assertThat(result.equals(other), is(true));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result.equals(result), is(true));
    }

    @Test
    public void shouldHaveEqualHashCodes() {
        ResetPasswordResult other = new ResetPasswordResult(code);
        assertThat(result.hashCode(), is(other.hashCode()));
    }

    @Test
    public void shouldReturnSuppliedCode() {
        assertThat(result.getResultCode(), is(code));
    }
}
