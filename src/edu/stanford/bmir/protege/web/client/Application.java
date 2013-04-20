package edu.stanford.bmir.protege.web.client;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.place.PlaceManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.client.ui.ClientApplicationPropertiesCache;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The application on the client side.  This is a singleton instance.  Amongst other things, it manages the logged in
 * user and some of their details.
 *
 * @author Matthew Horridge
 *
 */
public class Application {

    private static final Application instance = new Application();

    /**
     * A flag which should only be set once all initialization is complete.  This is set below by the initialization
     * task manager.
     */
    private static boolean properlyInitialized = false;

    private LoggedInUserManager userManager;

    private final PlaceManager placeManager;

    private Optional<ProjectId> activeProject = Optional.absent();

    private Application() {
        placeManager = new PlaceManager();
    }

    // Package level access - should be called by the module.
    /**
     * Initializes the application.  This method should only be called ONCE.  Calling it more than once will cause
     * an {@link IllegalStateException} to be be thrown.
     * @param initCompleteCallback A callback that will be called when initialization is complete.  Not {@code null}.
     * @throws NullPointerException if {@code initCompleteCallback} is {@code null}.
     * @throws IllegalStateException if this method is called more than once.
     */
    protected static void init(AsyncCallback<Void> initCompleteCallback) {
        if(properlyInitialized) {
            throw new IllegalStateException("Application has already been initialized");
        }

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                // Consider logging and posting to server.
                GWT.log("Uncaught exception", e);
                if (e instanceof SerializationException) {
                    MessageBox.alert("WebProtege has been upgraded.  Please clear your browser caches and refresh your brow");
                }
            }
        });

        instance.runInitTasks(checkNotNull(initCompleteCallback));

    }

    /**
     * Gets the one and only instance of the {@link Application}.
     * @return  The one and only {@link Application}.  Not {@code null}.
     */
    public static Application get() {
        if(!properlyInitialized) {
            throw new IllegalStateException("Application has not be initialized");
        }
        return instance;
    }

    /**
     * Run the list of init tasks.  After successful completion the {@link Application#properlyInitialized} flag
     * will be set to {@code true}.
     * @param callback A call back which will be called either when initialization has finished or when initialization
     * has failed.  Not {@code null}.
     */
    private void runInitTasks(final AsyncCallback<Void> callback) {
        List<ApplicationInitManager.ApplicationInitializationTask> initTasks = new ArrayList<ApplicationInitManager.ApplicationInitializationTask>();
        initTasks.add(new InitializeClientApplicationPropertiesTask());
        initTasks.add(new RestoreUserFromServerSessionTask());
        ApplicationInitManager initManager = new ApplicationInitManager(initTasks);
        // Run the tasks and mark proper initalization on finish.
        initManager.runTasks(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(Void result) {
                properlyInitialized = true;
                callback.onSuccess(result);
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////
    ///// Public interface
    /////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the UserId of the logged in user.  This may be the guest user.
     * @return The UserId of the logged in user.  Not {@code null}.
     */
    public UserId getUserId() {
        return userManager.getLoggedInUserId();
    }

    /**
     * Determines if the current user is a guest user (i.e. not logged in).
     * @return {@code true} if the current user is the guest user, otherwise {@code false}.
     */
    public boolean isGuestUser() {
        return getUserId().isGuest();
    }

    /**
     * Gets the display name for the current user.
     * @return A String representing the display name for the current user.  Not {@code null}.
     */
    public String getUserDisplayName() {
        return userManager.getLoggedInUserDisplayName();
    }

    /**
     * Gets the email address of the logged in user. The email address may or may not be present.  If the current user
     * is the guest user then the email address will not be present.
     * @return An optional String.  Not {@code null}.  If present the String represents the email address of the logged
     * in user.  If absent, either the user does not have an email address set, or the user is the guest user.
     */
    public Optional<String> getLoggedInUserEmailAddress() {
        return userManager.getLoggedInUserEmailAddress();
    }

    /**
     * Gets the groups that the logged in user belongs to.
     * @return A set of {@link GroupId} objects that identify the groups that the logged in user belongs to.  Not {@code null}.
     * May be the empty set.
     */
    public Set<GroupId> getUserGroups() {
        return userManager.getLoggedInUserGroups();
    }


    /**
     * Logs out the current user if the user current user is not the guest user.
     */
    public void doLogOut() {
        userManager.logOutCurrentUser();
    }


    // It would be better if login were handled centrally

//    public void doLogIn() {
//        // We need to:
//        //    initialize the user details (email etc.)
//        //    initialize the permissions for open projects
//
//    }


    /**
     * Loads a project on the client side.
     * @param projectId The id of the project to be loaded.  Not {@code null}.
     * @param callback The call back that will be executed once the project has been loaded.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public void loadProject(final ProjectId projectId, final AsyncCallback<Project> callback) {
        checkNotNull(callback);
        ProjectManager.get().loadProject(projectId, callback);

//        OntologyServiceManager.getInstance().loadProject(projectId, new AsyncCallback<Integer>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                callback.onFailure(caught);
//            }
//
//            @Override
//            public void onSuccess(Integer result) {
//
//            }
//        });
    }

    public void closeProject(ProjectId projectId, AsyncCallback<ProjectId> callback) {

    }

    public PlaceManager getPlaceManager() {
        return placeManager;
    }

    /**
     * Gets the active project.
     * @return An optional active project.  Not {@code null}.  If no project is active then an absent value will
     * be returned.
     */
    public Optional<ProjectId> getActiveProject() {
        return activeProject;
    }

    /**
     * Sets the active project.
     * @param activeProject The active project.  Not {@code null}.
     * @throws NullPointerException if {@code activeProject} is {@code null}.
     */
    public void setActiveProject(Optional<ProjectId> activeProject) {
        if(this.activeProject.equals(checkNotNull(activeProject))) {
            return;
        }
        this.activeProject = activeProject;
//        placeManager.updateCurrentPlace();

        EventBusManager.getManager().postEvent(new ActiveProjectChangedEvent(activeProject));

    }

    /**
     * Sets the logged in user.  A check will be performed to ensure that the value is the same as the server side
     * session.  If the specified user is different to the current logged in user then either a {@link UserLoggedInEvent}
     * or {@link UserLoggedOutEvent} will be fired on the event bus.
     * @param userId The id of the user to set.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public void setCurrentUser(UserId userId) {
        userManager.setLoggedInUser(checkNotNull(userId));
    }


    /**
     * Gets the value of a session property for the currently logged in user.
     * @param propertyName The propertyName.  Not {@code null}.
     * @return The session value, which may or may not be present.  Not {@code null}.
     * @throws NullPointerException {@code propertyName} is {@code null}.
     */
    public Optional<String> getCurrentUserProperty(String propertyName) {
        return userManager.getSessionProperty(checkNotNull(propertyName));
    }

    /**
     * Sets a session property for the current user.  If the current user changes the session property will be
     * cleared.
     * @param propertyName The name of the property.  Not {@code null}.
     * @param value The value of the property.  Not {@code null}.
     * @throws NullPointerException if any parameter is {@code null}.
     * @throws IllegalArgumentException if propertyName is empty.
     * @see Application#clearCurrentUserProperty to remove a property value that has been set.
     */
    public void setCurrentUserProperty(String propertyName, String value) {
        if(checkNotNull(propertyName).isEmpty()) {
            throw new IllegalArgumentException("propertyName is empty");
        }
        userManager.setSessionProperty(propertyName, checkNotNull(value));
    }

    /**
     * Clears the value of a session property.  The value will automatically be cleared when the current user changes.
     * @param propertyName The name of the property to clear.  Not {@code null}.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    public void clearCurrentUserProperty(String propertyName) {
        userManager.clearSessionProperty(checkNotNull(propertyName));
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////
    ///////  Application initialization tasks.  Each task gets run in the specified order.  When one task is complete
    ///////  the next task is run.
    ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class InitializeClientApplicationPropertiesTask implements ApplicationInitManager.ApplicationInitializationTask {

        @Override
        public void run(final ApplicationInitManager.ApplicationInitTaskCallback taskFinishedCallback) {
            ClientApplicationPropertiesCache.initialize(new AsyncCallback<Map<String, String>>() {
                public void onFailure(Throwable caught) {
                    taskFinishedCallback.taskFailed(caught);
                }

                public void onSuccess(Map<String, String> result) {
                    taskFinishedCallback.taskComplete();
                }
            });
        }

        @Override
        public String getName() {
            return "InitializeClientApplicationProperties";
        }
    }

    private class RestoreUserFromServerSessionTask implements ApplicationInitManager.ApplicationInitializationTask {

        @Override
        public void run(final ApplicationInitManager.ApplicationInitTaskCallback taskFinishedCallback) {
            userManager = LoggedInUserManager.getAndRestoreFromServer(Optional.<AsyncCallback<UserDetails>>of(new AsyncCallback<UserDetails>() {
                @Override
                public void onFailure(Throwable caught) {
                    taskFinishedCallback.taskFailed(caught);
                }

                @Override
                public void onSuccess(UserDetails result) {
                    taskFinishedCallback.taskComplete();
                }
            }));
        }

        @Override
        public String getName() {
            return "RestoreUserFromServerSession";
        }
    }





}
