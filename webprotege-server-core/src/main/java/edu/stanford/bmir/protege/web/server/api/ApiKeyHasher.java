package edu.stanford.bmir.protege.web.server.api;

import com.google.common.hash.Hashing;
import edu.stanford.bmir.protege.web.shared.api.ApiKey;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class ApiKeyHasher {

    @Inject
    public ApiKeyHasher() {
    }

    /**
     * Gets an encrypted version of the specified API key.
     *
     * @param apiKey The API Key.
     * @return The encrypted version of the specified API Key.
     */
    @Nonnull
    public HashedApiKey getHashedApiKey(@Nonnull ApiKey apiKey) {
        // Since an API Key is a secure random UUID we don't really
        // need to salt it
        String base16Encoding = Hashing.sha256()
                                       .hashString(apiKey.getKey(), StandardCharsets.UTF_8)
                                       .toString();
        return HashedApiKey.valueOf(base16Encoding);
    }
}
