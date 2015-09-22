
package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
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

    @Before
    public void setUp()
        throws Exception
    {
        userDetailsManagerImpl = new UserDetailsManagerImpl(metaProject, metaProjectStore);
        when(user.getName()).thenReturn(userName);
        when(user.getEmail()).thenReturn(email);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_metaProject_IsNull() {
        new UserDetailsManagerImpl(null, metaProjectStore);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_metaProjectStore_IsNull() {
        new UserDetailsManagerImpl(metaProject, null);
    }


    @Test
    public void should_getUserIds() {
        when(metaProject.getUsers()).thenReturn(Collections.singleton(user));
        assertThat(userDetailsManagerImpl.getUserIds(), Matchers.hasItem(UserId.getUserId(userName)));
    }

    @Test
    public void should_getUserByUserId() {
        when(metaProject.getUser(userName)).thenReturn(user);
        assertThat(userDetailsManagerImpl.getUserByUserIdOrEmail(userName), is(Optional.of(user)));
    }

    @Test
    public void should_getUserDetailsByEmail() {
        when(metaProject.getUser(email)).thenReturn(user);
        assertThat(userDetailsManagerImpl.getUserByUserIdOrEmail(email), is(Optional.of(user)));
    }

    @Test
    public void should_returnAbsent() {
        assertThat(userDetailsManagerImpl.getUserByUserIdOrEmail(userName), is(Optional.<User>absent()));
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
