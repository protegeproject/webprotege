package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class ProjectListPlaceTokenizer implements PlaceTokenizer<ProjectListPlace> {

    public ProjectListPlace getPlace(String token) {
        return new ProjectListPlace();
    }

    public String getToken(ProjectListPlace place) {
        return "/projects";
    }
}
