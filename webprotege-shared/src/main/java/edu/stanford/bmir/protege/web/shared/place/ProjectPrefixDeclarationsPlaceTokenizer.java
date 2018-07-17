package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 */
public class ProjectPrefixDeclarationsPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectPrefixDeclarationsPlace> {

    private final RegExp pattern = RegExp.compile("projects/(.{36})/prefixes");

    private static final int PROJECT_ID_GROUP = 1;


    @Override
    public boolean matches(String token) {
        return pattern.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof ProjectPrefixDeclarationsPlace;
    }

    @Override
    public ProjectPrefixDeclarationsPlace getPlace(String s) {
        MatchResult matchResult = pattern.exec(s);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(PROJECT_ID_GROUP);
        if(!ProjectId.isWelFormedProjectId(projectIdString)) {
            return null;
        }
        return new ProjectPrefixDeclarationsPlace(ProjectId.get(projectIdString));
    }

    @Override
    public String getToken(ProjectPrefixDeclarationsPlace projectPrefixDeclarationsPlace) {
        return "projects/" + projectPrefixDeclarationsPlace.getProjectId().getId() + "/prefixes";
    }
}
