
package edu.stanford.bmir.protege.web.shared.mail;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.NullPointerException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SetEmailAddressResult_TestCase {

    private SetEmailAddressResult setEmailAddressResult;

    private SetEmailAddressResult.Result result;

    @Before
    public void setUp()
        throws Exception
    {
        result = SetEmailAddressResult.Result.ADDRESS_CHANGED;
        setEmailAddressResult = new SetEmailAddressResult(result);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_result_IsNull() {
        new SetEmailAddressResult(null);
    }

    @Test
    public void shouldReturnSupplied_result() {
        assertThat(setEmailAddressResult.getResult(), is(this.result));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setEmailAddressResult, is(setEmailAddressResult));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(setEmailAddressResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setEmailAddressResult, is(new SetEmailAddressResult(result)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_result() {
        assertThat(setEmailAddressResult, is(Matchers.not(new SetEmailAddressResult(SetEmailAddressResult.Result.ADDRESS_ALREADY_EXISTS))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setEmailAddressResult.hashCode(), is(new SetEmailAddressResult(result).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setEmailAddressResult.toString(), Matchers.startsWith("SetEmailAddressResult"));
    }

}
