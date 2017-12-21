package edu.stanford.bmir.protege.web.client.user;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.user.LoggedInUser.LoggedInUserState.LOGGED_IN_USER_CHANGED;
import static edu.stanford.bmir.protege.web.client.user.LoggedInUser.LoggedInUserState.LOGGED_IN_USER_UNCHANGED;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 21 Dec 2017
 *
 * Manages the state of the logged in user on the client and event firing for when the
 * logged in user changes.  No communication with the server is performed by instances
 * of this class.  See also {@link LoggedInUserManager}.
 */
public class LoggedInUser implements LoggedInUserProvider {

    enum LoggedInUserState {
        LOGGED_IN_USER_CHANGED,
        LOGGED_IN_USER_UNCHANGED
    }

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private UserId loggedInUser = UserId.getGuest();

    @Inject
    public LoggedInUser(@Nonnull EventBus eventBus) {
        this.eventBus = checkNotNull(eventBus);
    }

    /**
     * Gets the logged in user.  By default, this is the guest user.
     * @return The logged in user.
     */
    @Nonnull
    public UserId getCurrentUserId() {
        return loggedInUser;
    }

    /**
     * Sets the logged in user.  If the logged in user changes then the appropriate
     * events will be fired on the referenced event bus.
     * @param loggedInUser The logged in user
     * @return Whether the logged in user was changed.  If the logged in user was changed
     * then LOGGED_IN_USER_CHANGED will be returned, otherwise LOGGED_IN_USER_UNCHANGED
     * will be returned.
     */
    @Nonnull
    public LoggedInUserState setLoggedInUser(@Nonnull UserId loggedInUser) {
        if (this.loggedInUser.equals(checkNotNull(loggedInUser))) {
            return LOGGED_IN_USER_UNCHANGED;
        }
        this.loggedInUser = loggedInUser;
        if(loggedInUser.isGuest()) {
            fireUserLoggedOutEvent();
        }
        else {
            fireUserLoggedInEvent();
        }
        return LOGGED_IN_USER_CHANGED;
    }

    private void fireUserLoggedInEvent() {
        eventBus.fireEvent(new UserLoggedInEvent(loggedInUser).asGWTEvent());
    }

    private void fireUserLoggedOutEvent() {
        eventBus.fireEvent(new UserLoggedOutEvent(loggedInUser).asGWTEvent());
    }


}
