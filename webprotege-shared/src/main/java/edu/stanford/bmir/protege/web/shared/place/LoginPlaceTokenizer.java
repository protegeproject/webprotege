package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.login.LoginPlace;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginPlaceTokenizer implements WebProtegePlaceTokenizer<LoginPlace> {

    private static final String LOGIN = "login";

    @Override
    public boolean matches(String token) {
        return LOGIN.equals(token);
    }

    public LoginPlace getPlace(String token) {
        GWT.log("[LoginPlaceTokenizer] getPlace Token: " + token);
        return new LoginPlace();
    }

    public String getToken(LoginPlace place) {
        return LOGIN;
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof LoginPlace;
    }
}
