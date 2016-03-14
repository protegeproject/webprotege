package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingSettingsPlaceTokenizer implements PlaceTokenizer<SharingSettingsPlace> {

    private final RegExp pattern = RegExp.compile("/projects/(" + ProjectId.UUID_PATTERN + ")/sharing");

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
        return "/projects/" + place.getProjectId().getId() + "/sharing";
    }
}
