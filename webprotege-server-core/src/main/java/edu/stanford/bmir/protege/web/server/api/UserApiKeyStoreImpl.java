package edu.stanford.bmir.protege.web.server.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.api.ApiKeyRecord.API_KEY_ID;
import static edu.stanford.bmir.protege.web.server.api.UserApiKeys.*;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class UserApiKeyStoreImpl implements UserApiKeyStore {

    private static final int INCLUDE = 1;

    @Nonnull
    private final Datastore datastore;

    @Inject
    public UserApiKeyStoreImpl(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    private static DBObject object(String key, Object val) {
        return new BasicDBObject(key, val);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(UserApiKeys.class);
    }

    @Override
    public void addApiKey(@Nonnull UserId userId, @Nonnull ApiKeyRecord record) {
        UserApiKeys existingKeys = datastore.createQuery(UserApiKeys.class)
                                            .field(USER_ID).equal(userId)
                                            .get();
        if (existingKeys == null) {
            datastore.save(new UserApiKeys(userId, singletonList(record)));
        }
        else {
            Optional<ApiKeyRecord> existingKeyRecord = existingKeys.getApiKeys().stream()
                                                                   .filter(r -> r.getApiKeyId().equals(record.getApiKeyId()))
                                                                   .findFirst();
            existingKeyRecord.ifPresent(existing -> dropApiKey(userId, existing.getApiKeyId()));
            UpdateOperations<UserApiKeys> updateOps = datastore.createUpdateOperations(UserApiKeys.class)
                                                               .addToSet(API_KEYS, record);
            datastore.update(existingKeys, updateOps);
        }
    }

    @Override
    public void dropApiKeys(@Nonnull UserId userId) {
        datastore.delete(UserApiKeys.class, userId);
    }

    @Override
    public void dropApiKey(@Nonnull UserId userId,
                           @Nonnull ApiKeyId apiKeyId) {
        DBCollection collection = datastore.getCollection(UserApiKeys.class);
        DBObject ops = object("$pull",
                              // From
                              object(API_KEYS,
                                     // where
                                     object(API_KEY_ID, apiKeyId.getId())));
        // Match the UserId
        DBObject query = object(USER_ID, userId.getUserName());
        collection.update(query, ops);
    }

    @Override
    public void setApiKeys(@Nonnull UserId userId, List<ApiKeyRecord> records) {
        Set<ApiKeyId> ids = new HashSet<>();
        List<ApiKeyRecord> nonDuplicates = records.stream()
                                                  .filter(r -> ids.add(r.getApiKeyId()))
                                                  .collect(Collectors.toList());
        UpdateOperations<UserApiKeys> ops = datastore.createUpdateOperations(UserApiKeys.class)
                                                     .set(API_KEYS, nonDuplicates);
        Query<UserApiKeys> query = datastore.createQuery(UserApiKeys.class)
                                            .field(USER_ID).equal(userId);
        datastore.update(query, ops, new UpdateOptions().upsert(true));
    }

    @Nonnull
    @Override
    public List<ApiKeyRecord> getApiKeys(@Nonnull UserId userId) {
        UserApiKeys keys = datastore.createQuery(UserApiKeys.class)
                                    .field(USER_ID).equal(userId)
                                    .get();
        if (keys == null) {
            return Collections.emptyList();
        }
        return keys.getApiKeys();
    }

    @Nonnull
    @Override
    public Optional<UserId> getUserIdForApiKey(@Nonnull HashedApiKey apiKey) {
        DBCollection collection = datastore.getCollection(UserApiKeys.class);
        DBObject query = object(API_KEYS__API_KEY, apiKey.get());
        DBObject projection = object(USER_ID, INCLUDE);
        DBObject found = collection.findOne(query, projection);
        return Optional.ofNullable(found)
                       .map(object -> object.get(USER_ID).toString())
                       .map(UserId::getUserId);
    }
}
