package edu.stanford.bmir.protege.web.client.login;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignInRequestHandlerImpl implements SignInRequestHandler {

    private final PlaceController placeController;

    @Inject
    public SignInRequestHandlerImpl(PlaceController placeController) {
        this.placeController = checkNotNull(placeController);
    }

    @Override
    public void handleSignInRequest() {
        placeController.goTo(new LoginPlace());
    }
}
