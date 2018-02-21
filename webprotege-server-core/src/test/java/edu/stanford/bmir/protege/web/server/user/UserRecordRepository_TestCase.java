package edu.stanford.bmir.protege.web.server.user;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRecordRepository_TestCase {

    private static final String EMAIL_ADDRESS = "jane.doe@somewhere.com";

    private UserRecordRepository recordRepository;

    private MongoClient client;

    private MongoDatabase database;

    private UserRecord userRecord;

    private UserId userId;

    @Before
    public void setUp() throws Exception {
        client = new MongoClient();
        database = client.getDatabase(MongoTestUtils.getTestDbName());
        recordRepository = new UserRecordRepository(database, new UserRecordConverter());
        userId = UserId.getUserId("JaneDoe");
        userRecord = new UserRecord(
                userId,
                "Jane Doe" ,
                EMAIL_ADDRESS,
                "" ,
                new Salt("somebytes".getBytes()),
                new SaltedPasswordDigest("someotherbytes".getBytes())
        );
    }

    @After
    public void tearDown() {
        database.drop();
        client.close();
    }

    @Test
    public void shouldSaveUserRecord() {
        recordRepository.save(userRecord);
    }

    @Test
    public void shouldFindUserRecord() {
        recordRepository.save(userRecord);
        Optional<UserRecord> record = recordRepository.findOne(userId);
        assertThat(record.isPresent(), is(true));
        assertThat(record.get(), is(userRecord));
    }

    @Test
    public void shouldReplaceUserRecord() {
        recordRepository.save(userRecord);
        recordRepository.save(userRecord);
    }

    @Test
    public void shouldDeleteUserRecord() {
        recordRepository.save(userRecord);
        assertThat(recordRepository.findOne(userId).isPresent(), is(true));
        recordRepository.delete(userId);
        assertThat(recordRepository.findOne(userId).isPresent(), is(false));
    }

    @Test
    public void shouldFindByContaining() {
        recordRepository.save(userRecord);
        List<UserId> users = recordRepository.findByUserIdContainingIgnoreCase("doe", 1);
        assertThat(users, hasItem(userId));
    }

    @Test
    public void shouldFindByEmailAddress() {
        recordRepository.save(userRecord);
        Optional<UserRecord> record = recordRepository.findOneByEmailAddress(EMAIL_ADDRESS);
        assertThat(record.isPresent(), is(true));
    }
}
