package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
public class CollectionViewPlaceTokenizer implements WebProtegePlaceTokenizer<CollectionViewPlace> {


    private static final String PROJECTS = "projects/";

    private static final String COLLECTIONS = "/collections/";

    private static final String FORMS = "/forms/";

    private static final String SELECTION = "?selection=";

    private static RegExp regExp = RegExp.compile(PROJECTS + "(.{36})" + COLLECTIONS + "(.{36})" + FORMS + "([^\\?]*)(\\" + SELECTION + "(.*))?" );

    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof CollectionViewPlace;
    }

    @Override
    public CollectionViewPlace getPlace(String token) {
        MatchResult result = regExp.exec(token);
        ProjectId projectId = ProjectId.get(result.getGroup(1));
        CollectionId collectionId = CollectionId.get(result.getGroup(2));
        FormId formId = FormId.get(result.getGroup(3));
        String selectionString = result.getGroup(5);
        GWT.log("[CollectionViewPlaceTokenizer] Selection string: " + selectionString);
        Optional<CollectionItem> selection = Optional.ofNullable(selectionString).map(CollectionItem::get);
        return new CollectionViewPlace(
                projectId,
                collectionId,
                formId,
                selection
        );
    }

    @Override
    public String getToken(CollectionViewPlace place) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROJECTS);
        sb.append(place.getProjectId().getId());
        sb.append(COLLECTIONS);
        sb.append(place.getCollectionId().getId());
        sb.append(FORMS);
        sb.append(place.getFormId().getId());
        place.getSelection().ifPresent(sel -> {
            sb.append(SELECTION);
            sb.append(sel.getName());
        });
        return sb.toString();
    }
}
