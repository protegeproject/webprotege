
package edu.stanford.bmir.protege.web.shared.api;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeyInfo_TestCase {

    private ApiKeyInfo apiKeyInfo;

    @Mock
    private ApiKeyId apiKeyId;

    private long createdAt = 1L;

    private String purpose = "The purpose";

    @Before
    public void setUp() {
        apiKeyInfo = new ApiKeyInfo(apiKeyId, createdAt, purpose);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_apiKeyId_IsNull() {
        new ApiKeyInfo(null, createdAt, purpose);
    }

    @Test
    public void shouldReturnSupplied_apiKeyId() {
        assertThat(apiKeyInfo.getApiKeyId(), is(this.apiKeyId));
    }

    @Test
    public void shouldReturnSupplied_createdAt() {
        assertThat(apiKeyInfo.getCreatedAt(), is(this.createdAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_purpose_IsNull() {
        new ApiKeyInfo(apiKeyId, createdAt, null);
    }

    @Test
    public void shouldReturnSupplied_purpose() {
        assertThat(apiKeyInfo.getPurpose(), is(this.purpose));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(apiKeyInfo, is(apiKeyInfo));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(apiKeyInfo.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(apiKeyInfo, is(new ApiKeyInfo(apiKeyId, createdAt, purpose)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_apiKeyId() {
        assertThat(apiKeyInfo, is(not(new ApiKeyInfo(Mockito.mock(ApiKeyId.class), createdAt, purpose))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(apiKeyInfo, is(not(new ApiKeyInfo(apiKeyId, 2L, purpose))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_purpose() {
        assertThat(apiKeyInfo, is(not(new ApiKeyInfo(apiKeyId, createdAt, "String-f303a24a-2b5d-472d-ae61-0fa43d97f70b"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(apiKeyInfo.hashCode(), is(new ApiKeyInfo(apiKeyId, createdAt, purpose).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(apiKeyInfo.toString(), startsWith("ApiKeyInfo"));
    }

}
