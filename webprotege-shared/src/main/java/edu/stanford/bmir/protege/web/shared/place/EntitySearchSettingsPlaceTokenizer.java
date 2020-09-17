package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchSettingsPlaceTokenizer implements WebProtegePlaceTokenizer<EntitySearchSettingsPlace> {


    private static final String PROJECTS = "projects/";

    private static final String SEARCH_SETTINGS = "/settings/search";

    private static RegExp regExp = RegExp.compile(PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + SEARCH_SETTINGS);



    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof EntitySearchSettingsPlace;
    }

    @Override
    public EntitySearchSettingsPlace getPlace(String token) {
        MatchResult matchResult = regExp.exec(token);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return EntitySearchSettingsPlace.get(projectId, Optional.empty());
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(EntitySearchSettingsPlace place) {
        return PROJECTS + place.getProjectId().getId() + SEARCH_SETTINGS;
    }
}
