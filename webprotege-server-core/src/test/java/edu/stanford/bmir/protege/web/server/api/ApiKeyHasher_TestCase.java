package edu.stanford.bmir.protege.web.server.api;

import com.google.common.io.BaseEncoding;
import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class ApiKeyHasher_TestCase {

    private String apiKeyString = "abcd";

    private ApiKey apiKey = ApiKey.valueOf(apiKeyString);

    private ApiKeyHasher hasher;

    @Before
    public void setUp() {
        hasher = new ApiKeyHasher();
    }

    @Test
    public void shouldHashApiKey() throws Exception {
        byte[] bytes = MessageDigest.getInstance("SHA-256")
                                    .digest(apiKeyString.getBytes("utf-8"));
        String hashedApiKeyString = BaseEncoding.base16()
                                                .lowerCase()
                                                .encode(bytes);
        HashedApiKey hashedApiKey = hasher.getHashedApiKey(apiKey);
        assertThat(hashedApiKey.get(), is(hashedApiKeyString));
    }
}
