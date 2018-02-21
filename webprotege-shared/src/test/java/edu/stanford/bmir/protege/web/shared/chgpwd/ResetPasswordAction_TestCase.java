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
public class ResetPasswordAction_TestCase {

    @Mock
    private ResetPasswordData data;

    private ResetPasswordAction action;

    @Before
    public void setUp() throws Exception {
        action = new ResetPasswordAction(data);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ResetPasswordAction(null);
    }

    @Test
    public void shouldBeEqualToOther() {
        ResetPasswordAction other = new ResetPasswordAction(data);
        assertThat(action.equals(other), is(true));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action.equals(action), is(true));
    }

    @Test
    public void shouldHaveEqualHashCodes() {
        ResetPasswordAction other = new ResetPasswordAction(data);
        assertThat(action.hashCode(), is(other.hashCode()));
    }

    @Test
    public void shouldReturnSuppliedData() {
        assertThat(action.getResetPasswordData(), is(data));
    }

}
