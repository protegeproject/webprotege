
package edu.stanford.bmir.protege.web.server.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class HashedApiKey_TestCase {

    private String apiKey = "abc";

    private HashedApiKey hashedApiKey;

    @Before
    public void setUp() {
        hashedApiKey = HashedApiKey.valueOf(apiKey);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(hashedApiKey, is(hashedApiKey));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(hashedApiKey.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(hashedApiKey, is(HashedApiKey.valueOf(apiKey)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(hashedApiKey.hashCode(), is(HashedApiKey.valueOf(apiKey).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(hashedApiKey.toString(), startsWith("HashedApiKey"));
    }

    @Test
    public void should_get() {
        assertThat(hashedApiKey.get(), is(apiKey));
    }

}
