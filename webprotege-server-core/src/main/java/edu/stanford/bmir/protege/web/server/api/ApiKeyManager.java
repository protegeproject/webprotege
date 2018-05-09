package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyInfo;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2018
 */
@ApplicationSingleton
public class ApiKeyManager implements ApiKeyChecker {

    @Nonnull
    private final ApiKeyHasher encryptionAlgorithm;

    @Nonnull
    private final UserApiKeyStore apiKeyStore;

    @Inject
    public ApiKeyManager(@Nonnull ApiKeyHasher encryptionAlgorithm,
                         @Nonnull UserApiKeyStore apiKeyStore) {
        this.encryptionAlgorithm = checkNotNull(encryptionAlgorithm);
        this.apiKeyStore = checkNotNull(apiKeyStore);
    }

    /**
     * Gets the user id that the specified API key belongs to.
     *
     * @param apiKey The API key.
     * @return The {@link UserId} that the specified API key belongs to.  If the API key does not belong to a
     * user then the empty value is returned.
     */
    @Nonnull
    @Override
    public Optional<UserId> getUserIdForApiKey(@Nonnull ApiKey apiKey) {
        HashedApiKey hashedApiKey = encryptionAlgorithm.getHashedApiKey(apiKey);
        return apiKeyStore.getUserIdForApiKey(hashedApiKey);
    }

    @Nonnull
    public List<ApiKeyInfo> getApiKeysForUser(@Nonnull UserId userId) {
        return apiKeyStore.getApiKeys(userId).stream()
                          .map(r -> new ApiKeyInfo(r.getApiKeyId(), r.getCreatedAt(), r.getPurpose()))
                          .collect(toList());
    }

    @Nonnull
    public ApiKey generateApiKeyForUser(@Nonnull UserId userId,
                                        @Nonnull String purpose) {
        checkNotNull(userId);
        ApiKeyId apiKeyId = ApiKeyId.generateApiKeyId();
        ApiKey apiKey = ApiKey.generateApiKey();
        HashedApiKey hashedApiKey = encryptionAlgorithm.getHashedApiKey(apiKey);
        long createdAt = System.currentTimeMillis();
        ApiKeyRecord record = new ApiKeyRecord(apiKeyId,
                                               hashedApiKey,
                                               createdAt,
                                               checkNotNull(purpose));
        apiKeyStore.addApiKey(userId, record);
        return apiKey;
    }

    public void revokeApiKeys(@Nonnull UserId userId) {
        apiKeyStore.dropApiKeys(checkNotNull(userId));
    }

    public void revokeApiKey(@Nonnull UserId userId,
                             @Nonnull ApiKeyId apiKeyId) {
        apiKeyStore.dropApiKey(userId, apiKeyId);
    }
}
