package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignOutRequestHandlerImpl implements SignOutRequestHandler {

    private LoggedInUserManager loggedInUserManager;

    @Inject
    public SignOutRequestHandlerImpl(LoggedInUserManager loggedInUserManager) {
        this.loggedInUserManager = loggedInUserManager;
    }

    @Override
    public void handleSignOutRequest() {
        loggedInUserManager.logOutCurrentUser();
    }
}
