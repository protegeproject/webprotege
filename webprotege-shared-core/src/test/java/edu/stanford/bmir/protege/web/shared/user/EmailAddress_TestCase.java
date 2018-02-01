
package edu.stanford.bmir.protege.web.shared.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class EmailAddress_TestCase {

    private EmailAddress emailAddress;

    private String address = "x@y.com";

    @Before
    public void setUp() {
        emailAddress = new EmailAddress(address);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_address_IsNull() {
        new EmailAddress(null);
    }

    @Test
    public void shouldReturnSupplied_address() {
        assertThat(emailAddress.getEmailAddress(), is(this.address));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(emailAddress, is(emailAddress));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(emailAddress.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(emailAddress, is(new EmailAddress(address)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_address() {
        assertThat(emailAddress, is(not(new EmailAddress("String-de67417b-eb48-4ebc-a141-08e715a54719"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(emailAddress.hashCode(), is(new EmailAddress(address).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(emailAddress.toString(), startsWith("EmailAddress"));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        EmailAddress emailAddress = new EmailAddress("");
        assertThat(emailAddress.isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        assertThat(emailAddress.isEmpty(), is(false));
    }

    @Test
    public void should_getEmailAddress() {
        assertThat(emailAddress.getEmailAddress(), is(address));
    }

}
