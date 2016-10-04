package edu.stanford.bmir.protege.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitManagerInitializationTask;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
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

    private final LoggedInUserManager userManager;

    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    protected WebProtegeInitializer(DispatchServiceManager dispatchServiceManager, LoggedInUserManager loggedInUserManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.userManager = loggedInUserManager;
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
                    MessageBox.showMessage("WebProtege has been upgraded.  Please clear your browser caches and refresh your browser.");
                }
            }
        });
        runInitTasks(initCompleteCallback);
    }

    /**
     * Run the list of init tasks.  After successful completion the {@link WebProtegeInitializer#properlyInitialized} flag
     * will be set to {@code true}.
     * @param callback A call back which will be called either when initialization has finished or when initialization
     * has failed.  Not {@code null}.
     */
    private void runInitTasks(final AsyncCallback<Void> callback) {
        List<ApplicationInitManager.ApplicationInitializationTask> initTasks = new ArrayList<>();
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






}
