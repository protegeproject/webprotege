package edu.stanford.bmir.protege.web.server.api;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class UserApiKeyStoreImpl_IT {

    private static final String TESTING_PURPOSE = "Testing purpose";

    private static final long CREATED_AT = System.currentTimeMillis();

    private static final HashedApiKey HASHED_API_KEY = HashedApiKey.valueOf("xyz");

    private static final ApiKeyId API_KEY_ID = ApiKeyId.valueOf("abcd");

    private static final UserId USER_ID = UserId.getUserId("JaneDoe");

    private UserApiKeyStoreImpl store;

    private MongoClient mongoClient;

    private ApiKeyRecord record;

    @Before
    public void setUp() throws Exception {
        Morphia morphia = MongoTestUtils.createMorphia();
        mongoClient = MongoTestUtils.createMongoClient();
        Datastore datastore = morphia.createDatastore(mongoClient, MongoTestUtils.getTestDbName());
        store = new UserApiKeyStoreImpl(datastore);
        store.ensureIndexes();

        record = new ApiKeyRecord(API_KEY_ID,
                                  HASHED_API_KEY,
                                  CREATED_AT,
                                  TESTING_PURPOSE);
        store.addApiKey(USER_ID, record);
    }

    @Test
    public void shouldStoreApiKeyForUser() {
        List<ApiKeyRecord> records = store.getApiKeys(USER_ID);
        assertThat(records, hasItem(record));
    }

    @Test
    public void shouldFindUserForApiKey() {
        Optional<UserId> userId = store.getUserIdForApiKey(HASHED_API_KEY);
        assertThat(userId, is(Optional.of(USER_ID)));
    }

    @Test
    public void shouldNotFindUserForApiKey() {
        Optional<UserId> userId = store.getUserIdForApiKey(mock(HashedApiKey.class));
        assertThat(userId, is(Optional.empty()));
    }

    @Test
    public void shouldAddFreshApiKey() {
        ApiKeyRecord otherRecord = new ApiKeyRecord(ApiKeyId.valueOf("other"),
                                  HASHED_API_KEY,
                                  CREATED_AT,
                                  TESTING_PURPOSE);
        store.addApiKey(USER_ID, otherRecord);
        List<ApiKeyRecord> records = store.getApiKeys(USER_ID);
        assertThat(records.size(), is(2));
        assertThat(records, hasItems(record, otherRecord));
    }

    @Test
    public void shouldNotAddDuplicateApiKey() {
        store.addApiKey(USER_ID, record);
        assertThat(store.getApiKeys(USER_ID), hasItem(record));
    }

    @Test
    public void shouldOverwriteKeyWithExistingId() {
        ApiKeyRecord otherRecord = new ApiKeyRecord(API_KEY_ID,
                                                    HASHED_API_KEY,
                                                    CREATED_AT,
                                                    "Other purpose");
        store.addApiKey(USER_ID, otherRecord);
        List<ApiKeyRecord> records = store.getApiKeys(USER_ID);
        assertThat(records.size(), is(1));
        assertThat(records, hasItems(otherRecord));
    }

    @Test
    public void shouldDropApiKey() {
        store.dropApiKey(USER_ID, API_KEY_ID);
        List<ApiKeyRecord> records = store.getApiKeys(USER_ID);
        assertThat(records.size(), is(0));
    }

    @Test
    public void shouldSetApiKeys() {
        ApiKeyRecord otherRecord = new ApiKeyRecord(ApiKeyId.valueOf("OtherKeyId"),
                                                    HASHED_API_KEY,
                                                    CREATED_AT,
                                                    "Other purpose");
        store.setApiKeys(USER_ID, Collections.singletonList(otherRecord));
        List<ApiKeyRecord> records = store.getApiKeys(USER_ID);
        assertThat(records.size(), is(1));
        assertThat(records, hasItems(otherRecord));
    }

    @Test
    public void shouldNotSetDuplicateApiKeys() {
        ApiKeyRecord otherRecord = new ApiKeyRecord(ApiKeyId.valueOf("OtherKeyId"),
                                                    HASHED_API_KEY,
                                                    CREATED_AT,
                                                    "Other purpose");
        store.setApiKeys(USER_ID, Arrays.asList(otherRecord, otherRecord));
        List<ApiKeyRecord> records = store.getApiKeys(USER_ID);
        assertThat(records.size(), is(1));
        assertThat(records, hasItems(otherRecord));
    }


    @After
    public void tearDown() {
        mongoClient.dropDatabase(MongoTestUtils.getTestDbName());
        mongoClient.close();
    }

}
