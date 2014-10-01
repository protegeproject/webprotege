package edu.stanford.bmir.protege.web.shared.chgpwd;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordData_TestCase {


    public static final String EMAIL_ADDRESS = "email-address";

    private ResetPasswordData data;

    @Before
    public void setUp() throws Exception {
        data = new ResetPasswordData(EMAIL_ADDRESS);
    }

    @Test
    public void shouldBeEqualToOther() {
        ResetPasswordData other = new ResetPasswordData(EMAIL_ADDRESS);
        assertThat(data.equals(other), is(true));
    }

    @Test
    public void shouldHaveEqualHashCode() {
        ResetPasswordData other = new ResetPasswordData(EMAIL_ADDRESS);
        assertThat(data.hashCode(), is(other.hashCode()));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(data.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(data.equals(data), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ResetPasswordData(null);
    }

    @Test
    public void shouldReturnSuppliedEmailAddress() {
        assertThat(data.getEmailAddress(), is(EMAIL_ADDRESS));
    }

}
