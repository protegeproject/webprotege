package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.user.LoggedInUser.LoggedInUserState;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent.ON_USER_LOGGED_IN;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent.ON_USER_LOGGED_OUT;
import static edu.stanford.bmir.protege.web.client.user.LoggedInUser.LoggedInUserState.LOGGED_IN_USER_UNCHANGED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 21 Dec 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggedInUser_TestCase {

    private LoggedInUser loggedInUser;

    private UserId otherUserId = UserId.getUserId("OtherUser");

    @Mock
    private UserLoggedInHandler userLoggedInHandler;

    @Mock
    private UserLoggedOutHandler userLoggedOutHandler;

    @Mock
    private UserDetails otherUserDetails;

    @Mock
    private UserInSession otherUserInSession;

    @Mock
    private UserInSession guestUserInSession;

    @Before
    public void setUp() {
        when(otherUserDetails.getUserId()).thenReturn(otherUserId);
        when(otherUserInSession.getUserDetails()).thenReturn(otherUserDetails);
        when(guestUserInSession.getUserDetails()).thenReturn(UserDetails.getGuestUserDetails());
        EventBus eventBus = new SimpleEventBus();
        loggedInUser = new LoggedInUser(eventBus);
        eventBus.addHandler(ON_USER_LOGGED_IN, userLoggedInHandler);
        eventBus.addHandler(ON_USER_LOGGED_OUT, userLoggedOutHandler);
    }

    @Test
    public void shouldBeGuestUserByDefault() {
        assertThat(loggedInUser.getCurrentUserId().isGuest(), is(true));
    }

    @Test
    public void shouldNotFireUserLoggedOutEventForGuestUserToGuestUser() {
        loggedInUser.setLoggedInUser(guestUserInSession);
        reset(userLoggedOutHandler);
        loggedInUser.setLoggedInUser(guestUserInSession);
        verify(userLoggedOutHandler, never()).handleUserLoggedOut(any());
    }

    @Test
    public void shouldNotFireUserLoggedInEventForGuestUserToGuestUser() {
        loggedInUser.setLoggedInUser(guestUserInSession);
        reset(userLoggedInHandler);
        loggedInUser.setLoggedInUser(guestUserInSession);
        verify(userLoggedInHandler, never()).handleUserLoggedIn(any());
    }

    @Test
    public void shouldNotChangeStateForGuestUserToGuestUser() {
        loggedInUser.setLoggedInUser(guestUserInSession);
        LoggedInUserState state = loggedInUser.setLoggedInUser(guestUserInSession);
        assertThat(state, is(LOGGED_IN_USER_UNCHANGED));
    }

    @Test
    public void shouldFireUserLoggedInEventForGuestUserToOtherUser() {
        loggedInUser.setLoggedInUser(otherUserInSession);
        verify(userLoggedInHandler, times(1)).handleUserLoggedIn(any());
    }

    @Test
    public void shouldNotFireUserLoggedOutEventForGuestUserToOtherUser() {
        loggedInUser.setLoggedInUser(otherUserInSession);
        verify(userLoggedOutHandler, never()).handleUserLoggedOut(any());
    }

    @Test
    public void shouldChangeStateForGuestUserToOtherUser() {
        LoggedInUserState state = loggedInUser.setLoggedInUser(otherUserInSession);
        assertThat(state, is(LoggedInUserState.LOGGED_IN_USER_CHANGED));
    }

    @Test
    public void shouldFireUserLoggedOutForOtherUserToGuestUser() {
        loggedInUser.setLoggedInUser(otherUserInSession);
        reset(userLoggedOutHandler);
        loggedInUser.setLoggedInUser(guestUserInSession);
        verify(userLoggedOutHandler, times(1)).handleUserLoggedOut(any());
    }
}
