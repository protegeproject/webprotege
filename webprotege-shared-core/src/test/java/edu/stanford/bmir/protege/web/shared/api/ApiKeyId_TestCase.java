
package edu.stanford.bmir.protege.web.shared.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeyId_TestCase {

    private String id = "12345678-1234-1234-1234-123456789abc";

    private ApiKeyId apiKeyId;

    @Before
    public void setUp() {
        apiKeyId = ApiKeyId.valueOf(id);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(apiKeyId, is(apiKeyId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(apiKeyId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(apiKeyId, is(ApiKeyId.valueOf(id)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(apiKeyId.hashCode(), is(ApiKeyId.valueOf(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(apiKeyId.toString(), Matchers.startsWith("ApiKeyId"));
    }

    @Test
    public void should_getId() {
        assertThat(apiKeyId.getId(), is(id));
    }

}
