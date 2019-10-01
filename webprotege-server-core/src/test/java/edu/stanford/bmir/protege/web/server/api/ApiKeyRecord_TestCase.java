
package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ApiKeyRecord_TestCase {

    private ApiKeyRecord apiKeyRecord;

    @Mock
    private ApiKeyId apiKeyId;

    @Mock
    private HashedApiKey apiKey;

    private long createdAt = 1L;

    private String purpose = "The purpose";

    @Before
    public void setUp()
        throws Exception
    {
        apiKeyRecord = new ApiKeyRecord(apiKeyId, apiKey, createdAt, purpose);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_apiKeyId_IsNull() {
        new ApiKeyRecord(null, apiKey, createdAt, purpose);
    }

    @Test
    public void shouldReturnSupplied_apiKeyId() {
        assertThat(apiKeyRecord.getApiKeyId(), is(this.apiKeyId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_apiKey_IsNull() {
        new ApiKeyRecord(apiKeyId, null, createdAt, purpose);
    }

    @Test
    public void shouldReturnSupplied_apiKey() {
        assertThat(apiKeyRecord.getApiKey(), is(this.apiKey));
    }

    @Test
    public void shouldReturnSupplied_createdAt() {
        assertThat(apiKeyRecord.getCreatedAt(), is(this.createdAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_purpose_IsNull() {
        new ApiKeyRecord(apiKeyId, apiKey, createdAt, null);
    }

    @Test
    public void shouldReturnSupplied_purpose() {
        assertThat(apiKeyRecord.getPurpose(), is(this.purpose));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(apiKeyRecord, is(apiKeyRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(apiKeyRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(apiKeyRecord, is(new ApiKeyRecord(apiKeyId, apiKey, createdAt, purpose)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_apiKeyId() {
        assertThat(apiKeyRecord, is(not(new ApiKeyRecord(mock(ApiKeyId.class), apiKey, createdAt, purpose))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_apiKey() {
        assertThat(apiKeyRecord, is(not(new ApiKeyRecord(apiKeyId, mock(HashedApiKey.class), createdAt, purpose))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(apiKeyRecord, is(not(new ApiKeyRecord(apiKeyId, apiKey, 2L, purpose))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_purpose() {
        assertThat(apiKeyRecord, is(not(new ApiKeyRecord(apiKeyId, apiKey, createdAt, "String-c461a645-b9fd-4104-a4a6-281ee9e5ec70"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(apiKeyRecord.hashCode(), is(new ApiKeyRecord(apiKeyId, apiKey, createdAt, purpose).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(apiKeyRecord.toString(), startsWith("ApiKeyRecord"));
    }

}
