package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.client.form.FormsPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
public class EditProjectFormsUiHandler {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public EditProjectFormsUiHandler(@Nonnull ProjectId projectId,
                                     @Nonnull PlaceController placeController) {
        this.projectId = projectId;
        this.placeController = placeController;
    }

    public void handleEditProjectForms() {
        Place backHere = placeController.getWhere();
        Place formsPlace = FormsPlace.get(projectId, Optional.of(backHere));
        placeController.goTo(formsPlace);
    }
}
