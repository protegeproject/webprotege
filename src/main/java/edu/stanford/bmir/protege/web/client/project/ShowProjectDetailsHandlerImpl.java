package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.place.ProjectSettingsPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ShowProjectDetailsHandlerImpl implements ShowProjectDetailsHandler {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public ShowProjectDetailsHandlerImpl(@Nonnull ProjectId projectId,
                                         @Nonnull PlaceController placeController) {
        this.projectId = checkNotNull(projectId);
        this.placeController = checkNotNull(placeController);
    }

    @Override
    public void handleShowProjectDetails() {
        Place currentPlace = placeController.getWhere();
        placeController.goTo(new ProjectSettingsPlace(projectId, Optional.ofNullable(currentPlace)));
    }
}
