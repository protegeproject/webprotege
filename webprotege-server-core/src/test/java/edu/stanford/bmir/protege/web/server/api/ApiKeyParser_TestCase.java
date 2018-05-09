package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class ApiKeyParser_TestCase {

    private ApiKey expectedApiKey = ApiKey.valueOf("test key");

    @Test
    public void shouldReturnEmptyForNullValue() {
        assertThat(ApiKeyParser.parseApiKey(null), is(Optional.empty()));
    }

    @Test
    public void shouldReturnEmptyForMissingApiPrefix() {
        assertThat(ApiKeyParser.parseApiKey("test key"), is(Optional.empty()));
    }

    @Test
    public void shouldReturnEmptyForMissingApiKey() {
        assertThat(ApiKeyParser.parseApiKey("apikey"), is(Optional.empty()));
    }

    @Test
    public void shouldParseApiKey() {
        assertThat(ApiKeyParser.parseApiKey("apikey test key"), is(Optional.of(expectedApiKey)));
    }

    @Test
    public void shouldParseApiKeyCaseInsensitive() {
        assertThat(ApiKeyParser.parseApiKey("ApiKey test key"), is(Optional.of(expectedApiKey)));
    }

    @Test
    public void shouldParseApiKeyWithMultiSpaceSeparator() {
        assertThat(ApiKeyParser.parseApiKey("apikey   test key"), is(Optional.of(expectedApiKey)));
    }

    @Test
    public void shouldParseApiKeyWithTrailingSpace() {
        assertThat(ApiKeyParser.parseApiKey("apikey test key"), is(Optional.of(expectedApiKey)));
    }
}
