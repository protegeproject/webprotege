package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 *
 * Stores api keys for a given user.  Each key is stored in a hashed format.  A user may have multiple
 * api keys.  The following metadata is stored with each key that a user has 1) The key id, which allows
 * specific keys to be dropped; 2) A timestamp that records the time that the key was generated; 3)
 * A user specified string that records the purpose of the key.
 */
@ApplicationSingleton
public interface UserApiKeyStore extends Repository {

    /**
     * Gets the Api keys for a particular user
     * @param userId The user id.
     * @return A list of API key records for the specified user.
     */
    @Nonnull
    List<ApiKeyRecord> getApiKeys(@Nonnull UserId userId);

    /**
     * Adds the specified api key and related information for the specified user.
     * @param userId The userId
     * @param record The api key record.  If this list is empty then this has the effect of
     *                      revoking all api keys for the specified user.
     */
    void addApiKey(@Nonnull UserId userId, @Nonnull ApiKeyRecord record);

    /**
     * Drops all of the api keys for the specified user.
     * @param userId The {@link UserId} that identifies the user whose keys are to be dropped.
     */
    void dropApiKeys(@Nonnull UserId userId);

    /**
     * Drops the specified api key for the specified user.
     * @param userId The {@link UserId} that identifies the user that the key to be droped belongs to.
     * @param keyId The id of the key to be dropped.
     */
    void dropApiKey(@Nonnull UserId userId, @Nonnull ApiKeyId keyId);

    /**
     * Sets the api key records for the given user.
     * @param userId The {@link UserId} that specifies the user who the keys should be set for.
     * @param records The records.
     */
    void setApiKeys(@Nonnull UserId userId, List<ApiKeyRecord> records);

    /**
     * Gets the user that owns the specified api key.  This allows a user to be looked up from
     * an incoming api key.
     * @param apiKey The hashed api key.
     * @return The user or Optional.empty if there is no user that owns the specified API key.
     */
    @Nonnull
    Optional<UserId> getUserIdForApiKey(@Nonnull HashedApiKey apiKey);

}
