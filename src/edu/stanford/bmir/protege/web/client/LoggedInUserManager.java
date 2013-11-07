package edu.stanford.bmir.protege.web.client;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoggedInUserManager {

    private UserId userId = UserId.getGuest();

    private UserDetails userDetails = UserDetails.getGuestUserDetails();

    private Set<GroupId> groups = new HashSet<GroupId>();

    private Map<String, String> currentUserProperties = new HashMap<String, String>();


    private LoggedInUserManager() {
    }

    /**
     * Creates a {@link LoggedInUserManager} with the guest user as the current user.  A call will be made to the server
     * to asynchronously restore the current user from the server side session.
     * @param initCompleteCallback An optional call back that will be invoked (if present) when the user has been
     * restored from the server side session.
     * @return A {@link LoggedInUserManager} instances which is initialised immediately with the guest user details and
     * initialised asynchronously with the server side session details.
     */
    public static LoggedInUserManager getAndRestoreFromServer(Optional<AsyncCallback<UserDetails>> initCompleteCallback) {
        LoggedInUserManager manager = new LoggedInUserManager();
        manager.restoreUserFromServerSideSession(initCompleteCallback);
        return manager;
    }


    /**
     * Gets the id of the currently logged in user.
     * @return The id of the currently logged in user.  Not {@code null}.  The returned id may correspond to the id
     * of the guest user.
     */
    public UserId getLoggedInUserId() {
        return userId;
    }

    /**
     * Sets the logged in user.  An event will be fired (asynchronously) to indicate whether the user has logged in or out.
     * @param userId The user id of the logged in user.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public void setLoggedInUser(final UserId userId) {
        checkNotNull(userId);
        if(userId.equals(this.userId)) {
            return;
        }
        restoreUserFromServerSideSession(Optional.<AsyncCallback<UserDetails>>absent());
    }

    /**
     * If the current user is not the guest user calling this method will log out the current user.  An event will
     * be fired (asynchronously) when the current user has been logged out.
     */
    public void logOutCurrentUser() {
        if(userId.isGuest()) {
            return;
        }
        AdminServiceManager.getInstance().logout(new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert(AuthenticationConstants.ASYNCHRONOUS_CALL_FAILURE_MESSAGE);
            }

            public void onSuccess(Void result) {
                replaceUserAndBroadcastChanges(UserDetails.getGuestUserDetails(), Collections.<GroupId>emptySet());
            }
        });
    }

    private void restoreUserFromServerSideSession(final Optional<AsyncCallback<UserDetails>> callback) {
        DispatchServiceManager.get().execute(new GetCurrentUserInSessionAction(), new AsyncCallback<GetCurrentUserInSessionResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Problem getting user details for user " + userId, caught);
                if(callback.isPresent()) {
                    callback.get().onFailure(caught);
                }
            }

            @Override
            public void onSuccess(GetCurrentUserInSessionResult result) {
                replaceUserAndBroadcastChanges(result.getUserDetails(), result.getUserGroupIds());
                if(callback.isPresent()) {
                    callback.get().onSuccess(result.getUserDetails());
                }
            }

        });
    }

    public String getLoggedInUserDisplayName() {
        return userDetails.getDisplayName();
    }

    public Optional<String> getLoggedInUserEmailAddress() {
        return userDetails.getEmailAddress();
    }

    public Set<GroupId> getLoggedInUserGroups() {
        return new HashSet<GroupId>(groups);
    }


    // TODO:  A temp hack - I'd like to make this type safe or get rid of it.



    public Optional<String> getSessionProperty(String prop) {
        String value = currentUserProperties.get(prop);
        if(value == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(value);
        }
    }

    public void setSessionProperty(String prop, String value) {
        currentUserProperties.put(checkNotNull(prop), checkNotNull(value));
    }

    public void clearSessionProperty(String propertyName) {
        currentUserProperties.remove(checkNotNull(propertyName));
    }



    private void replaceUserAndBroadcastChanges(UserDetails newUserDetails, Set<GroupId> newUserGroups) {
        groups.clear();
        currentUserProperties.clear();

        UserId previousUserId = this.userId;
        this.userId = newUserDetails.getUserId();
        this.userDetails = newUserDetails;
        this.groups.addAll(newUserGroups);
        if(userId.isGuest()) {
            EventBusManager.getManager().postEvent(new UserLoggedOutEvent(previousUserId));
        }
        else {
            EventBusManager.getManager().postEvent(new UserLoggedInEvent(userId));
        }
    }

}
