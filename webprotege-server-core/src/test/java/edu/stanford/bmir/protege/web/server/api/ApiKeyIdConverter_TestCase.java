package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import org.junit.Test;
import org.mongodb.morphia.mapping.MappedField;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class ApiKeyIdConverter_TestCase {

    private String id = "12345678-1234-1234-1234-123456789abc";

    private ApiKeyId apiKeyId = ApiKeyId.valueOf(id);

    private ApiKeyIdConverter converter = new ApiKeyIdConverter();

    @Test
    public void shouldConvertApiKeyToString() {
        String s = converter.encodeObject(apiKeyId, mock(MappedField.class));
        assertThat(s, is(id));
    }

    @Test
    public void shouldConverStringToApiKey() {
        ApiKeyId k = converter.decodeObject(id, mock(MappedField.class));
        assertThat(k.getId(), is(id));
    }
}
