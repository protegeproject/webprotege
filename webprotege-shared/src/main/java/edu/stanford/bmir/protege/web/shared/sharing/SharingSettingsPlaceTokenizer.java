package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingSettingsPlaceTokenizer implements WebProtegePlaceTokenizer<SharingSettingsPlace> {

    private static final String PROJECTS = "projects/";

    private static final String SHARING = "/sharing";

    private static final RegExp pattern = RegExp.compile(PROJECTS + "(" + UUIDUtil.UUID_PATTERN + ")" + SHARING);

    @Override
    public boolean matches(String token) {
        return pattern.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof SharingSettingsPlace;
    }

    @Override
    public SharingSettingsPlace getPlace(String token) {
        String trimmedToken = token.trim();
        if(!pattern.test(trimmedToken)) {
            return null;
        }
        MatchResult matchResult = pattern.exec(trimmedToken);
        String projectIdString = matchResult.getGroup(1);
        return new SharingSettingsPlace(ProjectId.get(projectIdString));
    }

    @Override
    public String getToken(SharingSettingsPlace place) {
        return PROJECTS + place.getProjectId().getId() + SHARING;
    }
}
