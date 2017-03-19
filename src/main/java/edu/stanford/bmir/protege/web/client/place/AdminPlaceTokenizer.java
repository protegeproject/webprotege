package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.place.AdminPlace;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class AdminPlaceTokenizer implements PlaceTokenizer<AdminPlace> {

    @Override
    public AdminPlace getPlace(String token) {
        return AdminPlace.get();
    }

    @Override
    public String getToken(AdminPlace place) {
        return "/admin";
    }
}
