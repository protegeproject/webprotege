package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static edu.stanford.bmir.protege.web.shared.util.UUIDUtil.UUID_PATTERN;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
public class EditFormPlaceTokenizer implements WebProtegePlaceTokenizer<EditFormPlace> {

    private static final String PROJECTS = "projects/";

    private static final String FORMS = "/forms/";

    private static RegExp regExp = RegExp.compile(PROJECTS + "(" +  UUID_PATTERN + ")" + FORMS + "(" + UUID_PATTERN + ")");

    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof EditFormPlace;
    }

    @Override
    public EditFormPlace getPlace(String token) {
        MatchResult result = regExp.exec(token);
        ProjectId projectId = ProjectId.get(result.getGroup(1));
        FormId formId = FormId.get(result.getGroup(2));
        return EditFormPlace.get(projectId, formId, null);
    }

    @Override
    public String getToken(EditFormPlace place) {
        return PROJECTS +
                place.getProjectId()
                     .getId() +
                FORMS +
                place.getFormId().getId();
    }
}
