package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.login.LoginPlace;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class SignInRequiredHandlerImpl implements SignInRequiredHandler {

    private final PlaceController placeController;

    @Inject
    public SignInRequiredHandlerImpl(PlaceController placeController) {
        this.placeController = placeController;
    }

    @Override
    public void handleSignInRequired(@Nonnull Place redirectTo) {
        GWT.log("[SignInRequiredHandler] Handling sign in required");
        LoginPlace loginPlace;
        GWT.log("[SignInRequiredHandler] Place: " + redirectTo);
        if(!(redirectTo instanceof LoginPlace)) {
            loginPlace = new LoginPlace(redirectTo);
        }
        else {
            loginPlace = new LoginPlace();
        }
        placeController.goTo(loginPlace);
    }
}
