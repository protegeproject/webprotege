package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.client.login.LoginPlace;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignOutRequestHandlerImpl implements SignOutRequestHandler {

    private final LoggedInUserManager loggedInUserManager;

    private final PlaceController placeController;

    @Inject
    public SignOutRequestHandlerImpl(LoggedInUserManager loggedInUserManager, PlaceController placeController) {
        this.loggedInUserManager = loggedInUserManager;
        this.placeController = placeController;
    }

    @Override
    public void handleSignOutRequest() {
        loggedInUserManager.logOutCurrentUser();
    }
}
