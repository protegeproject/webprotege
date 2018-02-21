package edu.stanford.bmir.protege.web.client.app;

import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22 Dec 2017
 */
public class ReadLoggedInUserInitializationTask implements ApplicationInitManager.ApplicationInitializationTask {

    @Nonnull
    private final LoggedInUserManager loggedInUserManager;

    @Inject
    public ReadLoggedInUserInitializationTask(@Nonnull LoggedInUserManager loggedInUserManager) {
        this.loggedInUserManager = loggedInUserManager;
    }

    @Override
    public void run(ApplicationInitManager.ApplicationInitTaskCallback callback) {
        loggedInUserManager.readInitialUserInSession();
        callback.taskComplete();
    }

    @Override
    public String getName() {
        return "Init Logged In User";
    }
}
