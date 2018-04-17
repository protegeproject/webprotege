package edu.stanford.bmir.protege.web.server.api;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 *
 * Represents an API key that has been hashed
 */
public class HashedApiKey {

    private final String apiKey;

    /**
     * Represents a hashed api key
     * @param hashedApiKey The API Key
     */
    private HashedApiKey(@Nonnull String hashedApiKey) {
        this.apiKey = checkNotNull(hashedApiKey);
    }

    public String get() {
        return apiKey;
    }

    @Nonnull
    public static HashedApiKey valueOf(@Nonnull String encryptedApiKey) {
        return new HashedApiKey(encryptedApiKey);
    }

    @Override
    public int hashCode() {
        return apiKey.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HashedApiKey)) {
            return false;
        }
        HashedApiKey other = (HashedApiKey) obj;
        return this.apiKey.equals(other.apiKey);
    }


    @Override
    public String toString() {
        return toStringHelper("HashedApiKey")
                .addValue(apiKey)
                .toString();
    }
}
