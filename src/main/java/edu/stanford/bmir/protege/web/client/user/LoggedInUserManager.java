package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ClientObjectReader;
import edu.stanford.bmir.protege.web.client.app.UserInSessionDecoder;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoggedInUserManager implements LoggedInUserProvider {

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private final Set<ActionId> applicationActions = new HashSet<>();

    private UserId userId = UserId.getGuest();

    private UserDetails userDetails = UserDetails.getGuestUserDetails();

    @Inject
    public LoggedInUserManager(@Nonnull EventBus eventBus,
                               @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.eventBus = checkNotNull(eventBus);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    /**
     * Creates a {@link LoggedInUserManager} with the guest user as the current user.  A call will be made to the server
     * to asynchronously restore the current user from the server side session.
     * @return A {@link LoggedInUserManager} instances which is initialised immediately with the guest user details and
     * initialised asynchronously with the server side session details.
     */
    @Nonnull
    public static LoggedInUserManager getAndRestoreFromServer(EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        LoggedInUserManager manager = new LoggedInUserManager(eventBus, dispatchServiceManager);
        manager.readUserInSession();
        return manager;
    }


    /**
     * Gets the id of the currently logged in user.
     * @return The id of the currently logged in user.  Not {@code null}.  The returned id may correspond to the id
     * of the guest user.
     */
    @Nonnull
    public UserId getLoggedInUserId() {
        return userId;
    }

    @Nonnull
    @Override
    public UserId getCurrentUserId() {
        return userId;
    }

    /**
     * Sets the logged in user.  An event will be fired (asynchronously) to indicate whether the user has logged in or out.
     * @param userId The user id of the logged in user.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public void setLoggedInUser(@Nonnull final UserId userId, @Nonnull AsyncCallback<UserDetails> callback) {
        if(checkNotNull(userId).equals(this.userId)) {
            callback.onSuccess(userDetails);
            return;
        }
        restoreUserFromServerSideSession(Optional.of(callback));
    }

    /**
     * If the current user is not the guest user calling this method will log out the current user.  An event will
     * be fired (asynchronously) when the current user has been logged out.
     */
    public void logOutCurrentUser() {
        if(userId.isGuest()) {
            return;
        }
        dispatchServiceManager.execute(new LogOutUserAction(), logOutUserResult -> {
            replaceUserAndBroadcastChanges(new UserInSession(
                    UserDetails.getGuestUserDetails(),
                    Collections.emptySet()
            ));
        });
    }

    public Set<ActionId> getLoggedInUserApplicationActions() {
        return applicationActions;
    }

    public boolean isAllowedApplicationAction(ActionId actionId) {
        return applicationActions.contains(actionId);
    }

    public boolean isAllowedApplicationAction(BuiltInAction action) {
        return isAllowedApplicationAction(action.getActionId());
    }

    private void restoreUserFromServerSideSession(final Optional<AsyncCallback<UserDetails>> callback) {
        dispatchServiceManager.execute(new GetCurrentUserInSessionAction(), new DispatchServiceCallback<GetCurrentUserInSessionResult>() {
            @Override
            public void handleExecutionException(Throwable cause) {
                if (callback.isPresent()) {
                    callback.get().onFailure(cause);
                }
            }

            @Override
            public void handleSuccess(GetCurrentUserInSessionResult result) {
                replaceUserAndBroadcastChanges(result.getUserInSession());
                if (callback.isPresent()) {
                    callback.get().onSuccess(result.getUserInSession().getUserDetails());
                }
            }

        });
    }

    private void readUserInSession() {
        UserInSessionDecoder decoder = new UserInSessionDecoder();
        UserInSession userInSession  = ClientObjectReader.create("userInSession", decoder).read();
        replaceUserAndBroadcastChanges(userInSession);
    }

    private void replaceUserAndBroadcastChanges(UserInSession userInSession) {
        UserId previousUserId = this.userId;
        this.userId = userInSession.getUserDetails().getUserId();
        this.userDetails = userInSession.getUserDetails();
        this.applicationActions.clear();
        this.applicationActions.addAll(userInSession.getAllowedApplicationActions());
        if(userId.isGuest()) {
            eventBus.fireEvent(new UserLoggedOutEvent(previousUserId).asGWTEvent());
        }
        else {
            eventBus.fireEvent(new UserLoggedInEvent(userId).asGWTEvent());
        }
    }

}
