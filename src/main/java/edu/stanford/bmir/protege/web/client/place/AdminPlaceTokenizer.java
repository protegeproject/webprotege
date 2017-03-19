package edu.stanford.bmir.protege.web.client.place;

import edu.stanford.bmir.protege.web.shared.place.AdminPlace;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class AdminPlaceTokenizer implements WebProtegePlaceTokenizer<AdminPlace> {

    public static final String ADMIN = "admin";

    @Override
    public boolean matches(String token) {
        return ADMIN.equals(token);
    }

    @Override
    public Class<AdminPlace> getPlaceClass() {
        return AdminPlace.class;
    }

    @Override
    public AdminPlace getPlace(String token) {
        return AdminPlace.get();
    }

    @Override
    public String getToken(AdminPlace place) {
        return ADMIN;
    }
}
