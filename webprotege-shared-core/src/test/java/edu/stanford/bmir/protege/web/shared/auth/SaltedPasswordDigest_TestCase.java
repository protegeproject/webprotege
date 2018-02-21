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
public class SaltedPasswordDigest_TestCase {


    private SaltedPasswordDigest saltedPasswordDigest;

    private SaltedPasswordDigest otherSaltedPasswordDigest;

    private byte [] digest = {1, 2, 3, 4};

    @Before
    public void setUp() throws Exception {
        saltedPasswordDigest = new SaltedPasswordDigest(digest);
        otherSaltedPasswordDigest = new SaltedPasswordDigest(digest);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new SaltedPasswordDigest(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(saltedPasswordDigest, is(equalTo(saltedPasswordDigest)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(saltedPasswordDigest, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(saltedPasswordDigest, is(equalTo(otherSaltedPasswordDigest)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(saltedPasswordDigest.hashCode(), is(otherSaltedPasswordDigest.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(saltedPasswordDigest.toString(), startsWith("SaltedPasswordDigest"));
    }

    @Test
    public void shouldReturnDigest() {
        assertThat(saltedPasswordDigest.getBytes(), is(digest));
        assertThat(saltedPasswordDigest.getBytes() == digest, is(false));
    }

}