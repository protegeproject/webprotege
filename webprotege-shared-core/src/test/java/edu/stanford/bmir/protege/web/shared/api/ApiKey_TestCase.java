
package edu.stanford.bmir.protege.web.shared.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class ApiKey_TestCase {

    private static final String LEXICAL_VALUE = "The Api Key";

    private ApiKey apiKey;

    @Before
    public void setUp() {
        apiKey = ApiKey.valueOf(LEXICAL_VALUE);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(apiKey, is(apiKey));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(apiKey.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(apiKey, is(ApiKey.valueOf(LEXICAL_VALUE)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(apiKey.hashCode(), is(ApiKey.valueOf(LEXICAL_VALUE).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(apiKey.toString(), startsWith("ApiKey"));
    }

    @Test
    public void should_getId() {
        assertThat(apiKey.getKey(), is(LEXICAL_VALUE));
    }

}
