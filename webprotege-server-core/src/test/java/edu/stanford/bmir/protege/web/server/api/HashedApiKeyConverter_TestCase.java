package edu.stanford.bmir.protege.web.server.api;

import org.junit.Test;
import org.mongodb.morphia.mapping.MappedField;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class HashedApiKeyConverter_TestCase {

    private String apiKeyString = "abcd";

    private HashedApiKey apiKey = HashedApiKey.valueOf(apiKeyString);

    private HashedApiKeyConverter converter = new HashedApiKeyConverter();

    @Test
    public void shouldConvertApiKeyToString() {
        String s = converter.encodeObject(apiKey, mock(MappedField.class));
        assertThat(s, is(apiKeyString));
    }

    @Test
    public void shouldConverStringToApiKey() {
        HashedApiKey k = converter.decodeObject(apiKeyString, mock(MappedField.class));
        assertThat(k.get(), is(apiKeyString));
    }

}
