package edu.stanford.bmir.protege.web.server.api;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyInfo;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class ApiKeyManager_IT {

    private static final UserId USER_ID = UserId.getUserId("JaneDoe");

    private static final String PURPOSE = "Test key";

    private ApiKeyManager keyManager;

    private MongoClient mongoClient;

    private ApiKey generatedKey;

    private long timestamp;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = MongoTestUtils.createMorphia();
        mongoClient = MongoTestUtils.createMongoClient();
        Datastore datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        UserApiKeyStore store = new UserApiKeyStoreImpl(datastore);
        store.ensureIndexes();
        keyManager = new ApiKeyManager(new ApiKeyHasher(), store);
        timestamp = System.currentTimeMillis();
        generatedKey = generateApiKey();

    }

    private ApiKey generateApiKey() {
        return keyManager.generateApiKeyForUser(USER_ID, PURPOSE);
    }

    @Test
    public void shouldGenerateApiKey() {
        assertThat(generatedKey, is(not(nullValue())));
    }

    @Test
    public void shouldRevokeApiKeys() {
        keyManager.revokeApiKeys(USER_ID);
        assertThat(keyManager.getApiKeysForUser(USER_ID), is(empty()));
    }

    @Test
    public void shouldFindUserByGeneratedKey() {
        Optional<UserId> userId = keyManager.getUserIdForApiKey(generatedKey);
        assertThat(userId, is(Optional.of(USER_ID)));
    }

    @Test
    public void shouldFindKeyInfo() {
        long currentTime = System.currentTimeMillis();
        List<ApiKeyInfo> keyInfo = keyManager.getApiKeysForUser(USER_ID);
        assertThat(keyInfo.size(), is(1));
        ApiKeyInfo info = keyInfo.get(0);
        assertThat(info.getCreatedAt(), is(lessThanOrEqualTo(currentTime)));
        assertThat(info.getCreatedAt(), is(greaterThanOrEqualTo(timestamp)));
        assertThat(info.getPurpose(), is(PURPOSE));
    }

    @After
    public void tearDown() {
        mongoClient.dropDatabase(MongoTestUtils.getTestDbName());
        mongoClient.close();
    }
}
