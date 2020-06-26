package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsPlaceTokenizer implements WebProtegePlaceTokenizer<FormsPlace> {

    private static final String PROJECTS = "projects/";

    private static final String FORMS = "/forms";

    private static RegExp regExp = RegExp.compile(PROJECTS + "(.{36})" + FORMS + "$");

    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof FormsPlace;
    }

    @Override
    public FormsPlace getPlace(String token) {
        MatchResult result = regExp.exec(token);
        ProjectId projectId = ProjectId.get(result.getGroup(1));
        return FormsPlace.get(projectId, Optional.empty());
    }

    @Override
    public String getToken(FormsPlace place) {
        return PROJECTS +
                place.getProjectId()
                     .getId() +
                FORMS;
    }
}
