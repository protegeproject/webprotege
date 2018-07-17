package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class LanguageSettingsPlaceTokenizer implements WebProtegePlaceTokenizer<LanguageSettingsPlace> {

    private static final String PROJECTS = "projects/";

    private static final String LANGUAGES = "/languages";

    private static RegExp regExp = RegExp.compile(PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + LANGUAGES);


    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof LanguageSettingsPlace;
    }

    @Override
    public LanguageSettingsPlace getPlace(String token) {
        MatchResult matchResult = regExp.exec(token);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return LanguageSettingsPlace.get(projectId);
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(LanguageSettingsPlace place) {
        return PROJECTS + place.getProjectId().getId() + LANGUAGES;
    }
}
