package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.core.client.GWT;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.auth.SignInPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignInRequestHandlerImpl implements SignInRequestHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserManager loggedInUserManager;

    @Inject
    public SignInRequestHandlerImpl(DispatchServiceManager dispatchServiceManager, LoggedInUserManager loggedInUserManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserManager = loggedInUserManager;
    }

    @Override
    public void handleSignInRequest() {
        UserId userId = loggedInUserManager.getCurrentUserId();
        if (userId.isGuest()) {
            SignInPresenter.get(dispatchServiceManager, loggedInUserManager).showLoginDialog();
        }
        else {
            GWT.log("User is already signed in");
        }
    }
}
