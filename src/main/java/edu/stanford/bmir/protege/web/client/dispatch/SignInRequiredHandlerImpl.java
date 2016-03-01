package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.login.LoginPlace;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;

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
    public void handleSignInRequired() {
        MessageBox.showAlert("You are not signed in", "WebProtege requires you to be signed in.  Please sign in to continue.");
        Place currentPlace = placeController.getWhere();
        LoginPlace loginPlace;
        if(!(currentPlace instanceof LoginPlace)) {
            loginPlace = new LoginPlace(currentPlace);
        }
        else {
            loginPlace = new LoginPlace();
        }
        placeController.goTo(loginPlace);
    }
}
