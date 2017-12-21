package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.user.LoggedInUser.LoggedInUserState;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    private UserId otherUser = UserId.getUserId("OtherUser");

    @Mock
    private UserLoggedInHandler userLoggedInHandler;

    @Mock
    private UserLoggedOutHandler userLoggedOutHandler;

    @Before
    public void setUp() {
        EventBus eventBus = new SimpleEventBus();
        loggedInUser = new LoggedInUser(eventBus);
        eventBus.addHandler(UserLoggedInEvent.ON_USER_LOGGED_IN, userLoggedInHandler);
        eventBus.addHandler(UserLoggedOutEvent.ON_USER_LOGGED_OUT, userLoggedOutHandler);
    }

    @Test
    public void shouldBeGuestUserByDefault() {
        assertThat(loggedInUser.getCurrentUserId().isGuest(), is(true));
    }

    @Test
    public void shouldNotFireUserLoggedOutEventForGuestUserToGuestUser() {
        loggedInUser.setLoggedInUser(UserId.getGuest());
        verify(userLoggedOutHandler, never()).handleUserLoggedOut(any());
    }

    @Test
    public void shouldNotFireUserLoggedInEventForGuestUserToGuestUser() {
        loggedInUser.setLoggedInUser(UserId.getGuest());
        verify(userLoggedInHandler, never()).handleUserLoggedIn(any());
    }

    @Test
    public void shouldNotChangeStateForGuestUserToGuestUser() {
        LoggedInUserState state = loggedInUser.setLoggedInUser(UserId.getGuest());
        assertThat(state, is(LOGGED_IN_USER_UNCHANGED));
    }

    @Test
    public void shouldFireUserLoggedInEventForGuestUserToOtherUser() {
        loggedInUser.setLoggedInUser(otherUser);
        verify(userLoggedInHandler, times(1)).handleUserLoggedIn(any());
    }

    @Test
    public void shouldNotFireUserLoggedOutEventForGuestUserToOtherUser() {
        loggedInUser.setLoggedInUser(otherUser);
        verify(userLoggedOutHandler, never()).handleUserLoggedOut(any());
    }

    @Test
    public void shouldChangeStateForGuestUserToOtherUser() {
        LoggedInUserState state = loggedInUser.setLoggedInUser(otherUser);
        assertThat(state, is(LoggedInUserState.LOGGED_IN_USER_CHANGED));
    }

    @Test
    public void shouldFireUserLoggedOutForOtherUserToGuestUser() {
        loggedInUser.setLoggedInUser(otherUser);
        reset(userLoggedOutHandler);
        loggedInUser.setLoggedInUser(UserId.getGuest());
        verify(userLoggedOutHandler, times(1)).handleUserLoggedOut(any());
    }
}
