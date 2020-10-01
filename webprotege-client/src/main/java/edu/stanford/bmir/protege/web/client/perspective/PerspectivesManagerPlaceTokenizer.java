package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.place.EntitySearchSettingsPlace;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectivesManagerPlaceTokenizer implements WebProtegePlaceTokenizer<PerspectivesManagerPlace> {
    private static final String PROJECTS = "projects/";

    private static final String PERSPECTIVES_SETTINGS = "/settings/perspectives";

    private static RegExp regExp = RegExp.compile(PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + PERSPECTIVES_SETTINGS);

    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof PerspectivesManagerPlace;
    }

    @Override
    public PerspectivesManagerPlace getPlace(String token) {
        MatchResult matchResult = regExp.exec(token);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return PerspectivesManagerPlace.get(projectId);
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(PerspectivesManagerPlace place) {
        return PROJECTS + place.getProjectId().getId() + PERSPECTIVES_SETTINGS;
    }
}
