package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.client.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.place.ProjectSettingsPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static java.util.Optional.empty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2017
 */
public class ProjectSettingsPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectSettingsPlace> {


    private static final String PROJECTS = "projects/";

    private static final String SETTINGS = "/settings";

    private static RegExp regExp = RegExp.compile(PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + SETTINGS);


    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public Class<ProjectSettingsPlace> getPlaceClass() {
        return ProjectSettingsPlace.class;
    }

    @Override
    public ProjectSettingsPlace getPlace(String token) {
        MatchResult matchResult = regExp.exec(token);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return new ProjectSettingsPlace(projectId, empty());
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(ProjectSettingsPlace place) {
        return PROJECTS + place.getProjectId().getId() + SETTINGS;
    }
}
