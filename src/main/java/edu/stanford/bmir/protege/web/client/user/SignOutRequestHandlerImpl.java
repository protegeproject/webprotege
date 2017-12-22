package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.place.shared.PlaceController;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignOutRequestHandlerImpl implements SignOutRequestHandler {

    @Nonnull
    private final LoggedInUserManager loggedInUserManager;

    @Inject
    public SignOutRequestHandlerImpl(@Nonnull LoggedInUserManager loggedInUserManager) {
        this.loggedInUserManager = checkNotNull(loggedInUserManager);
    }

    @Override
    public void handleSignOutRequest() {
        loggedInUserManager.logOutCurrentUser();
    }
}
