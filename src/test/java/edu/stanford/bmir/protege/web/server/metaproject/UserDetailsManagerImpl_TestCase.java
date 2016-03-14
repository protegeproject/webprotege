
package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManagerImpl;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.lang.NullPointerException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class UserDetailsManagerImpl_TestCase {

    private UserDetailsManagerImpl userDetailsManagerImpl;

    @Mock
    private MetaProject metaProject;

    @Mock
    private MetaProjectStore metaProjectStore;

    @Mock
    private User user;

    private String userName = "THE USER NAME";

    private String email = "THE EMAIL";

    @Mock
    private UserRecordRepository userRecordRepository;

    @Before
    public void setUp()
    {
        userDetailsManagerImpl = new UserDetailsManagerImpl(userRecordRepository);
        when(user.getName()).thenReturn(userName);
        when(user.getEmail()).thenReturn(email);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Repository_IsNull() {
        new UserDetailsManagerImpl(userRecordRepository);
    }


    @Test
    public void should_getUserIds() {
        when(metaProject.getUsers()).thenReturn(Collections.singleton(user));
        assertThat(userDetailsManagerImpl.getUserIds(), hasItem(UserId.getUserId(userName)));
    }

    @Test
    public void should_getEmail() {
        when(metaProject.getUser(userName)).thenReturn(user);
        assertThat(userDetailsManagerImpl.getEmail(UserId.getUserId(userName)), is(Optional.of(email)));
    }

    @Test
    public void shouldGetUserIdByEmail() {
        when(metaProject.getUsers()).thenReturn(Collections.singleton(user));
        EmailAddress emailAddress = new EmailAddress(email);
        assertThat(userDetailsManagerImpl.getUserIdByEmailAddress(emailAddress), is(Optional.of(UserId.getUserId(userName))));
    }

    @Test
    public void shouldNotGetUserIdByEmail() {
        when(metaProject.getUsers()).thenReturn(Collections.singleton(user));
        EmailAddress emailAddress = new EmailAddress("Other Email");
        assertThat(userDetailsManagerImpl.getUserIdByEmailAddress(emailAddress), is(Optional.absent()));
    }
}
