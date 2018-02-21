package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRecordConverter_TestCase {

    private UserRecordConverter converter;

    private UserRecord record;

    @Before
    public void setUp() {
        converter = new UserRecordConverter();
        record = new UserRecord(
                UserId.getUserId("TheUserId"),
                "The Real Name",
                "user@email.com",
                "",
                new Salt(new byte[]{1, 2, 3, 4}),
                new SaltedPasswordDigest(new byte[]{5, 6, 7, 8})
        );
    }

    @Test
    public void shouldRoundTripUserRecord() {
        Document document = converter.toDocument(record);
        UserRecord roundTripped = converter.fromDocument(document);
        assertThat(roundTripped, Matchers.is(record));
    }
}
