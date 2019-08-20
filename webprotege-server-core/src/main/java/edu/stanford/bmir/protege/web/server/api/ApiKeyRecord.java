package edu.stanford.bmir.protege.web.server.api;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class ApiKeyRecord {

    public static final String API_KEY_ID = "apiKeyId";

    private ApiKeyId apiKeyId;

    private HashedApiKey apiKey;

    // Note that this is a Date object because it serializes in a human readable way in MongoDB
    private Date createdAt;

    private String purpose;


    private ApiKeyRecord() {
    }

    /**
     * Creates an API Key Record.  The record holds a hashed API Key, the time that it was
     * created and the purpose of the API key.
     * @param apiKeyId The id for this API key record.
     * @param apiKey The hashed API Key.
     */
    public ApiKeyRecord(@Nonnull ApiKeyId apiKeyId,
                        @Nonnull HashedApiKey apiKey,
                        long createdAt,
                        @Nonnull String purpose) {
        this.apiKeyId = checkNotNull(apiKeyId);
        this.apiKey = checkNotNull(apiKey);
        this.createdAt = new Date(createdAt);
        this.purpose = checkNotNull(purpose);
    }

    @Nonnull
    public ApiKeyId getApiKeyId() {
        return apiKeyId;
    }

    @Nonnull
    public HashedApiKey getApiKey() {
        return apiKey;
    }

    public long getCreatedAt() {
        return createdAt.getTime();
    }

    @Nonnull
    public String getPurpose() {
        return purpose;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(apiKeyId, apiKey, createdAt, purpose);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApiKeyRecord)) {
            return false;
        }
        ApiKeyRecord other = (ApiKeyRecord) obj;
        return this.apiKeyId.equals(other.apiKeyId)
                && this.apiKey.equals(other.apiKey)
                && this.createdAt.getTime() == other.createdAt.getTime()
                && this.purpose.equals(other.purpose);
    }


    @Override
    public String toString() {
        return toStringHelper("ApiKeyRecord")
                .addValue(apiKey)
                .addValue(apiKeyId)
                .add("createdAt", createdAt)
                .add("purpose", purpose)
                .toString();
    }
}
