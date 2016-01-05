package edu.stanford.bmir.protege.web.client;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.app.ClientApplicationPropertiesDecoder;
import edu.stanford.bmir.protege.web.client.app.ClientObjectReader;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitManagerInitializationTask;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The application on the client side.  This is a singleton instance.  Amongst other things, it manages the logged in
 * user and some of their details.
 *
 * @author Matthew Horridge
 *
 */
public class WebProtegeInitializer {

    /**
     * A flag which should only be set once all initialization is complete.  This is set below by the initialization
     * task manager.
     */
    private static boolean properlyInitialized = false;

    private LoggedInUserManager userManager;

    private ClientApplicationProperties clientApplicationProperties;

    private final DispatchServiceManager dispatchServiceManager;

    private final EventBus eventBus;

    @Inject
    private WebProtegeInitializer(EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
    }


    /**
     * Initializes the application.  This method should only be called ONCE.  Calling it more than once will cause
     * an {@link IllegalStateException} to be be thrown.
     * @param initCompleteCallback A callback that will be called when initialization is complete.  Not {@code null}.
     * @throws NullPointerException if {@code initCompleteCallback} is {@code null}.
     * @throws IllegalStateException if this method is called more than once.
     */
    public void init(AsyncCallback<Void> initCompleteCallback) {
        if(properlyInitialized) {
            throw new IllegalStateException("Application has already been initialized");
        }

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                // Consider logging and posting to server.
                GWT.log("Uncaught exception", e);
                if (e instanceof SerializationException) {
                    MessageBox.alert("WebProtege has been upgraded.  Please clear your browser caches and refresh your browser.");
                }
            }
        });
    }

//    /**
//     * Gets the one and only instance of the {@link Application}.
//     * @return  The one and only {@link Application}.  Not {@code null}.
//     */
//    public static Application get() {
//        if(!properlyInitialized) {
//            throw new IllegalStateException("Application has not be initialized");
//        }
//        return instance;
//    }

    /**
     * Run the list of init tasks.  After successful completion the {@link WebProtegeInitializer#properlyInitialized} flag
     * will be set to {@code true}.
     * @param callback A call back which will be called either when initialization has finished or when initialization
     * has failed.  Not {@code null}.
     */
    private void runInitTasks(final AsyncCallback<Void> callback) {
        List<ApplicationInitManager.ApplicationInitializationTask> initTasks = new ArrayList<ApplicationInitManager.ApplicationInitializationTask>();
        initTasks.add(new InitializeClientApplicationPropertiesTask());
        initTasks.add(new RestoreUserFromServerSessionTask());
        initTasks.add(new EntityCrudKitManagerInitializationTask(dispatchServiceManager));
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



//    /**
//     * Determines whether the logged in user is the owner of the active project).
//     * @return {@code false} if there is no project that is the active project.  {@code false} if the logged in user
//     * is the guest user.  {@code true} if and only if the logged in user is equal to the active project owner.
//     */
//    public boolean isLoggedInUserOwnerOfActiveProject() {
//        if(!activeProject.isPresent()) {
//            return false;
//        }
//        ProjectId projectId = activeProject.get();
//        Optional<Project> project = projectManager.getProject(projectId);
//        if(!project.isPresent()) {
//            return false;
//        }
//        ProjectDetails projectDetails = project.get().getProjectDetails();
//        return projectDetails.getOwner().equals(getUserId());
//    }

    /**
     * Gets a client application property. Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The name of the property.  Not {@code null}.
     * @return The optional value of the property.  Not {@code null}.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    public Optional<String> getClientApplicationProperty(WebProtegePropertyName propertyName) {
        return clientApplicationProperties.getPropertyValue(propertyName);
    }

    /**
     * Gets a client application property value. Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param protegePropertyName The property name.  Not {@code null}.
     * @param defaultValue The value that should be returned if the specified application is not present.  May be {@code null}.
     * @return The value of the property if present, or the default value.  If the value of the property is present then
     * a non-{@code null} value will be returned.  If the value of the property is not present then whether or not
     * a {@code null} value is returned depeneds upon the default value.
     */
    public String getClientApplicationProperty(WebProtegePropertyName protegePropertyName, String defaultValue) {
        Optional<String> value = getClientApplicationProperty(protegePropertyName);
        if(value.isPresent()) {
            return value.get();
        }
        else {
            return defaultValue;
        }
    }

    /**
     * Gets the specified client application property value as a boolean.  Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The property name.  Not {@code null}.
     * @param defaultValue A default value for the property in case it does not exist or the property value cannot be
     * parsed into a boolean.
     * @return The property value.  Not {@code null}.  If the property does not exist then the value of {@code false}
     * will be returned.
     */
    public boolean getClientApplicationProperty(WebProtegePropertyName propertyName, boolean defaultValue) {
        Optional<String> propertyValue = getClientApplicationProperty(propertyName);
        try {
            return Boolean.parseBoolean(propertyValue.or(Boolean.toString(defaultValue)));
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the specified client application property value as an integer.  Note:  Client application property values
     * are immutable.  The properties and their values are determined on the server at startup.
     * @param propertyName The property name.  Not {@code null}.
     * @param defaultValue A default value for the property incase it does not exist or the property value cannot be
     * parsed into an integer.
     * @return The property value.  Not {@code null}.
     */
    public int getClientApplicationProperty(WebProtegePropertyName propertyName, int defaultValue) {
        Optional<String> propertyValue = getClientApplicationProperty(propertyName);
        if(!propertyValue.isPresent()) {
            return defaultValue;
        }
        else {
            try {
                return Integer.parseInt(propertyValue.get());
            }
            catch (NumberFormatException e) {
                com.google.gwt.core.shared.GWT.log("NumberFormatException while parsing " + propertyValue.get() + " as an integer.");
                return defaultValue;
            }
        }
    }

//    @Override
//    public boolean hasWritePermissionForProject(UserId userId, ProjectId projectId) {
//        Optional<Project> project = projectManager.getProject(projectId);
//        return project.isPresent() && project.get().hasWritePermission(userId);
//    }
//
//    @Override
//    public boolean hasReadPermissionForProject(UserId userId, ProjectId projectId) {
//        Optional<Project> project = projectManager.getProject(projectId);
//        return project.isPresent() && project.get().hasReadPermission(userId);
//    }
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
            clientApplicationProperties = ClientObjectReader.create(
                    "clientApplicationProperties", new ClientApplicationPropertiesDecoder()
            ).read();
            taskFinishedCallback.taskComplete();
        }

        @Override
        public String getName() {
            return "InitializeClientApplicationProperties";
        }
    }

    private class RestoreUserFromServerSessionTask implements ApplicationInitManager.ApplicationInitializationTask {

        @Override
        public void run(final ApplicationInitManager.ApplicationInitTaskCallback taskFinishedCallback) {
            userManager = LoggedInUserManager.getAndRestoreFromServer(eventBus, dispatchServiceManager, Optional.<AsyncCallback<UserDetails>>of(new AsyncCallback<UserDetails>() {
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
