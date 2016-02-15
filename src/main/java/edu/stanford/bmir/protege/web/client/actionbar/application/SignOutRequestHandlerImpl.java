package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.login.LoginPlace;

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
        placeController.goTo(new LoginPlace());
    }
}
