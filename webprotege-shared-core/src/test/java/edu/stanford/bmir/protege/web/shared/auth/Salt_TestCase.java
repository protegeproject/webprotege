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
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class Salt_TestCase {


    private Salt salt;

    private Salt otherSalt;

    private byte [] bytes = {2, 2, 2, 2, 2};

    @Before
    public void setUp() throws Exception {
        salt = new Salt(bytes);
        otherSalt = new Salt(bytes);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new Salt(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(salt, is(equalTo(salt)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(salt, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(salt, is(equalTo(otherSalt)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(salt.hashCode(), is(otherSalt.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(salt.toString(), startsWith("Salt"));
    }

    @Test
    public void shouldReturnSuppliedBytes() {
        assertThat(salt.getBytes(), is(bytes));
    }

}