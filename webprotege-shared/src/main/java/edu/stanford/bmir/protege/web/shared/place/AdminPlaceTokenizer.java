package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.place.ApplicationSettingsPlace;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class AdminPlaceTokenizer implements WebProtegePlaceTokenizer<ApplicationSettingsPlace> {

    public static final String ADMIN = "application/settings";

    @Override
    public boolean matches(String token) {
        return ADMIN.equals(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof ApplicationSettingsPlace;
    }

    @Override
    public ApplicationSettingsPlace getPlace(String token) {
        return ApplicationSettingsPlace.get();
    }

    @Override
    public String getToken(ApplicationSettingsPlace place) {
        return ADMIN;
    }
}
