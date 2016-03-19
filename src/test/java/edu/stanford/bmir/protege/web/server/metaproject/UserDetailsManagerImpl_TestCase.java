
package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManagerImpl;
import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class UserDetailsManagerImpl_TestCase {

    private UserDetailsManagerImpl userDetailsManagerImpl;

    private String userName = "THE USER NAME";

    private String email = "THE EMAIL";

    @Mock
    private UserRecordRepository userRecordRepository;

    private List<UserRecord> userRecords = new ArrayList<>();

    @Mock
    private UserRecord userRecord;

    @Mock
    private UserId userId;

    @Before
    public void setUp()
    {
        userRecords.clear();

        when(userId.getUserName()).thenReturn(userName);
        when(userRecord.getUserId()).thenReturn(userId);
        when(userRecord.getEmailAddress()).thenReturn(email);
        userRecords.add(userRecord);

        when(userRecordRepository.findAll()).thenReturn(userRecords.stream());
        when(userRecordRepository.findOneByEmailAddress(email)).thenReturn(java.util.Optional.of(userRecord));

        userDetailsManagerImpl = new UserDetailsManagerImpl(userRecordRepository);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Repository_IsNull() {
        new UserDetailsManagerImpl(userRecordRepository);
    }


    @Test
    public void should_getUserIds() {
        assertThat(userDetailsManagerImpl.getUserIds(), hasItem(UserId.getUserId(userName)));
    }

    @Test
    public void should_getEmail() {
        assertThat(userDetailsManagerImpl.getEmail(UserId.getUserId(userName)), is(Optional.of(email)));
    }

    @Test
    public void shouldGetUserIdByEmail() {
        EmailAddress emailAddress = new EmailAddress(email);
        assertThat(userDetailsManagerImpl.getUserIdByEmailAddress(emailAddress), is(Optional.of(UserId.getUserId(userName))));
    }

    @Test
    public void shouldNotGetUserIdByEmail() {
        EmailAddress emailAddress = new EmailAddress("Other Email");
        assertThat(userDetailsManagerImpl.getUserIdByEmailAddress(emailAddress), is(Optional.absent()));
    }
}
